package com.qantium.uisteps.core.browser.wait;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

public class TimeoutBuilder implements WithTimeout {

    private long timeout = WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT.getLongValue();
    private long pollingTime = WEBDRIVER_TIMEOUTS_POLLING.getLongValue();
    private long delay = WEBDRIVER_TIMEOUTS_DELAY.getLongValue();

    public static TimeoutBuilder defaultTimeouts() {
        return new TimeoutBuilder();
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public long getPollingTime() {
        return pollingTime;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    public TimeoutBuilder withDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public <T extends WithTimeout> T withTimeout(long timeout) {
        this.timeout = timeout;
        return (T) this;
    }

    public <T extends WithTimeout> T pollingEvery(long pollingTime) {
        this.pollingTime = pollingTime;
        return (T) this;
    }

    public <T extends WithTimeout> T flushTimeouts() {
        TimeoutBuilder defaultTimeouts = defaultTimeouts();
        withTimeout(defaultTimeouts.getTimeout());
        pollingEvery(defaultTimeouts.getPollingTime());
        withDelay(defaultTimeouts.getDelay());
        return (T) this;
    }
}