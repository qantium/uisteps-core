package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

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

    public IsNotDisplayException(UIObject uiObject, Throwable cause) {
        super(uiObject + " is not displayed!", cause);
    }
}
