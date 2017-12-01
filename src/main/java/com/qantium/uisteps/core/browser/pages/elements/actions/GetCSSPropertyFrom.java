package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

public class GetCSSPropertyFrom extends UIElementAction<String, UIElement> {

    private final String cssProperty;

    public GetCSSPropertyFrom(UIElement uiElement, String cssProperty) {
        super(uiElement);
        this.cssProperty = cssProperty;
    }

    @Override
    protected String apply(Object... args) {
        WebElement wrappedElement = getUIObject().getWrappedElement();
        return wrappedElement.getCssValue(cssProperty);
    }
}