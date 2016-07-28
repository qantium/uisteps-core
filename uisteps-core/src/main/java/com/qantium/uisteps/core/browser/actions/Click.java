package com.qantium.uisteps.core.browser.actions;


import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Solyankin
 */
public class Click extends Action {

    private final UIElement uiElement;

    public Click(UIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected Object apply() {
        uiElement.getWrappedElement().click();
        return null;
    }

    @Override
    public String toString() {
        return "click \"" + uiElement + "\"";
    }
}
