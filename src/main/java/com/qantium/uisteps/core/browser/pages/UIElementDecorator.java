package com.qantium.uisteps.core.browser.pages;

import org.openqa.selenium.WebElement;

import java.util.List;

import static com.qantium.uisteps.core.properties.UIStepsProperty.ELEMENT_DECORATOR_STYLE;
import static com.qantium.uisteps.core.properties.UIStepsProperty.ELEMENT_DECORATOR_USE;

public class UIElementDecorator {

    private static ThreadLocal<UIElement> lastUIElement = new ThreadLocal<>();
    private static ThreadLocal<WebElement> lastWebElement = new ThreadLocal<>();
    private static ThreadLocal<Object> lastStyle = new ThreadLocal<>();

    private final UIElement uiElement;
    private final WebElement wrappedElement;

    public UIElementDecorator(UIElement uiElement, WebElement wrappedElement) {
        this.uiElement = uiElement;
        this.wrappedElement = wrappedElement;
    }

    public void execute() {

        if (Boolean.valueOf(ELEMENT_DECORATOR_USE.getValue()) && lastUIElement.get() != null) {
            try {
                lastUIElement.get().executeScript("arguments[0].style = '" + lastStyle.get() + "';", lastWebElement.get());
            } catch (Exception ex) {
                lastUIElement.set(null);
                lastWebElement.set(null);
                lastStyle.set(null);
            }
        }

        lastUIElement.set(uiElement);
        lastWebElement.set(wrappedElement);
        lastStyle.set(wrappedElement.getAttribute("style"));

        StringBuilder style = new StringBuilder();

        if (lastStyle.get() != null) {
            style.append(lastStyle.get()).append(";");
        }
        style.append(ELEMENT_DECORATOR_STYLE.getValue()).append(";");

        if (Boolean.valueOf(ELEMENT_DECORATOR_USE.getValue())) {
            uiElement.executeScript("arguments[0].style = '" + style + "';", wrappedElement);
        }
    }
}
