package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.browser.wait.AlertDisplayWaiting;
import com.qantium.uisteps.core.browser.wait.Waiting;

/**
 * Created by Anton Solyankin
 */
@NotInit
public class Alert extends AbstractUIObject {

    private org.openqa.selenium.Alert wrappedAlert;


    @Override
    protected Waiting getDisplayWaiting() {
        return new AlertDisplayWaiting(this);
    }

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
}
