package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

public interface ActionExecutor {

    default Object perform(UIElement element) throws ActionException {

        return new Action(element.getTimeout(), element.getPollingTime(), element.getDelay()) {

            @Override
            protected Object apply(Object... args) {
                return execute();
            }
        }.perform();
    }

    Object execute();
}