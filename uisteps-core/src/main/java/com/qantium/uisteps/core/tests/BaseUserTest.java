/*
 * Copyright 2015 A.Solyankin.
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
package com.qantium.uisteps.core.tests;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.browser.Driver;
import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.user.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.conditions.DisplayCondition;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

/**
 * @param <U> specifies the type of user
 * @author A.Solyankin
 */
public class BaseUserTest<U extends User> extends BaseTest {

    public final U user;

    public BaseUserTest(Class<U> user) {
        this.user = instatiate(user);
    }

    protected <U extends User> U instatiate(Class<U> user) {
        try {
            return (U) ConstructorUtils.invokeConstructor(user, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + user + ".\nCause: " + ex);
        }
    }

    public Browser inOpenedBrowser() {
        return user.inOpenedBrowser();
    }

    public <T extends User> T openNewBrowser(Driver driver) {
        return user.openNewBrowser(driver);
    }

    public <T extends User> T openNewBrowser() {
        return user.openNewBrowser();
    }

    public <T extends User> T openNewBrowser(Driver driver, Map<String, Object> capabilities) {
        return user.openNewBrowser(driver, capabilities);
    }

    public <T extends User> T openNewBrowser(Map<String, Object> capabilities) {
        return user.openNewBrowser(capabilities);
    }

    public <T extends User> T openNewBrowser(String hub) {
        return user.openNewBrowser(hub);
    }

    public <T extends User> T openNewBrowser(String hub, Map<String, Object> capabilities) {
        return user.openNewBrowser(hub, capabilities);
    }

    public <T extends User> T openNewBrowser(String hub, Driver driver) {
        return user.openNewBrowser(hub, driver);
    }

    public <T extends User> T openNewBrowser(String hub, Driver driver, Map<String, Object> capabilities) {
        return user.openNewBrowser(hub, driver, capabilities);
    }

    public <T extends User> T openNewBrowser(WebDriver driver) {
        return user.openNewBrowser(driver);
    }

    public <T extends User> T switchToNextBrowser() {
        return user.switchToNextBrowser();
    }

    public <T extends User> T switchToPreviousBrowser() {
        return user.switchToPreviousBrowser();
    }

    public <T extends User> T switchToDefaultBrowser() {
        return user.switchToDefaultBrowser();
    }

    public <T extends User> T switchToBrowserByIndex(int index) throws NoBrowserException {
        return user.switchToBrowserByIndex(index);
    }

    public <T extends User> T switchToLastBrowser() {
        return user.switchToLastBrowser();
    }

    public void closeAllBrowsers() {
        user.closeAllBrowsers();
    }

    public <T extends User> T closeCurrentBrowser() {
        return user.closeCurrentBrowser();
    }

    public <T extends User> T openUrl(String url, String... params) {
        return user.openUrl(url, params);
    }

    public <T extends User> T open(Url url, String... params) {
        return user.open(url, params);
    }

