package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public class EnterInto extends UIElementAction {

    private final Object text;
    
    public EnterInto(UIElement input, Object text) {
        super(input);
        this.text = text;
    }

    @Override
    protected Object apply(Object... args) {
        WebElement webElement = getUIObject().getWrappedElement();
        webElement.clear();
        webElement.sendKeys(text.toString());
        return null;
    }

    @Override
    public String toString() {
        return "enter \"" + text + "\" into \"" + getUIObject() + "\"";
    }
}
