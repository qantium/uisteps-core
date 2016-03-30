package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

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

    public NotDisplayException(UIObject uiObject, Throwable cause) {
        super(uiObject + " is displayed!", cause);
    }
}
