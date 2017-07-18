package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Sopyankin
 */
public abstract class UIElementAction<T, E extends UIElement> extends Action<T> {

    private final E uiElement;

    public UIElementAction(E uiElement) {
        super(uiElement.getTimeout(), uiElement.getPollingTime());
        this.uiElement = uiElement;
    }

    @Override
    protected E getUIObject() {
        return uiElement;
    }
}
