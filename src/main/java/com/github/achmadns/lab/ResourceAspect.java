package com.github.achmadns.lab;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.EventBus;

/**
 * Created by achmad on 15/03/16.
 */
@Aspect
@Component
public class ResourceAspect {
    private final EventBus pool;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ResourceAspect(EventBus pool) {
        this.pool = pool;
    }

    @Before("execution(* com.github.achmadns.lab.Resource.get())")
    public void afterGet(JoinPoint joinPoint) {
        log.info("Resource aspect got result will be executed.");
    }
}
