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
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.WebDriverFactory;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import ru.stqa.selenium.factory.RemoteDriverProvider;
import ru.stqa.selenium.factory.WebDriverFactoryMode;

/**
 *
 * @author ASolyankin
 */
public class BrowserFactory {

    static {
        WebDriverFactory.setMode(WebDriverFactoryMode.UNRESTRICTED);
    }
    
    
    
    public Browser getBrowser() {
        return getBrowser(getDesiredCapabilities());
    }

    public Browser getBrowser(Driver driver) {
        return getBrowser(getDesiredCapabilities(driver));
    }

    public Browser getBrowser(Map<String, Object> capabilities) {
        return getBrowser(getDesiredCapabilities(capabilities));
    }

    public Browser getBrowser(Driver driver, Map<String, Object> capabilities) {
        return getBrowser(getDesiredCapabilities(driver, capabilities));
    }
    
    protected Browser getBrowser(Capabilities capabilities) {
        String hub = UIStepsProperties.getProperty(WEBDRIVER_REMOTE_URL);

        if (StringUtils.isEmpty(hub)) {
            return getBrowser(WebDriverFactory.getDriver(capabilities));
        } else {
            return getBrowser(hub, capabilities);
        }
    }

    public Browser getBrowser(WebDriver driver) {
        setSettingsTo(driver);
        Browser browser = new Browser();
        browser.setDriver(driver);
        return browser;
    }

    //Remote
    public Browser getBrowser(String hub) {
        return getBrowser(hub, getDesiredCapabilities());
    }

    public Browser getBrowser(String hub, Driver driver) {
        return getBrowser(hub, getDesiredCapabilities(driver));
    }

    public Browser getBrowser(String hub, Map<String, Object> capabilities) {
        return getBrowser(hub, getDesiredCapabilities(capabilities));
    }

    public Browser getBrowser(String hub, Driver driver, Map<String, Object> capabilities) {
        return getBrowser(hub, getDesiredCapabilities(driver, capabilities));
    }
    
    protected Browser getBrowser(String hub, Capabilities capabilities) {
        WebDriver driver = new RemoteDriverProvider.Default().createDriver(hub, capabilities);
        return getBrowser(driver);
    }

    public DesiredCapabilities getDesiredCapabilities(Map<String, Object> capabilities) {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();
        
        for(String capability: capabilities.keySet()) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }
        return desiredCapabilities;
    }
    
    public DesiredCapabilities getDesiredCapabilities() {
        Driver driver = Driver.valueOf(UIStepsProperties.getProperty(WEBDRIVER_DRIVER).toUpperCase());
        return getDesiredCapabilities(driver);
    }

    public DesiredCapabilities getDesiredCapabilities(Driver driver, Map<String, Object> capabilities) {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(driver);
        
        for(String capability: capabilities.keySet()) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }
        return desiredCapabilities;
    }
    
    public DesiredCapabilities getDesiredCapabilities(Driver driver) {

        switch (driver) {
            case FIREFOX:
                return DesiredCapabilities.firefox();
            case CHROME:
                return DesiredCapabilities.chrome();
            case OPERA:
                return DesiredCapabilities.operaBlink();
            case IEXPLORER:
                return DesiredCapabilities.internetExplorer();
            case EDGE:
                return DesiredCapabilities.edge();
            case SAFARI:
                return DesiredCapabilities.safari();
            case ANDROID:
                return DesiredCapabilities.android();
            case IPHONE:
                return DesiredCapabilities.iphone();
            case IPAD:
                return DesiredCapabilities.ipad();
            case HTMLUNIT:
                return DesiredCapabilities.htmlUnit();
            case HTMLUNITWITHJS:
                return DesiredCapabilities.htmlUnitWithJs();
            case PHANTOMJS:
                return DesiredCapabilities.phantomjs();
            default:
                throw new NoBrowserException("Cannot get capabilities for driver " + driver + "!");
        }
    }

    protected void setSettingsTo(WebDriver driver) {

        WebDriver.Options manage = driver.manage();
        manage.timeouts().setScriptTimeout(Long.parseLong(UIStepsProperties.getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT)), TimeUnit.MILLISECONDS);

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
