package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

public class GetHtmlFrom extends UIElementAction<String, UIElement> {

    public GetHtmlFrom(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected String apply(Object... args) {
        WebElement wrappedElement = getUIObject().getWrappedElement();
        return wrappedElement.getAttribute("innerHtml");
    }
}
