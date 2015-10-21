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

/**
 *
 * @author ASolyankin
 */
public class BrowserManager {

    private Integer currentIndex;
    private ArrayList<Browser> browsers;
    private final BrowserFactory browserFactory;
    private final static ThreadLocal<Browser> currentBrowser = new ThreadLocal();

    public BrowserManager(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
        init();
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
        
        if(index > -1) {
            currentBrowser.set(browsers.get(index));
        } else {
            currentBrowser.set(null);
        }
        
        return getCurrentIndex();
    }

    public void closeAllBrowsers() {

        for (Browser browser : getBrowsers()) {
            browser.getDriver().quit();
        }
        init();
    }

    private void init() {
        browsers = new ArrayList();
        setCurrentIndex(-1);
    }

    public static Browser getCurrentBrowser() {
        return currentBrowser.get();
    }

    public Browser openNewBrowser(String withDriver) {
        return add(browserFactory.getBrowser(withDriver));
    }

    public Browser openNewBrowser() {
        return add(browserFactory.getBrowser());
    }

    public Browser switchToNextBrowser() {

        if (this.hasNext()) {
            return switchToBrowserByIndex(incrementCurrentIndex());
        } else {
            return switchToFirstBrowser();
        }
    }

    public Browser switchToPreviousBrowser() {

        if (this.hasPrevious()) {
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
            throw new NoBrowserException("List of browsers is empty!");
        }

        if (index < 0) {
            throw new NoBrowserException("Index of browser must not be negative! Index: " + index);
        }

        if (index >= getBrowsers().size()) {
            throw new NoBrowserException("Index of browser is out of bounds! Index: " + index);
        }
        setCurrentIndex(index);
        return getCurrentBrowser();
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
