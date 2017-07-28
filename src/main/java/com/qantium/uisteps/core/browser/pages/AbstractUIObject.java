package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.DisplayWaiting;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.then.Then;

import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_DELAY;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;
import static java.lang.Long.parseLong;
import static org.codehaus.plexus.util.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
@NotInit
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private IBrowser browser;
    private final DisplayWaiting displayWaiting = new DisplayWaiting(this);
    private long timeout = parseLong(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT.getValue());
    private long pollingTime = parseLong(WEBDRIVER_TIMEOUTS_POLLING.getValue());
    private long delay = parseLong(WEBDRIVER_TIMEOUTS_DELAY.getValue());

    public <T extends AbstractUIObject> T immediately(){
        return withTimeout(0);
    }

    public long getTimeout() {
        return timeout;
    }

    public long getPollingTime() {
        return pollingTime;
    }

    public long getDelay() {
        return delay;
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

    public <T extends AbstractUIObject> T withDelay(long delay) {
        this.delay = delay;
        getDisplayWaiting().withDelay(delay);
        return (T) this;
    }

    public <T extends AbstractUIObject> T withTimeout(long timeout) {
        this.timeout = timeout;
        getDisplayWaiting().withTimeout(timeout);
        return (T) this;
    }

    public <T extends AbstractUIObject> T pollingEvery(long pollingTime) {
        this.pollingTime = pollingTime;
        getDisplayWaiting().pollingEvery(pollingTime);
        return (T) this;
    }

    private DisplayWaiting getDisplayWaiting() {
        return displayWaiting;
    }

    @Override
    public boolean isNotCurrentlyDisplayed() {
        return !isCurrentlyDisplayed();
    }
}

