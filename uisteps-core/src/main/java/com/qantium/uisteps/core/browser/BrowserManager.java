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
public class BrowserManager {

    private Integer currentIndex = -1;
    private List<Browser> browsers = new ArrayList();
    private BrowserFactory browserFactory = new BrowserFactory();

    private BrowserFactory getBrowserFactory() {
        return browserFactory;
    }

    protected void setBrowserFactory(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    public List<Browser> getBrowsers() {
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

    public void closeAllBrowsers() {
        for (Browser browser : getBrowsers()) {
            browser.close();
        }
        browsers = new ArrayList();
        resetCurrentIndex();
    }

    private Integer resetCurrentIndex() {
        return setCurrentIndex(-1);
    }

    public void closeCurrentBrowser() {
        Browser browser = getCurrentBrowser();
        if (browser != null) {
            browser.close();
            getBrowsers().remove(browser);
            try {
                switchToNextBrowser();
            } catch (NoBrowserException ex) {
            }
        }
    }

    public Browser getCurrentBrowser() {
        Browser browser;

        if (currentIndex > -1) {
            browser = browsers.get(currentIndex);
        } else {
            browser = null;
        }
        return browser;
    }

    public Browser openNewBrowser() {
        return open(getBrowserFactory().getBrowser());
    }

    public Browser openNewBrowser(WebDriver driver) {
        return open(getBrowserFactory().getBrowser(driver));
    }

    public Browser openNewBrowser(Driver driver) {
        DriverBuilder driverBuilder = new DriverBuilder().setDriver(driver);
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    public Browser openNewBrowser(String driver) {
        DriverBuilder driverBuilder = new DriverBuilder().setDriver(driver);
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    public Browser openNewBrowser(DriverBuilder driverBuilder) {
        return open(getBrowserFactory().getBrowser(driverBuilder));
    }

    public Browser open(Browser browser) {
        getBrowsers().add(browser);
        setCurrentIndex(getBrowsers().size() - 1);
        return getCurrentBrowser();
    }

    public Browser switchToNextBrowser() {

        if (hasNext()) {
            return switchToBrowserByIndex(incrementCurrentIndex());
        } else if (hasAny()) {
            return switchToFirstBrowser();
        } else {
            return null;
        }
    }

    public Browser switchToPreviousBrowser() {

        if (hasPrevious()) {
            return switchToBrowserByIndex(decrementCurrentIndex());
        } else if (hasAny()) {
            return switchToLastBrowser();
        } else {
            resetCurrentIndex();
            return null;
        }
    }

    public Browser switchToFirstBrowser() {
        return switchToBrowserByIndex(0);
    }

    public Browser switchToLastBrowser() {
        return switchToBrowserByIndex(getBrowsers().size() - 1);
    }

    public Browser switchToBrowserByIndex(int index) {
        if (index < 0) {
            showNoBrowserException("Index of browser must not be negative! Index: " + index);
        }

        if (index >= getBrowsers().size()) {
            showNoBrowserException("Index of browser is out of bounds! Index: " + index);
        }

        if (!hasAny()) {
            showNoBrowserException("The list of browsers is empty!");
        }
        setCurrentIndex(index);
        return getCurrentBrowser();
    }

    private void showNoBrowserException(String message) {
        setCurrentIndex(-1);
        throw new NoBrowserException(message);
    }

    public boolean hasNext() {
        return !getBrowsers().isEmpty() && getCurrentIndex() < getBrowsers().size() - 1;
    }

    public boolean hasPrevious() {
        return !getBrowsers().isEmpty() && getCurrentIndex() > 0;
    }

    public boolean hasAny() {
        return !getBrowsers().isEmpty() && !getBrowsers().isEmpty();
    }
}
