package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.IBrowser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

public interface BrowserActions {

    default void perform(IBrowser browser) throws ActionException {

        long timeout = WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT.getLongValue();
        long pollingTime = WEBDRIVER_TIMEOUTS_POLLING.getLongValue();
        long delay = WEBDRIVER_TIMEOUTS_DELAY.getLongValue();

        new Action(timeout, pollingTime, delay) {

            @Override
            protected Void apply(Object... args) {
                WebDriver driver = browser.getDriver();
                Actions actions = new Actions(driver);
                init(actions).build().perform();
                return null;
            }
        }.perform();
    }

    Actions init(Actions actions);
}
