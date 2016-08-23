package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public class EnterInto extends Action {

    private final UIElement input;
    private final Object text;

    public EnterInto(UIElement input, Object text) {
        this.input = input;
        this.text = text;
    }

    @Override
    protected Object apply() {
        WebElement webElement = input.getWrappedElement();
        webElement.clear();
        webElement.sendKeys(text.toString());
        return null;
    }

    @Override
    public String toString() {
        return "enter \"" + text + "\" into \"" + input + "\"";
    }
}
