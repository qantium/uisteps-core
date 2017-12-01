package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

public class GetAttributeFrom extends UIElementAction<String, UIElement> {

    private final String attribute;

    public GetAttributeFrom(UIElement uiElement, String attribute) {
        super(uiElement);
        this.attribute = attribute;
    }

    @Override
    protected String apply(Object... args) {
        WebElement wrappedElement = getUIObject().getWrappedElement();
        return wrappedElement.getAttribute(attribute);
    }
}