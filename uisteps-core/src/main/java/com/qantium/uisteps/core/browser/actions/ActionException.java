package com.qantium.uisteps.core.browser.actions;

/**
 * Created by Solan on 28.03.2016.
 */
public class ActionException extends RuntimeException {

    public ActionException(Action action) {
        super("Cannot execute " + action);
    }

    public ActionException(Action action, Throwable cause) {
        super("Cannot " + action, cause);
    }
}
