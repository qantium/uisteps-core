package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * Created by Anton Solyankin
 */
public class DisplayWaiting {

    private long delay = 0;
    private long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
    private long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
    private boolean not;
    private final UIObject uiObject;

    public DisplayWaiting(UIObject uiObject) {
        this.uiObject = uiObject;
    }

    public boolean isNot() {
        return not;
    }

    public DisplayWaiting setNot(boolean not) {
        this.not = not;
        return this;
    }

    public DisplayWaiting not() {
        return setNot(true);
    }

    public DisplayWaiting withTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public DisplayWaiting pollingEvery(long pollingTime) {
        this.pollingTime = pollingTime;
        return this;
    }

    public DisplayWaiting withDelay(long delay) {
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

    public boolean perform(long startTime) {
        sleep(getDelay());
        boolean successful;
        long timeDelta;

        do {
            try {
                if (isNot()) {
                    successful = uiObject.isNotCurrentlyDisplayed();
                } else {
                    successful = uiObject.isCurrentlyDisplayed();
                }
            } catch (Exception ex) {
                successful = isNot();
            }

            sleep(getPollingTime());

            long currentTime = System.currentTimeMillis();
            timeDelta = currentTime - startTime;
        } while (!successful && timeDelta <= getTimeout());

        return successful;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
