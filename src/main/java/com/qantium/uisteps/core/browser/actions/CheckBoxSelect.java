package com.qantium.uisteps.core.browser.actions;

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
        boolean select = (Boolean) args[0];

        if(select) {
            return select();
        } else {
            return deselect();
        }
    }

    private boolean select() {
        if(!getUIObject().isSelected()) {
            WebElement wrappedElement = getUIObject().getWrappedElement();
            wrappedElement.click();
            return true;
        } else {
            return false;
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

    @Override
    public String toString() {
        return "select \"" + getUIObject() + "\"";
    }
}