package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Solyankin
 */
public class UIElementDisplayWaiting extends Waiting {

    private final UIElement uiElement;

    public UIElementDisplayWaiting(UIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected boolean until() {
        return uiElement.getWrappedElement().isDisplayed() ;
    }
}
