package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.NoBrowserException;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Solan on 28.03.2016.
 */
public abstract class Action {

    private final Browser browser;
    private int timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
    private int pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
    private int counter = 0;
    private int attempts = timeout / pollingTime;


    public Action(Browser browser) {
        this.browser = browser;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPollingTime() {
        return pollingTime;
    }

    public void setPollingTime(int pollingTime) {
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

    public void execute() throws ActionException {

        ActionException exception = null;

        while (condition()) {

            try {
                toExecute();
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

    public abstract void toExecute();

    public Browser getBrowser() {
        return browser;
    }

    protected boolean condition() {
        return counter <= timeout && counter <= attempts * pollingTime;
    }

}