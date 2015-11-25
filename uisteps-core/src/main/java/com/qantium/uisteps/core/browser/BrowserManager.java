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
 * @author ASolyankin
 */
public class BrowserManager {

    private final static ThreadLocal<Integer> currentIndex = new ThreadLocal();
    private final static ThreadLocal<ArrayList<Browser>> browsers = new ThreadLocal();
    private BrowserFactory browserFactory = new BrowserFactory();
    private final static ThreadLocal<Browser> currentBrowser = new ThreadLocal();

    public BrowserFactory getBrowserFactory() {
        return browserFactory;
    }

    public void setBrowserFactory(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    public ArrayList<Browser> getBrowsers() {

        if (browsers.get() == null) {
            reset();
        }
        return browsers.get();
    }

    private Integer decrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() - 1);
    }

    private Integer incrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() + 1);
    }

    private Integer getCurrentIndex() {
        return currentIndex.get();
    }

    private Integer setCurrentIndex(int index) {
        currentIndex.set(index);

        if (index > -1) {
            currentBrowser.set(getBrowsers().get(index));
        } else {
            currentBrowser.set(null);
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

    public static void reset() {

        if (browsers.get() != null) {

            for (Browser browser : browsers.get()) {
                browser.getDriver().quit();
            }
        }

        browsers.set(new ArrayList());
        currentIndex.set(-1);
    }

    public Browser getCurrentBrowser() {
        return currentBrowser.get();
    }

    public Browser openNewBrowser() {
        return open(getBrowserFactory().getBrowser());
    }

    public Browser openNewBrowser(Map<String, Object> capabilities) {
        return open(getBrowserFactory().getBrowser(capabilities));
    }

    public Browser openNewBrowser(WebDriver driver) {
        return open(getBrowserFactory().getBrowser(driver));
    }

    public Browser openNewBrowser(Driver driver) {
        return open(getBrowserFactory().getBrowser(driver));
    }

    public Browser openNewBrowser(Driver driver, Map<String, Object> capabilities) {
        return open(getBrowserFactory().getBrowser(driver, capabilities));
    }

    public Browser openNewBrowser(String hub) {
        return open(getBrowserFactory().getBrowser(hub));
    }

    public Browser openNewBrowser(String hub, Map<String, Object> capabilities) {
        return open(getBrowserFactory().getBrowser(hub, capabilities));
    }

    public Browser openNewBrowser(String hub, Driver driver) {
        return open(getBrowserFactory().getBrowser(hub, driver));
    }

    public Browser openNewBrowser(String hub, Driver driver, Map<String, Object> capabilities) {
        return open(getBrowserFactory().getBrowser(hub, driver, capabilities));
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

    public void removeByIndex(int index) throws NoBrowserException {

        try {
            getBrowsers().remove(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoBrowserException("Cannot remove browser by index " + index);
        }

        switchToNextBrowser();
    }

    public void remove(Browser browser) {
        removeByIndex(getBrowsers().indexOf(browser));
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
