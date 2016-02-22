package com.github.achmadns.lab;

/**
 * Represent any external resource needed by {@link Action}
 */
public class Resource {
    private volatile boolean busy;

    public String get() {
        if (busy) throw new BusyException();
        return "ABC";
    }

    public boolean busy() {
        return busy = true;
    }

    public boolean free() {
        return busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public String check() {
        if (busy) return "BUSY";
        return "OK";
    }

    public boolean isFree() {
        return !isBusy();
    }
}
