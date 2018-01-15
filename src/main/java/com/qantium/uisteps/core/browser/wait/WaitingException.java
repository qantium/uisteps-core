package com.qantium.uisteps.core.browser.wait;

/**
 * Created by Anton Solyankin
 */
public class WaitingException extends RuntimeException {

    public WaitingException(WithTimeout timeouts) {
        super(getMessage(timeouts));
    }

    public WaitingException(WithTimeout timeouts, Throwable cause) {
        super(getMessage(timeouts), cause);
    }

    private static String getMessage(WithTimeout timeouts) {
        return "Waiting timeout " + timeouts.getTimeout()
                + " ms polling every " + timeouts.getPollingTime() + " ms with delay "
                + timeouts.getDelay() + " ms had been exceeded!";
    }
}
