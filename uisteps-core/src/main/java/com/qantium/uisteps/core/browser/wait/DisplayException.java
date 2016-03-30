package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

/**
 * Created by SolAN on 22.03.2016.
 */
public class DisplayException extends RuntimeException {

    public DisplayException(String message) {
        super(message);
    }

    public DisplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisplayException(UIObject uiObject, Throwable cause) {
        super(uiObject + " is not displayed!", cause);
    }
}



