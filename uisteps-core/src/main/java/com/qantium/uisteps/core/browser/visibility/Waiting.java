package com.qantium.uisteps.core.browser.visibility;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public abstract class Waiting {

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

    public void apply() throws WaitingException {
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
