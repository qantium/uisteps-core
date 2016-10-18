package com.qantium.uisteps.core.browser.pages.elements.actions;

/**
 * Created by Anton Solyankin
 */
public class ActionException extends RuntimeException {

    public ActionException(Action action) {
        super("Cannot " + action);
    }

    public ActionException(Action action, Throwable cause) {
        super("Cannot " + action, cause);
    }
}
