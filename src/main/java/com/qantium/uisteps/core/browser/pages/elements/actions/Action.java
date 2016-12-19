package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public abstract class Action<T> {

    private int timeout = Integer.parseInt(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT.getValue());
    private int pollingTime = Integer.parseInt(WEBDRIVER_TIMEOUTS_POLLING.getValue());

    protected abstract UIObject getUIObject();

    public int getTimeout() {
        return timeout;
    }

    public void withTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPollingTime() {
        return pollingTime;
    }

    public void pollingEvery(int pollingTime) {
        this.pollingTime = pollingTime;
    }

    public T perform(Object... args) throws ActionException {
        long startTime = System.currentTimeMillis();
        getUIObject().afterInitialization();
        ActionException exception;
        long timeDelta;

        do {
            try {
                return apply(args);
            } catch (NoBrowserException | UnhandledAlertException ex) {
                exception = new ActionException(this, ex);
                break;
            } catch (Exception ex) {
                sleep();
                exception = new ActionException(this, ex);
            }

            long currentTime = System.currentTimeMillis();
            timeDelta = currentTime - startTime;
        } while (timeDelta <= getTimeout());

        throw new ActionException(this, exception);
    }

    private void sleep() {
        try {
            Thread.sleep(getPollingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract T apply(Object... args);

}