package com.qantium.uisteps.core.browser.wait;

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

    default <T extends TimeoutBuilder> T withDelay(long delay) {
        getTimeoutBuilder().withDelay(delay);
        return (T) this;
    }

    default <T extends TimeoutBuilder> T withTimeout(long timeout) {
        getTimeoutBuilder().withTimeout(timeout);
        return (T) this;
    }

    default <T extends TimeoutBuilder> T pollingEvery(long pollingTime) {
        getTimeoutBuilder().pollingEvery(pollingTime);
        return (T) this;
    }
}
