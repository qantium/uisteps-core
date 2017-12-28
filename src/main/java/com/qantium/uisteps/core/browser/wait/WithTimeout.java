package com.qantium.uisteps.core.browser.wait;

import static com.qantium.uisteps.core.browser.wait.TimeoutBuilder.defaultTimeouts;

public interface WithTimeout {

    TimeoutBuilder getTimeoutBuilder();

    default long getTimeout() {
        return getTimeoutBuilder().getTimeout();
    }

    default long getPollingTime() {
        return getTimeoutBuilder().getPollingTime();
    }

    default long getDelay() {
        return getTimeoutBuilder().getDelay();
    }

    default <T extends WithTimeout> T withDelay(long delay) {
        getTimeoutBuilder().withDelay(delay);
        return (T) this;
    }

    default <T extends WithTimeout> T withTimeout(long timeout) {
        getTimeoutBuilder().withTimeout(timeout);
        return (T) this;
    }

    default <T extends WithTimeout> T pollingEvery(long pollingTime) {
        getTimeoutBuilder().pollingEvery(pollingTime);
        return (T) this;
    }

    default <T extends WithTimeout> T flushTimeout() {
        TimeoutBuilder defaultTimeouts = defaultTimeouts();
        getTimeoutBuilder().withTimeout(defaultTimeouts.getTimeout());
        getTimeoutBuilder().pollingEvery(defaultTimeouts.getPollingTime());
        getTimeoutBuilder().withDelay(defaultTimeouts.getDelay());
        return (T) this;
    }
}
