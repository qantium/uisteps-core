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
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.WebDriverFactory;

/**
 *
 * @author ASolyankin
 */
public class BrowserFactory {

    public Browser getBrowser() {
        return getBrowser(UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_DRIVER));
    }

    public Browser getBrowser(String withDriver) {

        switch (withDriver.toLowerCase()) {
            case "firefox":
                return getBrowser(DesiredCapabilities.firefox());
            case "chrome":
                return getBrowser(DesiredCapabilities.chrome());
            case "opera":
                return getBrowser(DesiredCapabilities.operaBlink());
            case "iexplorer":
                return getBrowser(DesiredCapabilities.internetExplorer());
            case "edge":
                return getBrowser(DesiredCapabilities.edge());
            case "safari":
                return getBrowser(DesiredCapabilities.safari());
            case "android":
                return getBrowser(DesiredCapabilities.android());
            case "iphone":
                return getBrowser(DesiredCapabilities.iphone());
            case "ipad":
                return getBrowser(DesiredCapabilities.ipad());
            case "htmlunit":
                return getBrowser(DesiredCapabilities.htmlUnit());
            case "htmlunitwithjs":
                return getBrowser(DesiredCapabilities.htmlUnitWithJs());
            case "phantomjs":
                return getBrowser(DesiredCapabilities.phantomjs());
            default:
                throw new NoBrowserException("Cannot get capabilities for driver " + withDriver + "!");
        }
    }

    protected Browser getBrowser(Capabilities capabilities) {
        return new Browser(WebDriverFactory.getDriver(capabilities));
    }

}
