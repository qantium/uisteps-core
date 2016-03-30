package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.then.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Anton Solyankin
 */
@NotInit
public class Alert extends AbstractUIObject {

    private org.openqa.selenium.Alert wrappedAlert;

    @Override
    public boolean isDisplayed() {
        try {
            getText();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void accept() {
        inOpenedBrowser().accept(this);
    }

    public String getText() {
        return getWrappedAlert().getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return inOpenedBrowser().getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return inOpenedBrowser().getDriver().findElement(by);
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
