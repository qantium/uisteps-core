package com.qantium.uisteps.core.browser;

import org.openqa.selenium.UnhandledAlertException;

/**
 * Created by SolAN on 17.03.2016.
 */
class AlertException extends RuntimeException {

    AlertException(UnhandledAlertException ex) {
        this("Unexpected alert: " + ex.getAlertText());
}

    AlertException(String message) {
        super(message);
    }

    AlertException(String message, Throwable cause) {
        super(message, cause);
    }
}
