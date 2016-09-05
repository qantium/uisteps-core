package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Sopyankin
 */
public abstract class UIElementAction<T> extends Action<T> {

    private final UIElement uiElement;

    public UIElementAction(UIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected UIElement getUIObject() {
        return uiElement;
    }
}
