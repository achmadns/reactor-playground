package com.github.achmadns.lab;

/**
 * Created by achmad on 19/02/16.
 */
public class BusyException extends RuntimeException {
    public BusyException() {
        super("I'm busy!");
    }
}
