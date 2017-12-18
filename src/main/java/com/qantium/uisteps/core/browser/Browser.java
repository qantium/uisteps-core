/*
 * Copyright 2014 ASolyankin.
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


import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiRequest;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.screenshots.IPhotographer;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.then.Then;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.net.MalformedURLException;
import java.net.URL;

import static com.qantium.uisteps.core.properties.UIStepsProperty.SOURCE_TAKE_FAKE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Anton Solyankin
 */
public class Browser implements IBrowser {

    private WebDriver driver;
    private String name;
    private IWindowManager windowManager;
    private UrlFactory urlFactory = new UrlFactory();
    private IUIObjectFactory uiObjectFactory = new UIObjectFactory(this);
    private BrowserMobProxyServer proxy;
    private String hub;
    private Photographer photographer;

    public Browser() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> close()));
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager = new WindowManager(driver);
        photographer = new Photographer(driver);
    }

    private IWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public URL getNodeUrl() {

        RestApiRequest request = null;

        try {
            URL hub = getHubUrl();
            RestApi client = new RestApi(hub.toString());
            RemoteWebDriver driver = (RemoteWebDriver) getDriver();
            SessionId session = driver.getSessionId();
            request = client.createRequest("/grid/api/testsession?session=" + session);
            JSONObject response = client.createRequest("/grid/api/testsession?session=" + session).get().toJSONObject();

            return new URL(response.getString("proxyId"));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot get node url by request " + request, ex);
        }
    }

    @Override
    public URL getHubUrl() {
        String hub = getHub();
        if (isEmpty(hub)) {
            throw new IllegalStateException("A hub is not set for browser " + this);
        } else {
            try {
                return new URL(hub.replace("/wd/hub", ""));
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Cannot get remote url for browser " + this);
            }
        }
    }


    @Override
    public WebDriver getDriver() {
        if (driver == null) {
            throw new WebDriverException("WebDriver is not set in browser " + this + "!");
        }
        return driver;
    }

    @Override
    public BrowserMobProxyServer getProxy() {
        return proxy;
    }

    @Override
    public void setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public String getHub() {
        return hub;
    }

    @Override
    public void setHub(String hub) {
        this.hub = hub;
    }

    @Override
    public void close() {

        if (proxy != null && !proxy.isStopped()) {
            proxy.stop();
        }

        if (driver != null) {
            driver.quit();
        }
    }

    //Open
    @Override
    public Page openUrl(String url) {
        return openUrl(url, null);
    }

    @Override
    public Page openUrl(String url, String[] params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot open url " + url, ex);
        }
    }

    @Override
    public <T extends Page> T open(Class<T> page, Url url, String[] params) {
        T pageInstance = uiObjectFactory.get(page);
        Url pageUrl;

        if (url != null) {
            pageUrl = url;
        } else {
            pageUrl = urlFactory.getUrlOf(pageInstance);
        }
        pageInstance.withUrl(pageUrl);

        if (ArrayUtils.isNotEmpty(params)) {
            pageUrl = urlFactory.getUrlOf(pageInstance, params);
            pageInstance.withUrl(pageUrl);
        }

        return open(pageInstance);
    }

    //Window
    @Override
    public void openNewWindow() {
        getWindowManager().openNewWindow();
    }

    @Override
    public void closeWindow() {
        if (getWindowManager().getCountOfWindows() < 2) {
            close();
        } else {
            getWindowManager().closeWindow();
        }
    }

    @Override
    public void switchToNextWindow() {
        getWindowManager().switchToNextWindow();
    }

    @Override
    public void switchToPreviousWindow() {
        getWindowManager().switchToPreviousWindow();
    }

    @Override
    public void switchToDefaultWindow() {
        getWindowManager().switchToDefaultWindow();
    }

    @Override
    public void switchToWindowByIndex(int index) {
        getWindowManager().switchToWindowByIndex(index);
    }

    @Override
    public boolean hasNextWindow() {
        return getWindowManager().hasNextWindow();
    }

    @Override
    public boolean hasPreviousWindow() {
        return getWindowManager().hasPreviousWindow();
    }

    @Override
    public int getCountOfWindows() {
        return getWindowManager().getCountOfWindows();
    }

    @Override
    public int getCurrentWindowIndex() {
        return getWindowManager().getCurrentWindowIndex();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder browserName = new StringBuilder();

        String os = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        Dimension size = getWindowSize();

        browserName.append(NameConverter.humanize(getDriver().getClass()).replace(" driver", ""))
                .append(" os.name: ").append(os)
                .append(" os.arch: ").append(osArch)
                .append(" os.version: ").append(osVersion)
                .append(" width: ").append(size.width)
                .append(" height: ").append(size.height);

        if (!StringUtils.isEmpty(name)) {
            browserName.append(" name: ").append(name);
        }

        return browserName.toString();
    }

    //Screenshots
    @Override
    public IPhotographer ignore(By... locators) {
        return photographer.ignore(locators);
    }

    @Override
    public IPhotographer ignore(UIElement... elements) {
        return photographer.ignore(elements);
    }

    @Override
    public IPhotographer ignore(Coords... areas) {
        return photographer.ignore(areas);
    }

    @Override
    public IPhotographer ignore(int width, int height) {
        return photographer.ignore(width, height);
    }

    @Override
    public IPhotographer ignore(int x, int y, int width, int height) {
        return photographer.ignore(x, y, width, height);
    }

    @Override
    public Screenshot takeScreenshot() {
        return photographer.takeScreenshot();
    }

    @Override
    public Screenshot takeScreenshot(UIElement... elements) {
        return photographer.takeScreenshot(elements);
    }

    @Override
    public Screenshot takeScreenshot(Ignored... elements) {
        return photographer.takeScreenshot(elements);
    }

    //Page source
    public String getPageSource() {
        try {
            return getDriver().getPageSource();
        } catch (Exception ex) {

            if (SOURCE_TAKE_FAKE.isTrue()) {
                return "CANNOT TAKE PAGE SOURCE!";
            } else {
                throw ex;
            }
        }
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By... locator) {
        return uiObjectFactory.getAll(uiObject, locator);
    }

    @Override
    public UIElement get(By... locator) {
        return uiObjectFactory.get(locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return uiObjectFactory.get(uiObject);
    }


    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By... locator) {
        return uiObjectFactory.get(uiObject, locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By... locators) {
        return uiObjectFactory.get(uiObject, context, locators);
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return new Then(uiObject, this);
    }
}