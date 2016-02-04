package com.qantium.uisteps.core.browser.pages;

import com.google.common.base.Function;
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.then.Then;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Solan on 02.02.2016.
 */
@NotInit
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private Browser browser;

    public Browser inOpenedBrowser() {
        return browser;
    }

    @Override
    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public <T extends AbstractUIObject> T withName(String name) {
        setName(name);
        return (T) this;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        if (StringUtils.isEmpty(name)) {
            setName(NameConvertor.humanize(getClass()));
        }
        return name;
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    @Override
    public void waitUntil(UIObject uiObject, Function<UIObject, Boolean> condition) {
        inOpenedBrowser().waitUntil(uiObject, condition);
    }

    @Override
    public void waitUntil(Function<UIObject, Boolean> condition){
        inOpenedBrowser().waitUntil(this, condition);
    }

    @Override
    public void waitUntilIsDisplayed(UIObject uiObject) {
        inOpenedBrowser().waitUntilIsDisplayed(uiObject);
    }

    @Override
    public void waitUntilIsDisplayed() {
        waitUntilIsDisplayed(this);
    }

    @Override
    public void afterInitialization() {
        waitUntilIsDisplayed();
    }


    @Override
    public List<WebElement> findElements(By by) {
        return getSearchContext().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getSearchContext().findElement(by);
    }

    @Override
    public SearchContext getSearchContext() {
        return inOpenedBrowser().getDriver();
    }

    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot();
    }
}