    public <T extends Page> T open(Class<T> page, Url url, String... params) {
        return user.open(page, url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        return user.open(page, url, params);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return user.open(page, params);
    }

    public <T extends Page> T open(T page, String... params) {
        return user.open(page, params);
    }

    public <T extends User> T goBack() {
        return user.goBack();
    }

    public <T extends User> T goForward() {
        return user.goForward();
    }

    public <T extends User> T openNewWindow() {
        return user.openNewWindow();
    }

    public BrowserManager getBrowserManager() {
        return user.getBrowserManager();
    }

    public <T extends User> T switchToNextWindow() {
        return user.switchToNextWindow();
    }

    public <T extends User> T switchToPreviousWindow() {
        return user.switchToPreviousWindow();
    }

    public <T extends User> T switchToDefaultWindow() {
        return user.switchToDefaultWindow();
    }

    public <T extends User> T switchToWindowByIndex(int index) {
        return user.switchToWindowByIndex(index);
    }

    public Point getWindowPosition() {
        return user.getWindowPosition();
    }

    public <T extends User> T moveWindowBy(int xOffset, int yOffset) {
        return user.moveWindowBy(xOffset, yOffset);
    }

    public <T extends User> T moveWindowTo(int newX, int newY) {
        return user.moveWindowTo(newX, newY);
    }

    public <T extends User> T maximizeWindow() {
        return user.maximizeWindow();
    }

    public Dimension getWindowSize() {
        return user.getWindowSize();
    }

    public <T extends User> T setWindowSize(String size) {
        return user.setWindowSize(size);
    }

    public <T extends User> T setWindowSize(int width, int height) {
        return user.setWindowSize(width, height);
    }

    public <T extends User> T setWindowWidth(int width) {
        return user.setWindowWidth(width);
    }

    public <T extends User> T setWindowHeight(int height) {
        return user.setWindowHeight(height);
    }

    public <T extends User> T scrollWindowByOffset(int x, int y) {
        return user.scrollWindowByOffset(x, y);
    }

    public <T extends User> T scrollWindowToTarget(WrapsElement element) {
        return user.scrollWindowToTarget(element);
    }

    public <T extends User> T scrollWindowToTargetByOffset(WrapsElement element, int x, int y) {
        return user.scrollWindowToTargetByOffset(element, x, y);
    }

    public <T extends User> T setWindowPosition(int newX, int newY) {
        return user.setWindowPosition(newX, newY);
    }

    public <T extends User> T refreshtPage() {
        return user.refreshPage();
    }

    public String getCurrentTitle() {
        return user.getCurrentTitle();
    }

    public Page getCurrentPage() {
        return user.getCurrentPage();
    }

    public <T extends User> T deleteCookies() {
        return user.deleteCookies();
    }

    public <T extends User> T deleteCookieNamed(String name) {
        return user.deleteCookieNamed(name);
    }

    public Set<Cookie> getCookies() {
        return user.getCookies();
    }

    public <T extends User> T click() {
        return user.click();
    }

    public <T extends User> T clickAndHold() {
        return user.clickAndHold();
    }

    public <T extends User> T doubleClick() {
        return user.doubleClick();
    }

    public <T extends User> T contextClick() {
        return user.contextClick();
    }

    public <T extends User> T keyDown(Keys theKey) {
        return user.keyDown(theKey);
    }

    public <T extends User> T keyUp(Keys theKey) {
        return user.keyUp(theKey);
    }

    public <T extends User> T releaseMouse() {
        return user.releaseMouse();
    }

    public <T extends User> T moveMouseByOffset(int xOffset, int yOffset) {
        return user.moveMouseByOffset(xOffset, yOffset);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return user.executeAsyncScript(script, args);
    }

    public Object executeScript(String script, Object... args) {
        return user.executeScript(script, args);
    }

    public DisplayCondition userNot() {
        return user.not();
    }

    public DisplayCondition userNot(boolean not) {
        return user.not(not);
    }

    public DisplayCondition userNot(String not) {
        return user.not(not);
    }

    public Condition userSeePartOf(String description, String obj, String value) {
        return user.seePartOf(description, obj, value);
    }

    public Condition userSeePartOf(String description, UIElement obj, String value) {
        return user.seePartOf(description, obj, value);
    }

    public Condition userSeePartOf(Class<? extends UIElement> uiObject, String value) {
        return user.seePartOf(uiObject, value);
    }

    public Condition userSeePartOf(String obj, String value) {
        return user.seePartOf(obj, value);
    }

    public Condition userSee(String description, Class<? extends UIObject> uiObject, String value) {
        return user.see(description, uiObject, value);
    }

    public Condition userSeePartOf(UIElement obj, String value) {
        return user.seePartOf(obj, value);
    }

    public Condition userSee(String description, UIObject obj, String value) {
        return user.see(description, obj, value);
    }

    public Condition userSee(String description, String obj, String value) {
        return user.see(description, obj, value);
    }

    public Condition userSee(Class<? extends UIObject> uiObject, String value) {
        return user.see(uiObject, value);
    }

    public Condition userSee(UIObject uiObject, String value) {
        return user.see(uiObject, value);
    }

    public Condition userSee(String description, Class<? extends UIObject> uiObject) {
        return user.see(description, uiObject);
    }

    public Condition userSee(String description, String obj) {
        return user.see(description, obj);
    }

    public Condition userSee(String description, UIObject obj) {
        return user.see(description, obj);
    }

    public Condition userSee(Class<? extends UIObject> uiObject) {
        return user.see(uiObject);
    }

    public Condition userSee(UIObject uiObject) {
        return user.see(uiObject);
    }

    public Condition userSeeEqual(String description, Screenshot screenshot1, Screenshot screenshot2) {
        return user.seeEqual(description, screenshot1, screenshot2);
    }

    public Condition userSeeEqual(Screenshot screenshot1, Screenshot screenshot2) {
        return user.seeEqual(screenshot1, screenshot2);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return user.onDisplayed(uiObject);
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject) {
        return user.onDisplayed(context, uiObject);
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return user.onDisplayed(uiObject, by);
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject, By by) {
        return user.onDisplayed(context, uiObject, by);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return user.onDisplayedAll(uiObject);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return user.onDisplayed(uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return user.onDisplayedAll(uiObject, by);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject) {
        return user.onDisplayedAll(context, uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject, By by) {
        return user.onDisplayedAll(context, uiObject, by);
    }

    public Photographer inScreenshotIgnoring(By... locators) {
        return user.inScreenshotIgnoring(locators);
    }

    public Photographer inScreenshotIgnoring(UIElement... elements) {
        return user.inScreenshotIgnoring(elements);
    }

    public Photographer inScreenshotIgnoring(Coords... areas) {
        return user.inScreenshotIgnoring(areas);
    }

    public Screenshot takeScreenshot() {
        return user.takeScreenshot();
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        return user.takeScreenshot(elements);
    }

    public Screenshot takeScreenshot(Ignored... elements) {
        return user.takeScreenshot(elements);
    }

    public <T extends User> T waitUntilIsDisplayed(UIObject uiObject) {
        return user.waitUntilIsDisplayed(uiObject);
    }
}
