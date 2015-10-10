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
package com.qantium.uisteps.core.user;

import com.qantium.uisteps.core.Named;
import com.qantium.uisteps.core.then.Then;
import com.qantium.uisteps.core.browser.BrowserList;
import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.BrowserFactory;
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class User implements Named {

    private final BrowserList browserList = new BrowserList();
    private final BrowserFactory browserFactory;
    public static final String DEFAULT_NAME = "user";
    private String name;

    public User(BrowserFactory browserFactory, String name) {
        this.browserFactory = browserFactory;
        this.name = name;
    }

    public User(BrowserFactory browserFactory) {
        this(browserFactory, DEFAULT_NAME);
    }

    public Browser inOpenedBrowser() {
        if (browserList.isEmpty()) {
            openNewBrowser();
        }
        return browserList.getCurrentBrowser();
    }

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    public Browser openNewBrowser(String withDriver) {
        return in(browserFactory.getBrowser(withDriver));
    }

    public Browser openNewBrowser() {
        return in(browserFactory.getBrowser());
    }

    public Browser in(Browser browser) {
        return browserList.add(browser);
    }

    public Browser switchToNextBrowser() {
        return browserList.switchToNextBrowser();
    }

    public Browser switchToPreviousBrowser() {
        return browserList.switchToPreviousBrowser();
    }

    public Browser switchToDefaultBrowser() {
        return browserList.switchToFirstBrowser();
    }

    public Browser switchToBrowserByIndex(int index) throws NoBrowserException {
        return browserList.switchToBrowserByIndex(index);
    }

    public Browser switchToLastBrowser() {
        return browserList.switchToLastBrowser();
    }

    public int count() {
        return browserList.size();
    }

    public void openUrl(String url, String... params) {
        inOpenedBrowser().openUrl(url, params);
    }

    public void open(Url url, String... params) {
        inOpenedBrowser().open(url, params);
    }

    public <T extends Page> T open(Class<T> page, Url url, String... params) {
        return inOpenedBrowser().open(page, url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        return inOpenedBrowser().open(page, url, params);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return inOpenedBrowser().open(page, params);
    }

    public <T extends Page> T open(T page, String... params) {
        return inOpenedBrowser().open(page, params);
    }

    public void openNewWindow() {
        inOpenedBrowser().openNewWindow();
    }

    public void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }

    public void refreshCurrentPage() {
        inOpenedBrowser().refreshCurrentPage();
    }

    public void deleteCookies() {
        inOpenedBrowser().deleteCookies();
    }

    public void click(WrapsElement element) {
        inOpenedBrowser().click(element);
    }

    public void clickOnPoint(WrapsElement element, int x, int y) {
        inOpenedBrowser().clickOnPoint(element, x, y);
    }

    public void moveMouseOver(WrapsElement element) {
        inOpenedBrowser().moveMouseOver(element);
    }

    public void typeInto(WrapsElement input, String text) {
        inOpenedBrowser().typeInto(input, text);
    }

    public void clear(WrapsElement input) {
        inOpenedBrowser().clear(input);
    }

    public void enterInto(WrapsElement input, String text) {
        inOpenedBrowser().enterInto(input, text);
    }

    public Object executeScript(String script) {
        return inOpenedBrowser().executeScript(script);
    }

    public boolean see(UIObject uiObject) {
        inOpenedBrowser().getUIObjectInitializer().initialize(uiObject);
        return uiObject.isDisplayed();
    }

    public boolean see(Class<? extends UIObject> uiObject) {
        return see(inOpenedBrowser().getUIObjectFactory().instatiate(uiObject));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return getName();
    }

}
