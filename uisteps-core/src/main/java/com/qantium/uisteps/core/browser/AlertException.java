package com.qantium.uisteps.core.browser;

import org.openqa.selenium.UnhandledAlertException;

/**
 * Created by SolAN on 17.03.2016.
 */
public class AlertException extends RuntimeException {

    public AlertException(UnhandledAlertException ex) {
        this("Unexpected alert: " + ex.getAlertText());
}

    public AlertException(String message) {
        super(message);
    }

    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }
}
