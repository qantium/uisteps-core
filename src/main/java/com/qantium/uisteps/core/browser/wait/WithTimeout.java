package com.qantium.uisteps.core.browser.wait;

public interface WithTimeout {

    long getTimeout();

    long getPollingTime();

    long getDelay();
}
