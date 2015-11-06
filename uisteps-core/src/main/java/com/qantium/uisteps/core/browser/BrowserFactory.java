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
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.WebDriverFactory;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 *
 * @author ASolyankin
 */
public class BrowserFactory {

    public Browser getBrowser() {
        return getBrowser(BrowserFactory.this.getDesiredCapabilities());
    }

    public Browser getBrowser(String withDriver) {
        return getBrowser(getDesiredCapabilities(withDriver));
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return getDesiredCapabilities(UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_DRIVER)); 
    }

    public DesiredCapabilities getDesiredCapabilities(String withDriver) {

        switch (withDriver.toLowerCase()) {
            case "firefox":
                return DesiredCapabilities.firefox();
            case "chrome":
                return DesiredCapabilities.chrome();
            case "opera":
                return DesiredCapabilities.operaBlink();
            case "iexplorer":
                return DesiredCapabilities.internetExplorer();
            case "edge":
                return DesiredCapabilities.edge();
            case "safari":
                return DesiredCapabilities.safari();
            case "android":
                return DesiredCapabilities.android();
            case "iphone":
                return DesiredCapabilities.iphone();
            case "ipad":
                return DesiredCapabilities.ipad();
            case "htmlunit":
                return DesiredCapabilities.htmlUnit();
            case "htmlunitwithjs":
                return DesiredCapabilities.htmlUnitWithJs();
            case "phantomjs":
                return DesiredCapabilities.phantomjs();
            default:
                throw new NoBrowserException("Cannot get capabilities for driver " + withDriver + "!");
        }
    }

    public Browser getBrowser(Capabilities capabilities) {
        return getBrowser(WebDriverFactory.getDriver(capabilities));
    }

    public Browser getBrowser(WebDriver withDriver) {
        setSettingsTo(withDriver);
        Browser browser = new Browser();
        browser.setDriver(withDriver);
        return browser;
    }
    
    protected void setSettingsTo(WebDriver driver) {
        
        WebDriver.Options manage = driver.manage();
        manage.timeouts().setScriptTimeout(Long.parseLong(UIStepsProperties.getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT)), TimeUnit.MILLISECONDS);
        
        int width = Integer.parseInt(UIStepsProperties.getProperty(BROWSER_WIDTH));
        int height = Integer.parseInt(UIStepsProperties.getProperty(BROWSER_HEIGHT));
        Dimension dimension = new Dimension(width, height);
        manage.window().setSize(dimension);
    }
}
