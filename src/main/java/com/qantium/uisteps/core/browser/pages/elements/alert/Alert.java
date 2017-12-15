package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import org.openqa.selenium.NoAlertPresentException;

/**
 * Created by Anton Solyankin
 */
@NotInit
public class Alert extends AbstractUIObject {

    private org.openqa.selenium.Alert wrappedAlert;


    public Object accept() {
        inOpenedBrowser().accept(this);
        return null;
    }

    public String getText() {
        return inOpenedBrowser().getTextFrom(this);
    }

    @Override
    protected Object setValue(Object value) {
        return accept();
    }

    public org.openqa.selenium.Alert getWrappedAlert() {
        if (wrappedAlert == null) {
            wrappedAlert = inOpenedBrowser().getDriver().switchTo().alert();
        }
        return wrappedAlert;
    }

    @Override
    public boolean isCurrentlyDisplayed() {
        try {
            getText();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }
}
