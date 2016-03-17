package com.github.achmadns.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.fn.Pausable;

import java.util.concurrent.TimeUnit;

import static reactor.bus.Event.wrap;

@Component
public class UserScheduler {
    private final EventBus bus;
    private final DBUtil db;
    private final Pausable schedule;
    private final Logger log = LoggerFactory.getLogger(UserScheduler.class);

    @Autowired
    public UserScheduler(EventBus bus, DBUtil db) {
        this.bus = bus;
        this.db = db;
        this.schedule = Environment.get().getTimer().schedule(aLong -> getFirstUser(), 1, TimeUnit.SECONDS);
    }

    public User getFirstUser() {
        try {
            final User user = db.getFirstUser();
            bus.notify("user", wrap(user));
            return user;
        } catch (Exception e) {
            log.error("Ouch! ", e);
            bus.notify(e, wrap(e));
        }
        return null;
    }

    public UserScheduler pause() {
        schedule.pause();
        return this;
    }

    public UserScheduler resume() {
        schedule.resume();
        return this;
    }
}
