package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.UnhandledAlertException;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * Created by Anton Solyankin
 */
public class DisplayWaiting {

    private long delay = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_DELAY));;
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
        perform(startTime, getDelay(), !isNot());
        return perform(startTime, getTimeout(), isNot());
    }


    private boolean perform(long startTime, long timeout, boolean not) {
        long timeDelta;

        do {
            try {
                if (!isSuccessful(not)) {
                    sleep();
                } else {
                    return true;
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
        }catch (Exception ex) {
            return not;
        }
    }


    private void sleep() {
        try {
            Thread.sleep(getPollingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
