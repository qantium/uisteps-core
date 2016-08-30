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


    public void accept() {
        inOpenedBrowser().accept(this);
    }

    public String getText() {
        return getWrappedAlert().getText();
    }

    public org.openqa.selenium.Alert getWrappedAlert() {
        if(wrappedAlert == null) {
            wrappedAlert = inOpenedBrowser().getDriver().switchTo().alert();
        }
        return wrappedAlert;
    }

    @Override
    public String toString() {
        return getWrappedAlert().getText();
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
