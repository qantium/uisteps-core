package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.NoBrowserException;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public abstract class Action {

    private int timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
    private int pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
    private int counter = 0;
    private int attempts = timeout / pollingTime;

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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void perform() throws ActionException {

        ActionException exception = null;

        while (condition()) {

            try {
                apply();
                exception = null;
                break;
            } catch (NoBrowserException | UnhandledAlertException ex) {
                exception = new ActionException(this, ex);
                break;
            } catch (Exception ex) {
                sleep(pollingTime);
                exception = new ActionException(this, ex);
                counter += pollingTime;
            }
        }

        counter = 0;

        if (exception != null) {
            throw new ActionException(this, exception);
        }
    }

    private void sleep(long pollingTime) {
        try {
            Thread.sleep(pollingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void apply();

    protected boolean condition() {
        return counter <= timeout && counter <= attempts * pollingTime;
    }

}