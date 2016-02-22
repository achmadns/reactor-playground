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

    public Action(Resource resource, EventBus bus) {
        scheduler = initScheduler(resource, bus);
    }

    private Pausable initScheduler(Resource resource, EventBus bus) {
        final Pausable scheduler = Environment.get().getTimer().schedule(aLong -> {
            try {
                final String response = resource.get();
                bus.notify("response", wrap(response));
            } catch (Exception e) {
                bus.notify(e, wrap(e));
                log.error("Failed to get resource!");
            }
        }, 1, TimeUnit.SECONDS, 1000);
        return scheduler;
    }

    public void suspend() {
        scheduler.pause();
    }

    public void resume() {
        scheduler.resume();
    }
}
