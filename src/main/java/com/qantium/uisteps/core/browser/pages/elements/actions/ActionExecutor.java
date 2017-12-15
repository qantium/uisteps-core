package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.AbstractUIObject;

public interface ActionExecutor<T> {

    default T perform(AbstractUIObject element) throws ActionException {

        element.afterInitialization();

        return new Action<T>(element.getTimeout(), element.getPollingTime(), element.getDelay()) {

            @Override
            protected T apply(Object... args) {
                return execute();
            }
        }.perform();
    }

    T execute();
}