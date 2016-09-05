package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.DisplayWaiting;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
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
    private final DisplayWaiting displayWaiting = new DisplayWaiting(this);

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
    public final boolean waitUntilIsDisplayed() {
        return getDisplayWaiting().perform(System.currentTimeMillis());
    }

    @Override
    public final boolean waitUntilIsNotDisplayed() {
        return getDisplayWaiting().not().perform(System.currentTimeMillis());
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public void afterInitialization() {
        if(!waitUntilIsDisplayed()) {
            throw new IsNotDisplayedException(this);
        }
    }

    public AbstractUIObject withDelay(long delay) {
        getDisplayWaiting().withDelay(delay);
        return this;
    }

    public AbstractUIObject withTimeout(long timeout) {
        getDisplayWaiting().withTimeout(timeout);
        return this;
    }

    public AbstractUIObject pollingEvery(long pollingTime) {
        getDisplayWaiting().pollingEvery(pollingTime);
        return this;
    }

    private DisplayWaiting getDisplayWaiting() {
        return displayWaiting;
    }

    @Override
    public boolean isNotCurrentlyDisplayed() {
        return !isCurrentlyDisplayed();
    }
}

