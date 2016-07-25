package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.then.Then;
import org.codehaus.plexus.util.StringUtils;

/**
 * Created by Anton Solyankin
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
            setName(NameConverter.humanize(getClass()));
        }
        return name;
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        new Then(uiObject);
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public void afterInitialization() {
        isDisplayed();
    }

    @Override
    public boolean isDisplayed() {
        return getDisplayWaiting().perform();
    }

    @Override
    public boolean isNotDisplayed() {
        return getDisplayWaiting().not().perform();
    }

    @Override
    public boolean isDisplayed(long timeout) {
        return getDisplayWaiting().withTimeout(timeout).perform();
    }

    @Override
    public boolean isNotDisplayed(long timeout) {
        return getDisplayWaiting().withTimeout(timeout).not().perform();
    }

    @Override
    public boolean isDisplayed(long timeout, long pollingTime) {
        return getDisplayWaiting().withTimeout(timeout).pollingEvery(pollingTime).perform();
    }

    @Override
    public boolean isNotDisplayed(long timeout, long pollingTime) {
        return getDisplayWaiting().withTimeout(timeout).pollingEvery(pollingTime).not().perform();
    }

    protected abstract Waiting getDisplayWaiting();
}

