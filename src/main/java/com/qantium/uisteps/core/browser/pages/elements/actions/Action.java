package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.browser.wait.DisplayWaiting.startTime;

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

    protected abstract T apply(Object... args);

    public long getTimeout() {
        return timeout;
    }

    public long getDelay() {
        return delay;
    }

    public long getPollingTime() {
        return pollingTime;
    }

    private boolean initAction;

    public T perform(Object... args) throws ActionException {

        sleep(getDelay());

        if (startTime.get() < 0) {
            initAction = true;
            startTime.set(System.currentTimeMillis());
        }

        ActionException exception = new ActionException(this, new TimeoutException("Timeout " + getTimeout() + " is exceeded"));

        while (System.currentTimeMillis() - startTime.get() <= getTimeout()) {
            try {
                T result = apply(args);
                initAction();
                return result;
            } catch (NoBrowserException | UnhandledAlertException ex) {
                exception = new ActionException(this, ex);
                break;
            } catch (Exception ex) {
                exception = errorHandler(ex);
            }
        }
        initAction();
        throw exception;
    }

    private void initAction() {
        if (initAction) {
            initAction = false;
            startTime.set(-1L);
        }
    }

    protected ActionException errorHandler(Exception ex) {
        sleep(getPollingTime());
        return new ActionException(this, ex);
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}