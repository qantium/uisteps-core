package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public class GetTextFrom extends UIElementAction<String, UIElement> {

    public GetTextFrom(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected String apply(Object... args) {
        WebElement wrappedElement = getUIObject().getWrappedElement();

        if ("input".equals(wrappedElement.getTagName())) {
            String enteredText = wrappedElement.getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return wrappedElement.getText();
        }
    }
}