package com.qantium.uisteps.core.browser.wait;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public abstract class Waiting {

    private long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
    private long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
    private boolean not = true;


    public boolean isNot() {
        return not;
    }

    public Waiting setNot(boolean not) {
        this.not = not;
        return this;
    }

    public Waiting not() {
        return setNot(false);
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

    public boolean perform() throws WaitingException {
        long counter = 0;

        while (counter <= getTimeout()) {
            try {
                if(isNot()) {
                    return !until();
                } else {
                    return until();
                }
            } finally {
                sleep(getPollingTime());
                counter += getPollingTime();
            }
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

    protected abstract boolean until();

}
