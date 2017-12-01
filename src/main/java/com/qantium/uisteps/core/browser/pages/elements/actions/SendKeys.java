package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

public class SendKeys extends UIElementAction {

    private final CharSequence[] keysToSend;

    public SendKeys(UIElement input, CharSequence... keysToSend) {
        super(input);
        if (keysToSend == null) {
            this.keysToSend = new CharSequence[0];
        } else {
            this.keysToSend = keysToSend;
        }
    }

    @Override
    protected Object apply(Object... args) {
        WebElement webElement = getUIObject().getWrappedElement();

        if (ArrayUtils.isNotEmpty(keysToSend)) {
            webElement.sendKeys(keysToSend);
        }
        return null;
    }

    @Override
    public String toString() {
        return "send keys \"" + Arrays.asList(keysToSend) + "\" into \"" + getUIObject() + "\"";
    }

}
