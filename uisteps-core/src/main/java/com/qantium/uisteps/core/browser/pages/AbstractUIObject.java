package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.browser.wait.WaitingException;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.then.Then;
import org.codehaus.plexus.util.StringUtils;

/**
 * Created by Anton Solyankin
 */
@NotInit
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private IBrowser browser;
    private final Waiting displayWaiting = getDisplayWaiting();

    public IBrowser inOpenedBrowser() {
        return browser;
    }

    @Override
    public void setBrowser(IBrowser browser) {
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
    public boolean isDisplayed() {
        try {
            displayWaiting.perform();
            return true;
        } catch (WaitingException ex) {
            return false;
        }
    }

    @Override
    public boolean isNotDisplayed() {
        try {
            displayWaiting.not().perform();
            return true;
        } catch (WaitingException ex) {
            return false;
        }
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public void afterInitialization() {
        isDisplayed();
    }

    public AbstractUIObject delay(long delay) {
        displayWaiting.withDelay(delay);
        return this;
    }

    public AbstractUIObject withTimeout(long timeout) {
        displayWaiting.withTimeout(timeout);
        return this;
    }

    public AbstractUIObject pollingEvery(long pollingTime) {
        displayWaiting.pollingEvery(pollingTime);
        return this;
    }

    protected abstract Waiting getDisplayWaiting();
}

