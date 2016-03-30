package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 * Created by Solan on 28.03.2016.
 */
public class EnterInto extends Action {

    private final UIElement input;
    private final Object text;

    public EnterInto(Browser browser, UIElement input, Object text) {
        super(browser);
        this.input = input;
        this.text = text;
    }

    @Override
    public void toExecute() {
        WebElement webElement = input.getWrappedElement();
        webElement.clear();
        webElement.sendKeys(text.toString());
    }

    @Override
    public String toString() {
        return "enter \"" + text + "\" into \"" + input + "\"";
    }
}
