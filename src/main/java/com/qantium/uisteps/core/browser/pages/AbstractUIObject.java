package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.TimeoutBuilder;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.then.Then;

import static org.codehaus.plexus.util.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
@NotInit
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private IBrowser browser;
    private final TimeoutBuilder timeoutBuilder = new TimeoutBuilder();

    public <T extends AbstractUIObject> T immediately() {
        return withTimeout(0);
    }


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
        if (isEmpty(name)) {
            setName(NameConverter.humanize(getClass()));
        }
        return name;
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public TimeoutBuilder getTimeoutBuilder() {
        return timeoutBuilder;
    }

    public abstract String getText();

    @Override
    public boolean isNotCurrentlyDisplayed() {
        return !isCurrentlyDisplayed();
    }

    @Override
    public String toString() {
        return getName();
    }

}
