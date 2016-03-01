package com.github.achmadns.lab;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;
import reactor.rx.broadcast.Broadcaster;

import java.util.concurrent.TimeUnit;

import static reactor.bus.selector.Selectors.$;
import static reactor.bus.selector.Selectors.T;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@SpringApplicationConfiguration(classes = {App.class})
public class AppTest {
    @Autowired
    private CamelContext camel;
    @Autowired
    private Resource resource;
    @Autowired
    private Action action;
    @Autowired
    private EventBus pool;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void try_out() throws InterruptedException {
        final Timer timer = Environment.get().getTimer();

        // resource check schedule
        final Pausable check = timer.schedule(number -> {
            if (resource.isFree()) {
                pool.notify("resource");
                log.info("Resource is free.");
            }
            log.info("Resource checked.");
        }, 1, TimeUnit.SECONDS, 1000);


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
}