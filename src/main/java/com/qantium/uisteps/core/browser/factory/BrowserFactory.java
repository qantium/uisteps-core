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
package com.qantium.uisteps.core.browser.factory;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.Proxy;
import org.openqa.selenium.WebDriver;

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

        String hub = driverBuilder.getHub();
        browser.setHub(hub);

        return browser;
    }

    public Browser getBrowser(WebDriver driver) {
        Browser browser = new Browser();
        browser.setDriver(driver);
        return browser;
    }
}
