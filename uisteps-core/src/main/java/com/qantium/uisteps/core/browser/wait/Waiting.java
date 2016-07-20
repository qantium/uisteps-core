package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.Browser;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Solan on 23.03.2016.
 */
public abstract class Waiting {// extends FluentWait<WebDriver> {

    private long timeout;
    private long pollingTime;

    public Waiting(long timeout, long pollingTime) {
        this.timeout = timeout;
        this.pollingTime = pollingTime;
    }

    public Waiting() {
        this(
                Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT)),
                Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING))
        );
        //super(browser.getDriver());
//        long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
//        long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
//        withTimeout(timeout, MILLISECONDS).pollingEvery(pollingTime, MILLISECONDS);
    }

    public Waiting withTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Waiting pollingEvery(long pollingTime) {
        this.pollingTime = pollingTime;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public long getPollingTime() {
        return pollingTime;
    }

    public void applay() throws WaitingException {
        long counter = 0;
        while (counter <= getTimeout()) {
            if (until()) return;
            sleep(getPollingTime());
            counter += getPollingTime();
        }
        throw new WaitingException(getTimeout(), getPollingTime());
    }

    private void sleep(long pollingTime) {
        try {
            Thread.sleep(pollingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract boolean until();

}
