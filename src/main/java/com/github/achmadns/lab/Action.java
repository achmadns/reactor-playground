package com.github.achmadns.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Pausable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by achmad on 19/02/16.
 */
public class Action {
    private final Resource resource;
    private final Pausable scheduler;
    private final EventBus bus;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Action(Resource resource, EventBus bus, CountDownLatch latch) {
        this.resource = resource;
        this.bus = bus;
        scheduler = Environment.get().getTimer().schedule(aLong -> {
            try {
                final String response = resource.get();
                log.info("Response {}", response);
                latch.countDown();
            } catch (Exception e) {
                bus.notify(e, Event.wrap(e));
                log.error("Failed to get resource!");
            }
        }, 1, TimeUnit.SECONDS, 1000);
    }

    public void suspend() {
        scheduler.pause();
    }

    public void resume() {
        scheduler.resume();
    }
}
