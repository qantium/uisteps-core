package com.qantium.uisteps.core.browser.actions;

/**
 * Created by Anton Solyankin
 */

import com.qantium.uisteps.core.browser.pages.UIElement;

public class Clear extends UIElementAction {

    public Clear(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected Object apply() {
        getUIObject().getWrappedElement().clear();
        return null;
    }

    @Override
    public String toString() {
        return "clear \"" + getUIObject() + "\"";
    }
}
