package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.Visible;

/**
 * Created by Anton Solyankin
 */
public class IsNotDisplayedException extends RuntimeException {

    public IsNotDisplayedException(String message) {
        super(message);
    }

    public IsNotDisplayedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IsNotDisplayedException(Visible uiObject, Throwable cause) {
        super(uiObject + " is not displayed!", cause);
    }

    public IsNotDisplayedException(Visible uiObject) {
        this(uiObject, null);
    }
}
