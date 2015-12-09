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
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.Driver;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.conditions.DisplayCondition;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

/**
 *
 * @author ASolyankin
 */
public class User implements Named {

    private BrowserManager browserManager = new BrowserManager();
    public static final String DEFAULT_NAME = "user";
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
        this(DEFAULT_NAME);
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public void setBrowserManager(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    public Browser inOpenedBrowser() {

        if (!getBrowserManager().hasAny()) {
            openNewBrowser();
        }
        return getBrowserManager().getCurrentBrowser();
    }

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    //Browser
    public <T extends User> T openNewBrowser(Driver driver) {
        getBrowserManager().openNewBrowser(driver);
        return (T) this;
    }

    public <T extends User> T openNewBrowser() {
        getBrowserManager().openNewBrowser();
        return (T) this;
    }

    public <T extends User> T openNewBrowser(Driver driver, Map<String, Object> capabilities) {
        getBrowserManager().openNewBrowser(driver, capabilities);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(Map<String, Object> capabilities) {
        getBrowserManager().openNewBrowser(capabilities);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(String hub) {
        getBrowserManager().openNewBrowser(hub);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(String hub, Map<String, Object> capabilities) {
        getBrowserManager().openNewBrowser(hub, capabilities);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(String hub, Driver driver) {
        getBrowserManager().openNewBrowser(hub, driver);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(String hub, Driver driver, Map<String, Object> capabilities) {
        getBrowserManager().openNewBrowser(hub, driver, capabilities);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(WebDriver driver) {
        getBrowserManager().openNewBrowser(driver);
        return (T) this;
    }

    public <T extends User> T switchToNextBrowser() {
        getBrowserManager().switchToNextBrowser();
        return (T) this;
    }

    public <T extends User> T switchToPreviousBrowser() {
        getBrowserManager().switchToPreviousBrowser();
        return (T) this;
    }

    public <T extends User> T switchToDefaultBrowser() {
        getBrowserManager().switchToFirstBrowser();
        return (T) this;
    }

    public <T extends User> T switchToBrowserByIndex(int index) throws NoBrowserException {
        getBrowserManager().switchToBrowserByIndex(index);
        return (T) this;
    }

    public <T extends User> T switchToLastBrowser() {
        getBrowserManager().switchToLastBrowser();
        return (T) this;
    }

    public void closeAllBrowsers() {
        getBrowserManager().closeAllBrowsers();
    }

    public <T extends User> T closeCurrentBrowser() {
        getBrowserManager().closeCurrentBrowser();
        return (T) this;
    }

    public <T extends User> T openUrl(String url, String... params) {
        inOpenedBrowser().openUrl(url, params);
        return (T) this;
    }

    public <T extends User> T open(Url url, String... params) {
        inOpenedBrowser().open(url, params);
        return (T) this;
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

    //Navigation
    public <T extends User> T goBack() {
        inOpenedBrowser().goBack();
        return (T) this;
    }

    public <T extends User> T goForward() {
        inOpenedBrowser().goForward();
        return (T) this;
    }

    //Window
    public <T extends User> T openNewWindow() {
        inOpenedBrowser().openNewWindow();
        return (T) this;
    }

    public <T extends User> T switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
        return (T) this;
    }

    public <T extends User> T switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
        return (T) this;
    }

    public <T extends User> T switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
        return (T) this;
    }

    public <T extends User> T switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
        return (T) this;
    }

    public Point getWindowPosition() {
        return inOpenedBrowser().getWindowPosition();
    }

    public <T extends User> T setWindowPosition(int newX, int newY) {
        inOpenedBrowser().setWindowPosition(newX, newY);
        return (T) this;
    }

    public <T extends User> T moveWindowBy(int xOffset, int yOffset) {
        inOpenedBrowser().moveWindowBy(xOffset, yOffset);
        return (T) this;
    }

    public <T extends User> T moveWindowTo(int newX, int newY) {
        inOpenedBrowser().moveWindowTo(newX, newY);
        return (T) this;
    }

    public <T extends User> T maximizeWindow() {
        inOpenedBrowser().maximizeWindow();
        return (T) this;
    }

    public Dimension getWindowSize() {
        return inOpenedBrowser().getWindowSize();
    }

    public <T extends User> T setWindowSize(String size) {
        inOpenedBrowser().setWindowSize(size);
        return (T) this;
    }

    public <T extends User> T setWindowSize(int width, int height) {
        inOpenedBrowser().setWindowSize(width, height);
        return (T) this;
    }

    public <T extends User> T setWindowWidth(int width) {
        inOpenedBrowser().setWindowWidth(width);
        return (T) this;
    }

    public <T extends User> T setWindowHeight(int height) {
        inOpenedBrowser().setWindowHeight(height);
        return (T) this;
    }

    //Scrooll window
    public <T extends User> T scrollWindowByOffset(int x, int y) {
        inOpenedBrowser().scrollWindowByOffset(x, y);
        return (T) this;
    }

    public <T extends User> T scrollWindowToTarget(WrapsElement element) {
        inOpenedBrowser().scrollWindowToTarget(element);
        return (T) this;
    }

    public <T extends User> T scrollWindowToTargetByOffset(WrapsElement element, int x, int y) {
        inOpenedBrowser().scrollWindowToTargetByOffset(element, x, y);
        return (T) this;
    }

    //Current page
    public <T extends User> T refreshtPage() {
        inOpenedBrowser().refreshPage();
        return (T) this;
    }

    public String getCurrentTitle() {
        return inOpenedBrowser().getCurrentTitle();
    }

    public Page getCurrentPage() {
        return inOpenedBrowser().getCurrentPage();
    }

    //Cookies
    public <T extends User> T deleteCookies() {
        Browser inOpenedBrowser = inOpenedBrowser();
        inOpenedBrowser.deleteAllCookies();
        return (T) this;
    }

    public <T extends User> T deleteCookieNamed(String name) {
        inOpenedBrowser().deleteCookieNamed(name);
        return (T) this;
    }

    public Set<Cookie> getCookies() {
        return inOpenedBrowser().getCookies();
    }

    //Actions
    public <T extends User> T click() {
        inOpenedBrowser().click();
        return (T) this;
    }

    public <T extends User> T clickAndHold() {
        inOpenedBrowser().clickAndHold();
        return (T) this;
    }

    public <T extends User> T doubleClick() {
        inOpenedBrowser().doubleClick();
        return (T) this;
    }

    public void contextClick() {
        inOpenedBrowser().contextClick();
    }

    public void releaseMouse() {
        inOpenedBrowser().releaseMouse();
    }

    public <T extends User> T keyDown(Keys theKey) {
        inOpenedBrowser().keyDown(theKey);
        return (T) this;
    }

    public <T extends User> T keyUp(Keys theKey) {
        inOpenedBrowser().keyUp(theKey);
        return (T) this;
    }

    public <T extends User> T moveMouseByOffset(int xOffset, int yOffset) {
        inOpenedBrowser().moveMouseByOffset(xOffset, yOffset);
        return (T) this;
    }

    public Object executeAsyncScript(String script, Object... args) {
        return inOpenedBrowser().executeAsyncScript(script, args);
    }

    public Object executeScript(String script, Object... args) {
        return inOpenedBrowser().executeScript(script, args);
    }

    //see
    private DisplayCondition displayCondition() {
        return new DisplayCondition(inOpenedBrowser());
    }

    public DisplayCondition not(String not) {
        return displayCondition().not(not);
    }

    public DisplayCondition not(boolean not) {
        return displayCondition().not(not);
    }

    public DisplayCondition not() {
        return not(true);
    }

    public Condition seeEqual(Screenshot screenshot1, Screenshot screenshot2) {
        return displayCondition().seeEqual(screenshot1, screenshot2);
    }

    public Condition seeEqual(String description, Screenshot screenshot1, Screenshot screenshot2) {
        return displayCondition().seeEqual(description, screenshot1, screenshot2);
    }

    public Condition see(UIObject uiObject) {
        return displayCondition().see(uiObject);
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

    public <T extends User> T withName(String name) {
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

    //take screenshot
    public Photographer inScreenshotIgnoring(By... locators) {
        return inOpenedBrowser().inScreenshotIgnoring(locators);
    }

    public Photographer inScreenshotIgnoring(UIElement... elements) {
        return inOpenedBrowser().inScreenshotIgnoring(elements);
    }

    public Photographer inScreenshotIgnoring(Coords... areas) {
        return inOpenedBrowser().inScreenshotIgnoring(areas);
    }

    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot();
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }

    public Screenshot takeScreenshot(Ignored... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }
}
