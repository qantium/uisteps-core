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
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.WebDriverFactory;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.CapabilityType;
import ru.stqa.selenium.factory.RemoteDriverProvider;
import ru.stqa.selenium.factory.WebDriverFactoryMode;

/**
 * Provides opportunity to instatiate browser by specified parameters Uses A.
 * Barancev WebDriverFactory
 *
 * @see ru.stqa.selenium.factory.WebDriverFactory
 *
 * @author ASolyankin
 */
public class BrowserFactory {

    /**
     * Set UNRESTRICTED mode to allow to open several browsers in one thread
     *
     * @see ru.stqa.selenium.factory.WebDriverFactory
     */
    static {
        WebDriverFactory.setMode(WebDriverFactoryMode.UNRESTRICTED);
    }

    /**
     * Opens browser with default settings
     *
     * @return Browser browser
     *
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    public Browser getBrowser() {
        return getBrowser(getDesiredCapabilities());
    }

    /**
     * Opens browser with default settings and specified proxy
     *
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     *
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    public Browser getBrowser(Proxy proxy) {
        return getBrowser(getDesiredCapabilities(), proxy);
    }

    /**
     * Opens browser with specified driver
     *
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @return Browser browser
     *
     */
    public Browser getBrowser(Driver driver) {
        return getBrowser(getDesiredCapabilities(driver));
    }

    /**
     * Opens browser with specified driver and proxy
     *
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     *
     */
    public Browser getBrowser(Driver driver, Proxy proxy) {
        return getBrowser(getDesiredCapabilities(driver), proxy);
    }

    /**
     * Opens default browser with specified capabilities
     *
     * @param capabilities map of capabilities
     * @return Browser browser
     */
    public Browser getBrowser(Map<String, Object> capabilities) {
        return getBrowser(getDesiredCapabilities(capabilities));
    }

    /**
     * Opens default browser with specified capabilities and proxy
     *
     * @param capabilities map of capabilities
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     */
    public Browser getBrowser(Map<String, Object> capabilities, Proxy proxy) {
        return getBrowser(getDesiredCapabilities(capabilities), proxy);
    }

    /**
     * Opens browser with specified driver and capabilities
     *
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @param capabilities map of capabilities
     * @return Browser browser
     *
     */
    public Browser getBrowser(Driver driver, Map<String, Object> capabilities) {
        return getBrowser(getDesiredCapabilities(driver, capabilities));
    }

    /**
     * Opens browser with specified driver, capabilities and proxy
     *
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @param capabilities map of capabilities
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     *
     */
    public Browser getBrowser(Driver driver, Map<String, Object> capabilities, Proxy proxy) {
        return getBrowser(getDesiredCapabilities(driver, capabilities), proxy);
    }

    /**
     * Internal method Can be overrided if you know what you do
     * <p>
     * Checks property webdriver.remote.url, e.g. webdriver.remote.url =
     * http://127.0.0.1:4444/wd/hub If this property is set returns browser with
     * remote driver
     * <p>
     * Checks property webdriver.proxy If this property is set returns browser
     * with started mob proxy server. ip and port of proxy server can be set in
     * webdriver.proxy property
     * <p>
     * Examples:
     * <ul>
     * <li>webdriver.proxy = localhost<li>
     * <li>webdriver.proxy = localhost:7777</li>
     * <li>webdriver.proxy = 127.0.0.1:7777</li>
     * <li>webdriver.proxy = :7777</li>
     * </ul>
     *
     * @param capabilities capabilities for driver
     * @return Browser browser
     *
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    protected Browser getBrowser(DesiredCapabilities capabilities) {
        String proxyProperty = UIStepsProperties.getProperty(WEBDRIVER_PROXY);
        Proxy proxy = null;
        //Check proxy
        if (!StringUtils.isEmpty(proxyProperty)) {

            String[] address = proxyProperty.split(":");

            String ip = null;
            Integer port = null;

            if (address.length > 0 && !StringUtils.isEmpty(address[0])) {
                ip = address[0];
            }

            if (address.length > 1) {
                port = Integer.parseInt(address[1]);
            }
            proxy = new Proxy.ProxyBuilder().setIp(ip).setPort(port).build();
        }
        return getBrowser(capabilities, proxy);
    }

    /**
     * Internal method Can be overrided if you know what you do
     * <p>
     * Checks property webdriver.remote.url, e.g. webdriver.remote.url =
     * http://127.0.0.1:4444/wd/hub If this property is set returns browser with
     * remote driver and specified proxy
     *
     * @param capabilities capabilities for driver
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     *
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    protected Browser getBrowser(DesiredCapabilities capabilities, Proxy proxy) {
        String hub = UIStepsProperties.getProperty(WEBDRIVER_REMOTE_URL);
        Browser browser;

        //Check proxy
        if (proxy != null) {
            capabilities.setCapability(CapabilityType.PROXY, proxy.start());
        }

        //Check remote driver 
        if (StringUtils.isEmpty(hub)) {
            browser = getBrowser(WebDriverFactory.getDriver(capabilities));
        } else {
            browser = getBrowser(hub, capabilities);
        }

        if (proxy != null) {
            browser.setProxy(proxy.getMobProxy());
        }
        return browser;
    }

    /**
     * Opens browser with specified driver
     *
     * @param driver {@link org.openqa.selenium.WebDriver}
     * @return Browser browser
     */
    public Browser getBrowser(WebDriver driver) {
        setSettingsTo(driver);
        Browser browser = new Browser();
        browser.setDriver(driver);
        return browser;
    }

