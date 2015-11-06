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
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author ASolyankin
 */
public class BrowserManager {

    private final static ThreadLocal<Integer> currentIndex = new ThreadLocal();
    private final static ThreadLocal<ArrayList<Browser>> browsers = new ThreadLocal();
    private final BrowserFactory browserFactory;
    private final static ThreadLocal<Browser> currentBrowser = new ThreadLocal();

    public BrowserManager(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
        init();
    }

    public static ArrayList<Browser> getBrowsers() {

        if (browsers.get() == null) {
            init();
        }
        return browsers.get();
    }

    private static Integer decrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() - 1);
    }

    private static Integer incrementCurrentIndex() {
        return setCurrentIndex(getCurrentIndex() + 1);
    }

    private static Integer getCurrentIndex() {
        return currentIndex.get();
    }

    private static Integer setCurrentIndex(int index) {
        currentIndex.set(index);

        if (index > -1) {
            currentBrowser.set(getBrowsers().get(index));
        } else {
            currentBrowser.set(null);
        }

        return getCurrentIndex();
    }

    public static void closeAllBrowsers() {

        for (int i = 0; i < getBrowsers().size(); i++) {

            try {
                setCurrentIndex(i);
                closeCurrentBrowser();
            } catch (NoBrowserException ex) {
            }
        }

        init();
    }

    public static void closeCurrentBrowser() {
        Browser browser = getCurrentBrowser();
        browser.getDriver().quit();
        getBrowsers().remove(browser);
        switchToNextBrowser();
    }

    private static void init() {
        browsers.set(new ArrayList());
        setCurrentIndex(-1);
    }

    public static Browser getCurrentBrowser() {
        return currentBrowser.get();
    }

    public Browser openNewBrowser(WebDriver withDriver) {
        return add(browserFactory.getBrowser(withDriver));
    }

    public Browser openNewBrowser(String withDriver) {
        return add(browserFactory.getBrowser(withDriver));
    }

    public Browser openNewBrowser() {
        return add(browserFactory.getBrowser());
    }

    public Browser openNewBrowser(Capabilities capabilities) {
        return add(browserFactory.getBrowser(capabilities));
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return browserFactory.getDesiredCapabilities();
    }

    public DesiredCapabilities getDesiredCapabilities(String withDriver) {
        return browserFactory.getDesiredCapabilities(withDriver);
    }

    public static Browser switchToNextBrowser() {

        if (hasNext()) {
            return switchToBrowserByIndex(incrementCurrentIndex());
        } else {
            return switchToFirstBrowser();
        }
    }

    public static Browser switchToPreviousBrowser() {

        if (hasPrevious()) {
            return switchToBrowserByIndex(decrementCurrentIndex());
        } else {
            return switchToLastBrowser();
        }
    }

    public static Browser switchToFirstBrowser() {
        return switchToBrowserByIndex(0);
    }

    public static Browser switchToLastBrowser() {
        return switchToBrowserByIndex(getBrowsers().size() - 1);
    }

    public static Browser switchToBrowserByIndex(int index) {

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

    private static void showNoBrowserException(String message) {
        setCurrentIndex(-1);
        throw new NoBrowserException(message);
    }

    public Browser add(Browser browser) {
        getBrowsers().add(browser);
        return switchToLastBrowser();
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

    public static boolean hasNext() {
        return getCurrentIndex() < getBrowsers().size() - 1;
    }

    public static boolean hasPrevious() {
        return getCurrentIndex() > 0;
    }

    public static boolean hasAny() {
        return !getBrowsers().isEmpty();
    }

}
