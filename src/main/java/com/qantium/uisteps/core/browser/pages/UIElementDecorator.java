package com.qantium.uisteps.core.browser.pages;

import org.openqa.selenium.WebElement;

import static com.qantium.uisteps.core.properties.UIStepsProperty.ELEMENT_DECORATOR_BORDER;
import static com.qantium.uisteps.core.properties.UIStepsProperty.ELEMENT_DECORATOR_USE;

public class UIElementDecorator {

    private static ThreadLocal<UIElement> lastUIElement = new ThreadLocal<>();
    private static ThreadLocal<WebElement> lastWebElement = new ThreadLocal<>();
    private static ThreadLocal<String> lastBorder = new ThreadLocal<>();

    private final UIElement uiElement;
    private final WebElement wrappedElement;

    public UIElementDecorator(UIElement uiElement, WebElement wrappedElement) {
        this.uiElement = uiElement;
        this.wrappedElement = wrappedElement;
    }

    public void execute() {

        if (Boolean.valueOf(ELEMENT_DECORATOR_USE.getValue())) {
            try {
                lastUIElement.get().executeScript("arguments[0].style.border = '" + lastBorder.get() + "';", lastWebElement.get());
            } catch (Exception ex) {

            }

            lastUIElement.set(uiElement);
            lastWebElement.set(wrappedElement);
            lastBorder.set(wrappedElement.getCssValue("border"));
            uiElement.executeScript("arguments[0].style.border = '" + ELEMENT_DECORATOR_BORDER.getValue() + "';", wrappedElement);
        }
    }
}
