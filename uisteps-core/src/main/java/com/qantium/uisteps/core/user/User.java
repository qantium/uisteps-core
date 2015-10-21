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

    public boolean see(Object obj) {
        return obj != null && StringUtils.isEmpty(obj.toString());
    }

    public boolean see(Object obj, String value) {
        return obj != null && obj.toString().equals(value);
    }

    public boolean seePartOf(Object obj, String value) {
        return obj != null && obj.toString().contains(value);
    }

    public boolean see(UIBlockOrElement uiObject, String value) {
        return uiObject != null && uiObject.getText().equals(value);
    }

    public boolean seePartOf(UIBlockOrElement uiObject, String value) {
        return uiObject != null && uiObject.getText().contains(value);
    }

    public boolean see(UIObject uiObject) {
        return inOpenedBrowser().initialize(uiObject).isDisplayed();
    }

    public boolean see(Class<? extends UIBlockOrElement> uiObject, String value) {
        return uiObjectInstance(uiObject).getText().equals(value);
    }

    public boolean seePartOf(Class<? extends UIBlockOrElement> uiObject, String value) {
        return uiObjectInstance(uiObject).getText().contains(value);
    }

    public boolean see(Class<? extends UIObject> uiObject) {
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

    //Find 
    public <T extends UIBlockOrElement> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().find(uiObject, context);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, by, context);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, String name) {
        return inOpenedBrowser().find(uiObject, name);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, context);
    }

    public <T extends UIBlockOrElement> T find(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, by, context);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, context);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, by, context);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().findAll(uiObject, name);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, context);
    }

    public <T extends UIBlockOrElement> List<T> findAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, by, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(List<T> proxyElements) {
        return inOpenedBrowser().uiElements(proxyElements);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject) {
        return inOpenedBrowser().uiElements(uiObject);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, By by) {
        return inOpenedBrowser().uiElements(uiObject, by);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, by, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, String name) {
        return inOpenedBrowser().uiElements(uiObject, name);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().uiElements(uiObject, name, by);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, name, context);
    }

    public <T extends UIBlockOrElement> UIElements<T> findElements(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, name, by, context);
    }

}
