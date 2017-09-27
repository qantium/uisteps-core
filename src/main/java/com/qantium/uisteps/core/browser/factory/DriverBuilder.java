package com.qantium.uisteps.core.browser.factory;

import com.opera.core.systems.OperaDesktopDriver;
import com.qantium.uisteps.core.browser.Proxy;
import com.qantium.uisteps.core.lifecycle.Execute;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.uiautomation.ios.IOSCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Anton Solyankin
 */
public class DriverBuilder {

    private Driver driver;
    private Map<String, Object> capabilities = newHashMap();
    private String hub;
    private Proxy proxy;
    private int width;
    private int height;
    private boolean maxWidth;
    private boolean maxHeight;

    public DriverBuilder() {
        setDriver(WEBDRIVER_DRIVER.getValue());
        setProxy(WEBDRIVER_PROXY.getValue());
        setHub(WEBDRIVER_REMOTE_URL.getValue());

        width = initWidth();
        height = initHeight();

        capabilities.put(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, WEBDRIVER_UNEXPECTED_ALERT_BEHAVIOUR.getValue());
    }

    private int initWidth() {
        return initSize(BROWSER_WIDTH.getValue(), true);
    }

    private int initHeight() {
        return initSize(BROWSER_HEIGHT.getValue(), false);
    }

    private int initSize(String property, boolean width) {
        if ("max".equals(property.toLowerCase())) {
            if (width) {
                setMaxWidth();
            } else {
                setMaxHeight();
            }
            return -1;
        } else {
            try {
                return Integer.parseInt(property);
            } catch (NumberFormatException ex) {
                return -1;
            }
        }
    }

    public boolean isMaxWidth() {
        return maxWidth;
    }

    public DriverBuilder setMaxWidth() {
        maxWidth = true;
        return this;
    }

    public boolean isMaxHeight() {
        return maxHeight;
    }

    public DriverBuilder setMaxHeight() {
        maxHeight = true;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public DriverBuilder setWidth(int width) {
        this.width = width;
        maxWidth = false;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public DriverBuilder setHeight(int height) {
        this.height = height;
        maxHeight = false;
        return this;
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

        for (String capability : capabilities.keySet()) {
            desiredCapabilities.setCapability(capability, capabilities.get(capability));
        }

        if (proxy != null) {
            desiredCapabilities.setCapability(CapabilityType.PROXY, proxy.start());
        }

        WebDriver driver;

        if (isEmpty(hub)) {
            driver = getWebDriver(desiredCapabilities);
        } else {
            driver = getRemoteDriver(desiredCapabilities);
        }

        setSize(driver);
        return driver;
    }

    public DriverBuilder setProxy(String proxy) {

        if (!Execute.NONE.name().equals(HAR_TAKE.getValue()) && isNotEmpty(proxy)) {

            String[] address = proxy.split(":");

            String ip = null;
            Integer port = null;

            if (address.length > 0 && isNotEmpty(address[0])) {
                ip = address[0];
            }

            if (address.length > 1) {

                if(isEmpty(address[1])) {
                    port = null;
                } else {
                    port = Integer.parseInt(address[1]);
                }
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
                DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                return capabilities;
            case EDGE:
                return DesiredCapabilities.internetExplorer();
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
                throw new IllegalArgumentException("Cannot get capabilities for driver " + driver + "!");
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
                return new OperaDesktopDriver(capabilities);
            case IEXPLORER:
                return new InternetExplorerDriver(capabilities);
            case EDGE:
                return new InternetExplorerDriver(capabilities);
            case SAFARI:
                return new SafariDriver(capabilities);
            case ANDROID:
                remoteDriverUrlError(driver, capabilities);
            case IPHONE:
                remoteDriverUrlError(driver, capabilities);
            case IPAD:
                remoteDriverUrlError(driver, capabilities);
            case HTMLUNIT:
                remoteDriverUrlError(driver, capabilities);
            case HTMLUNITWITHJS:
                remoteDriverUrlError(driver, capabilities);
            case PHANTOMJS:
                throw new IllegalArgumentException("Cannot get driver " + driver + " with capabilities " + capabilities);
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

    private void setSize(WebDriver driver) {
        WebDriver.Options manage = driver.manage();
        manage.timeouts().implicitlyWait(0, MILLISECONDS);

        WebDriver.Window window = manage.window();

        if (isMaxWidth() || isMaxWidth()) {
            window.maximize();
        }

        Dimension defaultSize = window.getSize();
        int width = getWidth(defaultSize);
        int height = getHeight(defaultSize);

        if (width != defaultSize.width || height != defaultSize.height) {
            Dimension dimension = new Dimension(width, height);
            manage.window().setSize(dimension);
        }
    }

    private int getWidth(Dimension defaultSize) {
        return getSize(isMaxWidth(), getWidth(), defaultSize.width);
    }

    private int getHeight(Dimension defaultSize) {
        return getSize(isMaxWidth(), getHeight(), defaultSize.height);
    }

    private int getSize(boolean isMax, int size, int defaultSize) {

        if (size > 0 && !isMax) {
            return size;
        } else {
            return defaultSize;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriverBuilder that = (DriverBuilder) o;

        if (getDriver() != that.getDriver()) return false;
        if (!getCapabilities().equals(that.getCapabilities())) return false;
        if (!getHub().equals(that.getHub())) return false;
        return getProxy().equals(that.getProxy());

    }

    @Override
    public int hashCode() {
        int result = getDriver().hashCode();
        result = 31 * result + getCapabilities().hashCode();
        result = 31 * result + getHub().hashCode();
        result = 31 * result + getProxy().hashCode();
        return result;
    }
}
