package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public class CheckBoxSelect extends UIElementAction<Boolean, CheckBox> {

    public CheckBoxSelect(CheckBox uiElement) {
        super(uiElement);
    }

    @Override
    protected Boolean apply(Object... args) {
        return (Boolean) args[0] ? select() : deselect();
    }

    private boolean select() {
        if(getUIObject().isSelected()) {
            return false;
        } else {
            WebElement wrappedElement = getUIObject().getWrappedElement();
            wrappedElement.click();
            return true;
        }
    }

    private boolean deselect() {
        if(getUIObject().isSelected()) {
            WebElement wrappedElement = getUIObject().getWrappedElement();
            wrappedElement.click();
            return true;
        } else {
            return false;
        }
    }

}