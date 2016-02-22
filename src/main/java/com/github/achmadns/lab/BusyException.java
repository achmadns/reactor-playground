package com.github.achmadns.lab;

/**
 * Exception thrown when a resource is busy/ unavailable.
 */
public class BusyException extends RuntimeException {
    public BusyException() {
        super("I'm busy!");
    }
}
