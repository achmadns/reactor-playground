package com.github.achmadns.lab;

/**
 * Created by achmad on 19/02/16.
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
