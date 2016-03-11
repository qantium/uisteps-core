/*
 * Copyright 2015 ASolyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.properties.UIStepsProperties;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Anton Solyankin
 */
public class BrowserFactory {

    public Browser getBrowser() {
        DriverBuilder driverBuilder = new DriverBuilder();
        return getBrowser(driverBuilder);
    }

    public Browser getBrowser(DriverBuilder driverBuilder) {
        WebDriver driver = driverBuilder.getWebDriver();
        Browser browser = getBrowser(driver);

        Proxy proxy = driverBuilder.getProxy();
        if (proxy != null) {
            browser.setProxy(proxy.getMobProxy());
        }
        return browser;
    }

    public Browser getBrowser(WebDriver driver) {
        setSettingsTo(driver);
        Browser browser = new Browser();
        browser.setDriver(driver);
        return browser;
    }

    /**
     * Sets default setting to driver: timeout (in milliseconds), width and height
     *
     * @param driver {@link org.openqa.selenium.WebDriver}
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    private void setSettingsTo(WebDriver driver) {

        WebDriver.Options manage = driver.manage();
        long timeout = Long.parseLong(UIStepsProperties.getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        manage.timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);

        String widthProperty = UIStepsProperties.getProperty(BROWSER_WIDTH);
        String heightProperty = UIStepsProperties.getProperty(BROWSER_HEIGHT);
        WebDriver.Window window = manage.window();
        Dimension size = window.getSize();

        int width;
        int height;

        if (!StringUtils.isEmpty(widthProperty)) {
            width = Integer.parseInt(widthProperty);
        } else {
            width = size.width;
        }

        if (!StringUtils.isEmpty(heightProperty)) {
            height = Integer.parseInt(heightProperty);
        } else {
            height = size.height;
        }
        Dimension dimension = new Dimension(width, height);
        manage.window().setSize(dimension);
    }
}
