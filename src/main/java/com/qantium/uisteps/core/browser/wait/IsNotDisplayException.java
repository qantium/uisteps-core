package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.Visible;

/**
 * Created by Anton Solyankin
 */
public class IsNotDisplayException extends RuntimeException {

    public IsNotDisplayException(String message) {
        super(message);
    }

    public IsNotDisplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public IsNotDisplayException(Visible uiObject, Throwable cause) {
        super(uiObject + " is not displayed!", cause);
    }

    public IsNotDisplayException(Visible uiObject) {
        this(uiObject, null);
    }
}
