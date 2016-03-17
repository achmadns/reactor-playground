package com.github.achmadns.lab;

import org.apache.camel.CamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.sql2o.Sql2oException;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;
import reactor.rx.broadcast.Broadcaster;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.bus.selector.Selectors.$;
import static reactor.bus.selector.Selectors.T;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@SpringApplicationConfiguration(classes = {App.class})
public class AppTest {
    static {
        Environment.initializeIfEmpty();
    }

    @Autowired
    private CamelContext camel;
    @Autowired
    private Resource resource;
    @Autowired
    private Action action;
    @Autowired
    private EventBus pool;
    @Autowired
    private DBUtil db;
    @Autowired
    private UserScheduler userScheduler;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Timer timer = Environment.get().getTimer();

    @Test
    public void try_out() throws InterruptedException {

        // resource check schedule
        final Pausable check = timer.schedule(number -> {
            if (resource.isFree()) {
                pool.notify("resource");
                log.info("Resource is free.");
            }
            log.info("Resource checked.");
        }, 1, TimeUnit.SECONDS, 1000);

        pool.on($("aspect.get"), event -> log.info("Got from aspect.get {}", event.getData()));

        // resume the camel when resource is available
        // consumer is resumed, not the endpoint
        pool.on($("resource"), event -> {
            try {
                camel.resume();
                log.info("Camel resumed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // suspend the camel when resource is busy
        // consumer is suspended, not the endpoint
        pool.on(T(BusyException.class), event -> {
            try {
                camel.suspend();
                log.info("Camel suspended.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // pause the resource checking when resource is available
        pool.on($("resource"), event -> {
            check.pause();
            log.info("Resource check paused.");
        });

        // stop the resource check initially
        check.pause();

        // resume the resource checking when resource is busy
        pool.on(T(BusyException.class), event -> {
            check.resume();
            log.info("Resource check resumed.");
        });

        final Broadcaster<String> stream = Broadcaster.create(Environment.get());

        // instead of listening from 'response' event, we listen to 'processed' event
        // which will be notified after camel context processed an event
        pool.on($("processed"), (Event<String> e) -> {
            final String response = e.getData();
            stream.onNext(response);
            log.info("Got {}", response);
        });
        stream.next().await();

        log.info("Wait for first response.");

        // make the resource busy
        resource.busy();

        // free the resource at 2nd second
        timer.submit(number -> resource.free(), 2, TimeUnit.SECONDS);
        // make the resource busy at 5th second
        timer.submit(number -> resource.busy(), 5, TimeUnit.SECONDS);
        // free the resource at 7th second
        timer.submit(number -> resource.free(), 7, TimeUnit.SECONDS);

        // wait for 10 seconds
        Thread.sleep(10000);

    }

    @Test
    public void check_db_should_ok() {
        assertThat(db.check()).isTrue();
    }

    @Test
    public void get_first_user_should_success() {
        assertThat(db.getFirstUser()).isEqualTo(new User("fulan", 23));
    }


    @Test
    public void try_user_scheduler() throws InterruptedException {

        // resource check schedule
        final Pausable check = timer.schedule(number -> {
            if (db.check()) {
                pool.notify("db.connected");
                log.info("DB is connected.");
            }
            log.info("DB checked.");
        }, 1, TimeUnit.SECONDS, 1000);

        // resume the user scheduler when db is connected
        pool.on($("db.connected"), event -> {
            try {
                userScheduler.resume();
                log.info("user scheduler started");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // suspend the user scheduler when there is problem with data access
        pool.on(T(Sql2oException.class), event -> {
            try {
                userScheduler.pause();
                log.info("Problem with data access!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // pause the resource checking when resource is available
        pool.on($("db.connected"), event -> {
            check.pause();
            log.info("DB check paused.");
        });

        // stop the resource check initially
        check.pause();

        // resume the resource checking when resource is busy
        pool.on(T(Sql2oException.class), event -> {
            check.resume();
            log.info("DB check resumed.");
        });

        final Broadcaster<String> stream = Broadcaster.create(Environment.get());

        // instead of listening from 'response' event, we listen to 'processed' event
        // which will be notified after camel context processed an event
        pool.on($("user"), (Event<String> e) -> {
            log.info("Got {}", e.getData());
        });
        stream.next().await();

        log.info("Wait for first response.");

        // wait for 10 seconds
        Thread.sleep(30000);

    }
}