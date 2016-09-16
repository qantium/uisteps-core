package com.qantium.uisteps.core.browser.actions;


import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Solyankin
 */
public class Click extends UIElementAction {

    public Click(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected Object apply(Object... args) {
        getUIObject().getWrappedElement().click();
        return null;
    }

    @Override
    public String toString() {
        return "click \"" + getUIObject() + "\"";
    }
}
