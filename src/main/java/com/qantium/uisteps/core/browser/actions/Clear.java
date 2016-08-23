package com.qantium.uisteps.core.browser.actions;

/**
 * Created by Anton Solyankin
 */

import com.qantium.uisteps.core.browser.pages.UIElement;

public class Clear extends Action {

    private final UIElement uiElement;

    public Clear(UIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected Object apply() {
        uiElement.getWrappedElement().clear();
        return null;
    }

    @Override
    public String toString() {
        return "clear \"" + uiElement + "\"";
    }
}
