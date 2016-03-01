package com.github.achmadns.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;
import reactor.rx.broadcast.Broadcaster;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.Runtime.getRuntime;
import static reactor.bus.selector.Selectors.$;
import static reactor.bus.selector.Selectors.T;


/**
 * Created by achmad on 19/02/16.
 */
public class ActionTest {
    static {
        Environment.initializeIfEmpty();
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * I prefer using test instead of main class :)
     *
     * @throws InterruptedException
     */
    @Test(timeOut = 15000)
    public void try_out() throws InterruptedException {
        log.info("Start the test.");
        final Resource resource = new Resource();
        final EventBus bus = EventBus.create(Environment.get(), new ThreadPoolExecutorDispatcher(
                getRuntime().availableProcessors(), 1024));
        final Action action = new Action(resource, bus);
        final Timer timer = Environment.get().getTimer();

        // resource check schedule
        final Pausable check = timer.schedule(number -> {
            if (resource.isFree()) {
                bus.notify("resource");
                log.info("Resource is free.");
            }
            log.info("Resource checked.");
        }, 1, TimeUnit.SECONDS, 1000);

        action.resume();
        // resume the action when resource is available
        bus.on($("resource"), event -> {
            action.resume();
            log.info("Action resumed.");
        });

        // suspend the action when the resource is busy
        bus.on(T(BusyException.class), event -> {
            action.suspend();
            log.info("Action suspended.");
        });

        // pause the resource checking when resource is available
        bus.on($("resource"), event -> {
            check.pause();
            log.info("Resource check paused.");
        });

        // stop the resource check initially
        check.pause();

        // resume the resource checking when resource is busy
        bus.on(T(BusyException.class), event -> {
            check.resume();
            log.info("Resource check resumed.");
        });

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

        // wait until got 3 response event
        final CountDownLatch latch = new CountDownLatch(3);
        final Broadcaster<String> stream = Broadcaster.create(Environment.get());

        // listen on response event
        bus.on($("response"), (Event<String> e) -> {
            stream.onNext(e.getData());
            log.info("Counting down {}", latch.getCount());
            latch.countDown();
        });

        log.info("Wait for count down.");
        latch.await();

        log.info("Done.");
    }
}