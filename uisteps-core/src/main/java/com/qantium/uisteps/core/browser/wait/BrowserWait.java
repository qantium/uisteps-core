package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * Created by Solan on 23.03.2016.
 */
public class BrowserWait extends FluentWait<WebDriver> {

    public BrowserWait(Browser browser) {
        super(browser.getDriver());
        long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
        withTimeout(timeout, TimeUnit.MILLISECONDS).pollingEvery(pollingTime, TimeUnit.MILLISECONDS);
    }

}
