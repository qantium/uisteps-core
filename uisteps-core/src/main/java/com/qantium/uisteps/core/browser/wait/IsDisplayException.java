package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

/**
 * Created by SolAN on 22.03.2016.
 */
public class IsDisplayException extends RuntimeException {

    public IsDisplayException(String message) {
        super(message);
    }

    public IsDisplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public IsDisplayException(UIObject uiObject, Throwable cause) {
        super(uiObject + " is displayed!", cause);
    }
}



