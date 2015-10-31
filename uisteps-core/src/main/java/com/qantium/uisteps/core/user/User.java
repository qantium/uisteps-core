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

import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.BrowserFactory;
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.run.verify.conditions.Condition;
import com.qantium.uisteps.core.run.verify.conditions.DisplayCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class User implements Named {

    private final BrowserManager browserManager;
    public static final String DEFAULT_NAME = "user";
    private String name;

    public User(BrowserManager browserManager, String name) {
        this.browserManager = browserManager;
        this.name = name;
    }

    public User(BrowserFactory browserFactory, String name) {
        this(new BrowserManager(browserFactory), name);
    }

    public User(BrowserFactory browserFactory) {
        this(browserFactory, DEFAULT_NAME);
    }

    public User(BrowserManager browserManager) {
        this(browserManager, DEFAULT_NAME);
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public Browser inOpenedBrowser() {

        if (!BrowserManager.hasAny()) {
            openNewBrowser();
        }
        return BrowserManager.getCurrentBrowser();
    }

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    public Browser openNewBrowser(String withDriver) {
        return getBrowserManager().openNewBrowser(withDriver);
    }

    public Browser openNewBrowser() {
        return getBrowserManager().openNewBrowser();
    }

    public Browser switchToNextBrowser() {
        return BrowserManager.switchToNextBrowser();
    }

    public Browser switchToPreviousBrowser() {
        return BrowserManager.switchToPreviousBrowser();
    }

    public Browser switchToDefaultBrowser() {
        return BrowserManager.switchToFirstBrowser();
    }

    public Browser switchToBrowserByIndex(int index) throws NoBrowserException {
        return BrowserManager.switchToBrowserByIndex(index);
    }

    public Browser switchToLastBrowser() {
        return BrowserManager.switchToLastBrowser();
    }

    public void closeAllBrowsers() {
        BrowserManager.closeAllBrowsers();
    }

    public void closeCurrentBrowser() {
        BrowserManager.closeCurrentBrowser();
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

    //see
    private DisplayCondition displayCondition() {
        return new DisplayCondition(inOpenedBrowser());
    }

    public DisplayCondition not() {
        return displayCondition().not(true);
    }

    public Condition see(UIObject obj) {
        return displayCondition().see(obj);
    }

    public Condition see(Class<? extends UIObject> uiObject) {
        return displayCondition().see(uiObject);
    }

    public Condition see(String description, UIObject obj) {
        return displayCondition().see(description, obj);
    }

    public Condition see(String description, String obj) {
        return displayCondition().see(description, obj);
    }

    public Condition see(String description, Class<? extends UIObject> uiObject) {
        return displayCondition().see(description, uiObject);
    }

    public Condition see(UIObject uiObject, String value) {
        return displayCondition().see(uiObject, value);
    }

    public Condition see(Class<? extends UIObject> uiObject, String value) {
        return displayCondition().see(uiObject, value);
    }

    public Condition see(String description, String obj, String value) {
        return displayCondition().see(description, obj, value);
    }

    public Condition see(String description, UIObject obj, String value) {
        return displayCondition().see(description, obj, value);
    }

    public Condition see(String description, Class<? extends UIObject> uiObject, String value) {
        return displayCondition().see(description, uiObject, value);
    }

    public Condition seePartOf(UIElement obj, String value) {
        return displayCondition().seePartOf(obj, value);
    }

    public Condition seePartOf(String obj, String value) {
        return displayCondition().seePartOf(obj, value);
    }

    public Condition seePartOf(Class<? extends UIElement> uiObject, String value) {
        return displayCondition().seePartOf(uiObject, value);
    }

    public Condition seePartOf(String description, UIElement obj, String value) {
        return displayCondition().seePartOf(description, obj, value);
    }

    public Condition seePartOf(String description, String obj, String value) {
        return displayCondition().seePartOf(description, obj, value);
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
        return getName();
    }

    @Override
    public <T extends Named> T withName(String name) {
        setName(name);
        return (T) this;
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by);
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, UIObject context) {
        return inOpenedBrowser().onDisplayed(uiObject, context);
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by, UIObject context) {
        return inOpenedBrowser().onDisplayed(uiObject, by, context);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, UIObject context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, context);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by, UIObject context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, context);
    }
}
