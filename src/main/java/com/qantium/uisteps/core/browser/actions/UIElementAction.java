package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Sopyankin
 */
public abstract class UIElementAction<T, E extends UIElement> extends Action<T> {

    private final E uiElement;

    public UIElementAction(E uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected E getUIObject() {
        return uiElement;
    }
}