    /**
     * Opens browser with remote default driver
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @return Browser browser
     */
    public Browser getBrowser(String hub) {
        return getBrowser(hub, getDesiredCapabilities());
    }

    /**
     * Opens browser with remote default driver and specified proxy
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     */
    public Browser getBrowser(String hub, Proxy proxy) {
        return getBrowser(hub, getDesiredCapabilities(), proxy);
    }

    /**
     * Opens browser with remote specified driver
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @return Browser browser
     */
    public Browser getBrowser(String hub, Driver driver) {
        return getBrowser(hub, getDesiredCapabilities(driver));
    }

    /**
     * Opens browser with remote specified driver and proxy
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser browser
     */
    public Browser getBrowser(String hub, Driver driver, Proxy proxy) {
        return getBrowser(hub, getDesiredCapabilities(driver), proxy);
    }

    /**
     * Opens browser with remote default driver and specified capabilities
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param capabilities map of capabilities
     * @return Browser
     */
    public Browser getBrowser(String hub, Map<String, Object> capabilities) {
        return getBrowser(hub, getDesiredCapabilities(capabilities));
    }

    /**
     * Opens browser with remote default driver and specified capabilities and
     * proxy
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param capabilities map of capabilities
     * @param proxy {@link com.qantium.uisteps.core.browser.Proxy}
     * @return Browser
     */
    public Browser getBrowser(String hub, Map<String, Object> capabilities, Proxy proxy) {
        return getBrowser(hub, getDesiredCapabilities(capabilities), proxy);
    }

    /**
     * Opens browser with remote specified driver and capabilities
     *
     * @param hub url for remote driver e.g. http://127.0.0.1:4444/wd/hub
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @param capabilities map of capabilities
     * @return Browser
     */
    public Browser getBrowser(String hub, Driver driver, Map<String, Object> capabilities) {
        return getBrowser(hub, getDesiredCapabilities(driver, capabilities));
    }

    public Browser getBrowser(String hub, Driver driver, Map<String, Object> capabilities, Proxy proxy) {
        return getBrowser(hub, getDesiredCapabilities(driver, capabilities), proxy);
    }

    private Browser getBrowser(String hub, DesiredCapabilities capabilities) {
        WebDriver driver = new RemoteDriverProvider.Default().createDriver(hub, capabilities);
        return getBrowser(driver);
    }

    private Browser getBrowser(String hub, DesiredCapabilities capabilities, Proxy proxy) {
        capabilities.setCapability(CapabilityType.PROXY, proxy.start());
        return getBrowser(hub, capabilities);
    }

    /**
     * Get DesiredCapabilities for default driver with specified capabilities
     *
     * @param capabilities map of capabilities
     * @return DesiredCapabilities
     *
     * @see org.openqa.selenium.remote.DesiredCapabilities
     */
    private DesiredCapabilities getDesiredCapabilities(Map<String, Object> capabilities) {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

        for (String capability : capabilities.keySet()) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }
        return desiredCapabilities;
    }

    /**
     * Get DesiredCapabilities for default driver
     *
     * @return DesiredCapabilities
     *
     * @see org.openqa.selenium.remote.DesiredCapabilities
     */
    private DesiredCapabilities getDesiredCapabilities() {
        Driver driver = Driver.valueOf(UIStepsProperties.getProperty(WEBDRIVER_DRIVER).toUpperCase());
        return getDesiredCapabilities(driver);
    }

    private DesiredCapabilities getDesiredCapabilities(Driver driver, Map<String, Object> capabilities) {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(driver);

        for (String capability : capabilities.keySet()) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }
        return desiredCapabilities;
    }

    /**
     * Get DesiredCapabilities for specified driver
     *
     * @param driver {@link com.qantium.uisteps.core.browser.Driver}
     * @return DesiredCapabilities
     *
     * @see org.openqa.selenium.remote.DesiredCapabilities
     */
    private DesiredCapabilities getDesiredCapabilities(Driver driver) {

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
                throw new IllegalArgumentException("Cannot get capabilities for driver " + driver + "!");
        }
    }

    /**
     * Sets default setting to driver: timeout (in milliseconds), width and
     * height
     * <p>
     * This method can be overrided if you whant to set additionsl properties
     *
     * @param driver {@link org.openqa.selenium.WebDriver}
     *
     * @see com.qantium.uisteps.core.properties.UIStepsProperties
     */
    protected void setSettingsTo(WebDriver driver) {

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
