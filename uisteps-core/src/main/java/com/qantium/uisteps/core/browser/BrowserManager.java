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

import java.util.ArrayList;
import java.util.Map;

import org.openqa.selenium.WebDriver;

/**
 * @author Anton Solyankin
 */
public class BrowserManager {

    private Integer currentIndex = -1;
    private ArrayList<Browser> browsers = new ArrayList();
    private BrowserFactory browserFactory = new BrowserFactory();
    private Browser currentBrowser;

    public BrowserManager() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                reset();
            }
        });
    }

    public BrowserFactory getBrowserFactory() {
        return browserFactory;
    }

    public void setBrowserFactory(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    public ArrayList<Browser> getBrowsers() {
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

        if (index > -1) {
            currentBrowser = getBrowsers().get(index);
        } else {
            currentBrowser = null;
        }

        return getCurrentIndex();
    }

    public void closeAllBrowsers() {
        reset();
    }

    public void closeCurrentBrowser() {
        Browser browser = getCurrentBrowser();
        browser.close();
        getBrowsers().remove(browser);
        switchToNextBrowser();
    }

    public void reset() {

        if (browsers != null) {
            for (Browser browser : browsers) {
                browser.close();
            }
        }

        browsers = new ArrayList();
        currentIndex = -1;
    }

    public Browser getCurrentBrowser() {
        return browsers.get(currentIndex);
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
        } else {
            return switchToFirstBrowser();
        }
    }

    public Browser switchToPreviousBrowser() {

        if (hasPrevious()) {
            return switchToBrowserByIndex(decrementCurrentIndex());
        } else {
            return switchToLastBrowser();
        }
    }

    public Browser switchToFirstBrowser() {
        return switchToBrowserByIndex(0);
    }

    public Browser switchToLastBrowser() {
        return switchToBrowserByIndex(getBrowsers().size() - 1);
    }

    public Browser switchToBrowserByIndex(int index) {

        if (!hasAny()) {
            showNoBrowserException("List of browsers is empty!");
        }

        if (index < 0) {
            showNoBrowserException("Index of browser must not be negative! Index: " + index);
        }

        if (index >= getBrowsers().size()) {
            showNoBrowserException("Index of browser is out of bounds! Index: " + index);
        }
        setCurrentIndex(index);
        return getCurrentBrowser();
    }

    private void showNoBrowserException(String message) {
        setCurrentIndex(-1);
        throw new NoBrowserException(message);
    }

    public boolean hasNext() {
        return getCurrentIndex() < getBrowsers().size() - 1;
    }

    public boolean hasPrevious() {
        return getCurrentIndex() > 0;
    }

    public boolean hasAny() {
        return !getBrowsers().isEmpty();
    }
}
