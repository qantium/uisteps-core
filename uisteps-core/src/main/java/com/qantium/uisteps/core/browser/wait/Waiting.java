package com.qantium.uisteps.core.browser.wait;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public abstract class Waiting {

    private long delay = 0;
    private long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
    private long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
    private boolean not;


    public boolean isNot() {
        return not;
    }

    public Waiting setNot(boolean not) {
        this.not = not;
        return this;
    }

    public Waiting not() {
        return setNot(true);
    }

    public Waiting withTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Waiting pollingEvery(long pollingTime) {
        this.pollingTime = pollingTime;
        return this;
    }

    public Waiting withDelay(long delay) {
        this.delay = delay;
        return this;
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

    public void perform() {
        long counter = 0;
        Exception waitingException = null;

        while (counter <= getTimeout()) {
            try {
                if (until()) {
                    return;
                }
            } catch (Exception ex) {
                waitingException = ex;
            } finally {
                sleep();
                counter += getPollingTime();
            }
        }
        throw new WaitingException(getTimeout(), getPollingTime(), waitingException);
    }

    private void sleep() {
        try {
            Thread.sleep(getPollingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected abstract boolean until();

}
