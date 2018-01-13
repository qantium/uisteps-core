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
    public long getTimeout() {
        return timeoutBuilder.getTimeout();
    }

    @Override
    public long getPollingTime() {
        return timeoutBuilder.getPollingTime();
    }

    @Override
    public long getDelay() {
        return timeoutBuilder.getDelay();
    }

    public <T extends AbstractUIObject> T withDelay(long delay) {
        timeoutBuilder.withDelay(delay);
        return (T) this;
    }

    public <T extends AbstractUIObject> T withTimeout(long timeout) {
        timeoutBuilder.withTimeout(timeout);
        return (T) this;
    }

    public <T extends AbstractUIObject> T pollingEvery(long pollingTime) {
        timeoutBuilder.pollingEvery(pollingTime);
        return (T) this;
    }

    public <T extends AbstractUIObject> T flushTimeouts() {
        timeoutBuilder.flushTimeouts();
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

    public abstract String getText();

    @Override
    public String toString() {
        return getName();
    }

}
