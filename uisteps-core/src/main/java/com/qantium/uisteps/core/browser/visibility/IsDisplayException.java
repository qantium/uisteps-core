package com.qantium.uisteps.core.browser.visibility;

import com.qantium.uisteps.core.browser.pages.Visible;

/**
 * Created by Anton Solyankin
 */
public class IsDisplayException extends RuntimeException {

    public IsDisplayException(String message) {
        super(message);
    }

    public IsDisplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public IsDisplayException(Visible uiObject, Throwable cause) {
        super(uiObject + " is displayed!", cause);
    }
}



