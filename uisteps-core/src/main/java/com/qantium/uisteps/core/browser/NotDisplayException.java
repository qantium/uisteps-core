package com.qantium.uisteps.core.browser;

/**
 * Created by Anton Solyankin
 */
public class NotDisplayException extends RuntimeException {

    public NotDisplayException(String message) {
        super(message);
    }

    public NotDisplayException(String message, Throwable cause) {
        super(message, cause);
    }
}
