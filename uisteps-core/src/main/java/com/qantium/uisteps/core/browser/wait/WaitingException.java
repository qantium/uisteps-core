package com.qantium.uisteps.core.browser.wait;

/**
 * Created by Solan on 20.07.2016.
 */
public class WaitingException extends Exception {

    public WaitingException(String message) {
        super(message);
    }

    public WaitingException(long timeout, long pollingTime) {
        this("Waiting timeout timeout " + timeout + " ms polling every " + pollingTime + " ms had been exceeded!");
    }
}
