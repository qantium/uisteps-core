package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.elements.TextField;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public class EnterInto extends TypeInto {

    
    public EnterInto(TextField input, Object text) {
        super(input, text);
    }

    @Override
    protected Object apply(Object... args) {
        WebElement webElement = getUIObject().getWrappedElement();
        webElement.clear();
        return super.apply(args);
    }

}
