package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.elements.TextField;
import org.openqa.selenium.WebElement;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.NULL_VALUE;

/**
 * Created by Anton Solyankin
 */
public class TypeInto extends UIElementAction {
    private final Object text;
    private final String nullValue = getProperty(NULL_VALUE);

    public TypeInto(TextField input, Object text) {
        super(input);
        this.text = text;
    }

    protected String getText() {
        if(text != null) {
            return text.toString();
        } else {
            return null;
        }
    }

    @Override
    protected Object apply(Object... args) {
        WebElement webElement = getUIObject().getWrappedElement();

        if(textIsNotEmpty()) {
            webElement.sendKeys(getText());
        }
        return null;
    }

    protected boolean textIsNotEmpty() {
        return  getText() != null && !getNullValue().equals(getText());
    }

    protected String getNullValue() {
        return nullValue;
    }

    @Override
    public String toString() {
        return "enter \"" + getText() + "\" into \"" + getUIObject() + "\"";
    }
}
