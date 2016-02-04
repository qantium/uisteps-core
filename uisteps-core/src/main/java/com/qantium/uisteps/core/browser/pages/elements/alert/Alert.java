package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Solan on 01.02.2016.
 */
@NotInit
public class Alert extends AbstractUIObject {

    private org.openqa.selenium.Alert wrappedAllert;

    @Override
    public boolean isDisplayed() {

        try {
            wrappedAllert = inOpenedBrowser().getDriver().switchTo().alert();
            wrappedAllert.getText();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void accept() {
        inOpenedBrowser().accept(this);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return inOpenedBrowser().getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return inOpenedBrowser().getDriver().findElement(by);
    }

    public org.openqa.selenium.Alert getWrappedAllert() {
        return wrappedAllert;
    }
}
