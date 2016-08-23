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

import com.qantium.uisteps.core.browser.factory.BrowserFactory;
import com.qantium.uisteps.core.browser.factory.Driver;
import com.qantium.uisteps.core.browser.factory.DriverBuilder;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Solyankin
 */
public class BrowserManager implements IBrowserManager {

    private Integer currentIndex = -1;
    private List<IBrowser> browsers = new ArrayList();
    private BrowserFactory browserFactory = new BrowserFactory();

    private BrowserFactory getBrowserFactory() {
        return browserFactory;
    }

    protected void setBrowserFactory(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    public List<IBrowser> getBrowsers() {
        return browsers;
    }

    private Integer decrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() - 1);
    }

    private Integer incrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() + 1);
    }

    private Integer getCurrentIndex() {
        return currentIndex;
    }

    private Integer setCurrentIndex(int index) {
        currentIndex = index;
        return currentIndex;
    }

    private Integer resetCurrentIndex() {
        return setCurrentIndex(-1);
    }

    @Override
    public void closeAllBrowsers() {
        for (IBrowser browser : getBrowsers()) {
            browser.close();
        }
        browsers = new ArrayList();
        resetCurrentIndex();
    }

    @Override
    public void closeCurrentBrowser() {
        IBrowser browser = getCurrentBrowser();
        if (browser != null) {
            browser.close();
            getBrowsers().remove(browser);
            try {
                switchToNextBrowser();
            } catch (NoBrowserException ex) {
            }
        }
    }

    @Override
    public IBrowser getCurrentBrowser() {
        IBrowser browser;

        if (currentIndex > -1) {
            browser = browsers.get(currentIndex);
        } else {
            browser = null;
        }
        return browser;
    }

    @Override
    public IBrowser openNewBrowser() {
        return open(getBrowserFactory().getBrowser());
    }

    @Override
    public IBrowser openNewBrowser(WebDriver driver) {
        return open(getBrowserFactory().getBrowser(driver));
    }

    @Override
    public IBrowser openNewBrowser(Driver driver) {
        DriverBuilder driverBuilder = new DriverBuilder().setDriver(driver);
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    @Override
    public IBrowser openNewBrowser(String driver) {
        DriverBuilder driverBuilder = new DriverBuilder().setDriver(driver);
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    @Override
    public IBrowser openNewBrowser(DriverBuilder driverBuilder) {
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    @Override
    public IBrowser open(IBrowser browser) {
        getBrowsers().add(browser);
        setCurrentIndex(getBrowsers().size() - 1);
        return getCurrentBrowser();
    }

    @Override
    public IBrowser switchToNextBrowser() {

        if (hasNextBrowser()) {
            return switchToBrowserByIndex(incrementCurrentIndex());
        } else if (hasAnyBrowser()) {
            return switchToFirstBrowser();
        } else {
            return null;
        }
    }

    @Override
    public IBrowser switchToPreviousBrowser() {

        if (hasPreviousBrowser()) {
            return switchToBrowserByIndex(decrementCurrentIndex());
        } else if (hasAnyBrowser()) {
            return switchToLastBrowser();
        } else {
            resetCurrentIndex();
            return null;
        }
    }

    @Override
    public IBrowser switchToFirstBrowser() {
        return switchToBrowserByIndex(0);
    }

    @Override
    public IBrowser switchToLastBrowser() {
        return switchToBrowserByIndex(getBrowsers().size() - 1);
    }

    @Override
    public IBrowser switchToBrowserByIndex(int index) {
        if (index < 0) {
            showNoBrowserException("Index of browser must not be negative! Index: " + index);
        }

        if (index >= getBrowsers().size()) {
            showNoBrowserException("Index of browser is out of bounds! Index: " + index);
        }

        if (!hasAnyBrowser()) {
            showNoBrowserException("The list of browsers is empty!");
        }
        setCurrentIndex(index);
        return getCurrentBrowser();
    }

    private void showNoBrowserException(String message) {
        setCurrentIndex(-1);
        throw new NoBrowserException(message);
    }

    @Override
    public boolean hasNextBrowser() {
        return !getBrowsers().isEmpty() && getCurrentIndex() < getBrowsers().size() - 1;
    }

    @Override
    public boolean hasPreviousBrowser() {
        return !getBrowsers().isEmpty() && getCurrentIndex() > 0;
    }

    @Override
    public boolean hasAnyBrowser() {
        return !getBrowsers().isEmpty() && !getBrowsers().isEmpty();
    }
}
