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
package com.qantium.uisteps.core.user.browser;

import java.util.ArrayList;

/**
 *
 * @author ASolyankin
 */
public class BrowserList {

    private int currentIndex;
    private final ArrayList<Browser> browsers = new ArrayList();

    public Browser getCurrentBrowser() {
        return browsers.get(currentIndex);
    }
    
    public Browser switchToNextBrowser() throws BrowserListEmptyException {

        if (this.hasNext()) {
            return switchToBrowserByIndex(++currentIndex);
        } else {
            return switchToFirstBrowser();
        }
    }

    public Browser switchToPreviousBrowser() {

        if (this.hasPrevious()) {
            return switchToBrowserByIndex(--currentIndex);
        } else {
            return switchToLastBrowser();
        }
    }

    public Browser switchToFirstBrowser() {
        return switchToBrowserByIndex(0);
    }
    
    public Browser switchToLastBrowser() {
        return switchToBrowserByIndex(this.size() - 1);
    }

    public Browser switchToBrowserByIndex(int index) throws BrowserListEmptyException, NoBrowserException {

        if (this.isEmpty()) {
            throw new BrowserListEmptyException();
        }

        if (index < 0) {
            throw new NoBrowserException("Index of browser must be positive! Index: " + index);
        }

        if (index >= browsers.size()) {
            throw new NoBrowserException("Index is out of bounds! Index: " + index);
        }

        currentIndex = index;
        return getCurrentBrowser();
    }

    public int size() {
        return browsers.size();
    }

    public Browser add(Browser browser) {
        browsers.add(browser);
        return switchToLastBrowser();
    }

    public void removeByIndex(int index) throws NoBrowserException {

        try {
            browsers.remove(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoBrowserException("Cannot remove browser by index " + index);
        }

        switchToNextBrowser();
    }

    public void remove(Browser browser) {
        removeByIndex(browsers.indexOf(browser));
    }

    public boolean hasNext() {
        return currentIndex < browsers.size() - 1;
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public boolean isEmpty() {
        return browsers.isEmpty();
    }

}
