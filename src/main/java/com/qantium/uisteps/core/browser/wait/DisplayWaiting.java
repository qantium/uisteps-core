package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * Created by Anton Solyankin
 */
public class DisplayWaiting {

    private long delay = WEBDRIVER_TIMEOUTS_DELAY.getIntValue();
    private long timeout = WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT.getIntValue();
    private long pollingTime = WEBDRIVER_TIMEOUTS_POLLING.getIntValue();
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

    public DisplayWaiting  immediately() {
        return withTimeout(0);
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
        return perform(startTime, getTimeout(), isNot());
    }


    private boolean perform(long startTime, long timeout, boolean not) {
        long timeDelta;
        sleep(getDelay());

        do {
            try {
                if (isSuccessful(not)) {
                    return true;
                } else {
                    sleep(getPollingTime());
                }
                long currentTime = System.currentTimeMillis();
                timeDelta = currentTime - startTime;
            } catch (NoBrowserException | UnhandledAlertException ex) {
                break;
            }
        } while (timeDelta <= timeout);

        return false;
    }

    private boolean isSuccessful(boolean not) {
        try {
            if (not) {
                return uiObject.isNotCurrentlyDisplayed();
            } else {
                return uiObject.isCurrentlyDisplayed();
            }
        } catch (NoBrowserException | UnhandledAlertException ex) {
            throw ex;
        } catch (Exception ex) {
            return not;
        }
    }


    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
