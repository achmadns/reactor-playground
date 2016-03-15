package com.github.achmadns.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.fn.Pausable;

import java.util.concurrent.TimeUnit;

import static reactor.bus.Event.wrap;

/**
 * Represent any recurrent business logic that needs external resource.
 */
public class Action {
    private final Pausable scheduler;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final EventBus bus;
    private final Resource resource;

    public Action(Resource resource, EventBus bus) {
        this.resource = resource;
        this.bus = bus;
        scheduler = initScheduler();
    }

    private Pausable initScheduler() {
        final Pausable scheduler = Environment.get().getTimer().schedule(aLong -> get(), 1, TimeUnit.SECONDS, 1000);
        scheduler.pause();
        return scheduler;
    }

    public String get() {
        try {
            final String response = resource.get();
            bus.notify("response", wrap(response));
            return response;
        } catch (Exception e) {
            bus.notify(e, wrap(e));
            log.error("Failed to get resource!");
        }
        return null;
    }

    public void suspend() {
        scheduler.pause();
        log.info("Action was suspended.");
    }

    public void resume() {
        scheduler.resume();
        log.info("Action was resumed.");
    }
}
