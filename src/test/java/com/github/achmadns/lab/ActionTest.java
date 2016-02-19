package com.github.achmadns.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    @Test(timeOut = 15000)
    public void test_drive() throws InterruptedException {
        log.info("Start the test.");
        final CountDownLatch latch = new CountDownLatch(5);
        final Resource resource = new Resource();
        final EventBus bus = EventBus.create(Environment.get());
        final Action action = new Action(resource, bus, latch);
        final Timer timer = Environment.get().getTimer();
        final Pausable check = timer.schedule(number -> {
            if (resource.isFree()) {
                bus.notify("resource", Event.wrap(""));
                log.info("Resource is free.");
            }
            log.info("Resource checked.");
        }, 1, TimeUnit.SECONDS, 1000);

        bus.on($("resource"), event -> {
            check.pause();
            action.resume();
            log.info("Resource check paused and action resumed.");
        });
        check.pause();
        bus.on(T(BusyException.class), event -> {
            action.suspend();
            check.resume();
            log.info("Resource check resumed and action suspended.");
        });
        log.info("Simulate resource business.");
        resource.busy();
        timer.submit(number -> resource.free(), 2, TimeUnit.SECONDS);
        timer.submit(number -> resource.busy(), 5, TimeUnit.SECONDS);
        timer.submit(number -> resource.free(), 7, TimeUnit.SECONDS);
        Thread.sleep(10000);
        log.info("Wait for nex response.");
        latch.await();
        log.info("Done.");
    }

}