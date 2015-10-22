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
import com.qantium.uisteps.core.browser.pages.UIBlockOrElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.run.verify.conditions.Condition;
import java.util.List;
import org.eclipse.aether.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
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

        if (!getBrowserManager().hasAny()) {
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
        return getBrowserManager().switchToNextBrowser();
    }

    public Browser switchToPreviousBrowser() {
        return getBrowserManager().switchToPreviousBrowser();
    }

    public Browser switchToDefaultBrowser() {
        return getBrowserManager().switchToFirstBrowser();
    }

    public Browser switchToBrowserByIndex(int index) throws NoBrowserException {
        return getBrowserManager().switchToBrowserByIndex(index);
    }

    public Browser switchToLastBrowser() {
        return getBrowserManager().switchToLastBrowser();
    }

    public void closeAllBrowsers() {
        getBrowserManager().closeAllBrowsers();
    }

    public void closeCurrentBrowser() {
        getBrowserManager().closeCurrentBrowser();
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

    private Condition getCondition(boolean condition, String expected, String altExpected, String actual) {
        if (!condition) {
            expected = altExpected;
        }
        return Condition.isTrue(condition, expected, actual);
    }

    private Condition getSeeCondition(boolean condition, String expected, String altExpected, String actual) {
        String message = "user see ";
        expected = message + expected;
        actual = message + actual;
        altExpected = message + altExpected;
        return getCondition(condition, expected, altExpected, actual);
    }

    private Condition getSeeCondition(boolean condition, String expected, String actual) {
        return getSeeCondition(condition, expected, expected, actual);
    }

    public Condition see(Object obj) {
        return getSeeCondition(obj != null && StringUtils.isEmpty(obj.toString()), "\"" + obj + "\"", "some object", "\"" + obj + "\"");
    }

    public Condition see(Object obj, String value) {
        return getSeeCondition(obj != null && obj.toString().equals(value), "\"" + value + "\"", "\"" + obj + "\"");
    }

    public Condition seePartOf(Object obj, String value) {
        String text = "";
        
        if(obj != null) {
           text = obj.toString();
        }
        return getSeeCondition(obj != null && text.contains(value), "part \"" + value + "\" of \"" + text + "\"", "\"" + obj + "\"");
    }

    public Condition see(UIBlockOrElement uiObject, String value) {
        return getSeeCondition(uiObject != null && uiObject.getText().equals(value), "\"" + value + "\"", "\"" + uiObject + "\"");
    }

    public Condition seePartOf(UIBlockOrElement uiObject, String value) {
        String text = "";
        
        if(uiObject != null) {
           text = uiObject.getText();
        }
        return getSeeCondition(uiObject != null && text.contains(value), "part \"" + value + "\" of " + text, "\"" + uiObject + "\"");
    }

    public Condition see(UIObject uiObject) {
        UIObject obj = inOpenedBrowser().initialize(uiObject);
        return getSeeCondition(obj.isDisplayed(), "\"" + obj + "\"", "some object", "\"" + obj + "\"");
    }

    public Condition see(Class<? extends UIBlockOrElement> uiObject, String value) {
        return getSeeCondition(uiObjectInstance(uiObject).getText().equals(value), "\"" + value + "\"", "\"" + uiObject + "\"");
    }

    public Condition seePartOf(Class<? extends UIBlockOrElement> uiObject, String value) {
        String text = uiObjectInstance(uiObject).getText();
        return getSeeCondition(text.contains(value), "part \"" + value + "\" of \"" + text + "\"", "\"" + uiObject + "\"");
    }

    public Condition see(Class<? extends UIObject> uiObject) {
        return see(uiObjectInstance(uiObject));
    }

    private <T extends UIObject> T uiObjectInstance(Class<T> uiObject) {
        return inOpenedBrowser().instatiate(uiObject);
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

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, context);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, by, context);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayed(uiObject, name);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, name, context);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by, context);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(List<T> proxyElements) {
        return inOpenedBrowser().onDisplayedAll(proxyElements);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name, by);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name, by, context);
    }

}
