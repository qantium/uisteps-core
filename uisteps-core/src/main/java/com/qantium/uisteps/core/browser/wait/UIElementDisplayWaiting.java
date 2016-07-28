package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.NotFoundException;

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

        try {
            boolean result = uiElement.getWrappedElement().isDisplayed();
            if(isNot()) {
                return !result;
            } else {
                return result;
            }
        } catch (NotFoundException ex) {
            if(isNot()) {
                return true;
            } else {
                throw new IsNotDisplayException(uiElement, ex);
            }
        }
    }
}
