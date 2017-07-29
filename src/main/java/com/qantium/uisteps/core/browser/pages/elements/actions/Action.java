package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.UnhandledAlertException;

/**
 * Created by Anton Solyankin
 */
public abstract class Action<T> {

    private final long timeout;
    private final long pollingTime;
    private final long delay;

    public Action(long timeout, long pollingTime, long delay) {
        this.timeout = timeout;
        this.pollingTime = pollingTime;
        this.delay = delay;
    }

    protected abstract UIObject getUIObject();

    public long getTimeout() {
        return timeout;
    }

    public long getDelay() {
        return delay;
    }

    public long getPollingTime() {
        return pollingTime;
    }

    public T perform(Object... args) throws ActionException {
        long startTime = System.currentTimeMillis();
        getUIObject().afterInitialization();
        ActionException exception;
        long timeDelta;

        sleep(getDelay());

        do {
            try {
                return apply(args);
            } catch (NoBrowserException | UnhandledAlertException ex) {
                exception = new ActionException(this, ex);
                break;
            } catch (Exception ex) {
                sleep(getPollingTime());
                exception = new ActionException(this, ex);
            }

            long currentTime = System.currentTimeMillis();
            timeDelta = currentTime - startTime;
        } while (timeDelta <= getTimeout());

        throw new ActionException(this, exception);
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract T apply(Object... args);

}