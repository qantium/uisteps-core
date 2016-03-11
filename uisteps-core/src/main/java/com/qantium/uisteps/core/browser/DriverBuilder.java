package com.qantium.uisteps.core.browser;

import org.apache.commons.lang3.StringUtils;
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

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import static com.qantium.uisteps.core.properties.UIStepsProperties.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Anton Solyankin
 */
public class DriverBuilder {

    private Driver driver;
    private Map<String, Object> capabilities;
    private String hub;
    private Proxy proxy;

    public DriverBuilder() {
        setDriver(getProperty(WEBDRIVER_DRIVER));
        setProxy(getProperty(WEBDRIVER_PROXY));
        setHub(getProperty(WEBDRIVER_REMOTE_URL));
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
        this.capabilities = capabilities;
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

        if (StringUtils.isEmpty(hub)) {
            return getRemoteDriver(desiredCapabilities);
        } else {
            return getWebDriver(desiredCapabilities);
        }
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
}
