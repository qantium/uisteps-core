package com.qantium.uisteps.core.browser.wait;

/**
 * Created by Anton Solyankin
 */
public class WaitingException extends RuntimeException {

    public WaitingException(String message) {
        super(message);
    }

    public WaitingException(long timeout, long pollingTime) {
        this("Waiting timeout timeout " + timeout + " ms polling every " + pollingTime + " ms had been exceeded!");
    }
}
