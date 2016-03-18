package com.qantium.uisteps.core.browser.factory;

import com.qantium.uisteps.core.browser.Proxy;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.uiautomation.ios.IOSCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * Created by Anton Solyankin
 */
public class DriverBuilder {

    private Driver driver;
    private Map<String, Object> capabilities = new HashMap();
    private String hub;
    private Proxy proxy;

    public DriverBuilder() {
        setDriver(getProperty(WEBDRIVER_DRIVER));
        setProxy(getProperty(WEBDRIVER_PROXY));
        setHub(getProperty(WEBDRIVER_REMOTE_URL));

        capabilities.put(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, getProperty(WEBDRIVER_UNEXPECTED_ALERT_BEHAVIOUR));
    }

    public DriverBuilder setHub(String hub) {
        this.hub = hub;
        return this;
    }

    public DriverBuilder setDriver(String driver) {
        return setDriver(Driver.valueOf(driver.toUpperCase()));
    }

    public DriverBuilder setDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public DriverBuilder setCapabilities(Map<String, Object> capabilities) {
        this.capabilities.putAll(capabilities);
        return this;
    }

    public Driver getDriver() {
        return driver;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public String getHub() {
        return hub;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public WebDriver getWebDriver() {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

        for (String capability : new HashSet<>(capabilities.keySet())) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }

        if (proxy != null) {
            desiredCapabilities.setCapability(CapabilityType.PROXY, proxy.start());
        }

        WebDriver driver;

        if (StringUtils.isEmpty(hub)) {
            driver = getWebDriver(desiredCapabilities);
        } else {
            driver = getRemoteDriver(desiredCapabilities);
        }

        setSettings(driver);
        return driver;
    }

    public DriverBuilder setProxy(String proxy) {

        if (!StringUtils.isEmpty(proxy)) {

            String[] address = proxy.split(":");

            String ip = null;
            Integer port = null;

            if (address.length > 0 && !StringUtils.isEmpty(address[0])) {
                ip = address[0];
            }

            if (address.length > 1) {
                port = Integer.parseInt(address[1]);
            }
            setProxy(new Proxy.ProxyBuilder().setIp(ip).setPort(port).build());
        }
        return this;
    }

    public DriverBuilder setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    private DesiredCapabilities getDesiredCapabilities() {

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
                return IOSCapabilities.iphone();
            case IPAD:
                return IOSCapabilities.ipad();
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

    private WebDriver getWebDriver(DesiredCapabilities capabilities) {

        switch (driver) {
            case FIREFOX:
                return new FirefoxDriver(capabilities);
            case CHROME:
                return new ChromeDriver(capabilities);
            case OPERA:
                return new OperaDriver(capabilities);
            case IEXPLORER:
                return new InternetExplorerDriver(capabilities);
            case EDGE:
                return new EdgeDriver(capabilities);
            case SAFARI:
                return new SafariDriver(capabilities);
            case ANDROID:
                remoteDriverUrlError(driver, capabilities);
            case IPHONE:
                remoteDriverUrlError(driver, capabilities);
            case IPAD:
                remoteDriverUrlError(driver, capabilities);
            case HTMLUNIT:
                return new HtmlUnitDriver(capabilities);
            case HTMLUNITWITHJS:
                remoteDriverUrlError(driver, capabilities);
            case PHANTOMJS:
                return new PhantomJSDriver(capabilities);
            default:
                throw new IllegalArgumentException("Cannot get driver " + driver + " with capabilities " + capabilities);
        }
    }

    private WebDriver getRemoteDriver(DesiredCapabilities capabilities) {
        try {
            return new RemoteWebDriver(new URL(hub), capabilities);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Could not connect to WebDriver hub", ex);
        }
    }

    private void remoteDriverUrlError(Driver driver, DesiredCapabilities capabilities) {
        throw new IllegalArgumentException("It is necessary to set url for RemoteDriver to get " + driver + " driver with capabilities " + capabilities);
    }

    private void setSettings(WebDriver driver) {
        WebDriver.Options manage = driver.manage();
        long timeout = Long.parseLong(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        manage.timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);

        String widthProperty = getProperty(BROWSER_WIDTH).toLowerCase().trim();
        String heightProperty = getProperty(BROWSER_HEIGHT).toLowerCase().trim();
        WebDriver.Window window = manage.window();

        if ("max".equals(widthProperty) || "max".equals(heightProperty)) {
            window.maximize();
        }

        Dimension size = window.getSize();
        int width = getSizeFrom(widthProperty, size.width);
        int height = getSizeFrom(heightProperty, size.height);

        if (width != size.width || height != size.height) {
            Dimension dimension = new Dimension(width, height);
            manage.window().setSize(dimension);
        }
    }

    private int getSizeFrom(String property, int defaultSize) {

        if (!StringUtils.isEmpty(property) && !"max".equals(property)) {
            return Integer.parseInt(property);
        } else {
            return defaultSize;
        }
    }
}
