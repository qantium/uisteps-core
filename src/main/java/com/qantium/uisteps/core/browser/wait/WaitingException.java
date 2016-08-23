package com.qantium.uisteps.core.browser.wait;

/**
 * Created by Anton Solyankin
 */
public class WaitingException extends RuntimeException {

    public WaitingException(String message) {
        super(message);
    }

    public WaitingException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaitingException(long timeout, long pollingTime) {
        this("Waiting timeout " + timeout + " ms polling every " + pollingTime + " ms had been exceeded!");
    }

    public WaitingException(long timeout, long pollingTime, Throwable cause) {
        this(new WaitingException(timeout, pollingTime).getMessage(), cause);
    }
}
