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

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.factory.Driver;
import com.qantium.uisteps.core.browser.factory.DriverBuilder;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.storage.Storage;
import com.qantium.uisteps.core.then.Then;
import com.qantium.uisteps.core.utils.data.Data;
import com.qantium.uisteps.core.utils.data.DataContainer;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Anton Solyankin
 */
public class User  {

    private BrowserManager browserManager;
    private DataContainer DATA;
    public Storage storage;

    public User() {
        setBrowserManager(new BrowserManager());
        setData(new DataContainer(new Data()));
        setStorage(new Storage());
    }

    public DataContainer data() {
        return DATA;
    }

    public <T extends User> T  setData(DataContainer DATA) {
        this.DATA = DATA;
        return (T) this;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public <T extends User> T setBrowserManager(BrowserManager browserManager) {
        this.browserManager = browserManager;
        return (T) this;
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

    public <T extends User> T openNewBrowser(WebDriver driver) {
        getBrowserManager().openNewBrowser(driver);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(String driver) {
        getBrowserManager().openNewBrowser(driver);
        return (T) this;
    }

    public <T extends User> T openNewBrowser(DriverBuilder driverBuilder) {
        getBrowserManager().openNewBrowser(driverBuilder);
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

    public synchronized void closeAllBrowsers() {
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
    public <T extends User> T refreshPage() {
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

    public <T extends User> T contextClick() {
        inOpenedBrowser().contextClick();
        return (T) this;
    }

    public <T extends User> T releaseMouse() {
        inOpenedBrowser().releaseMouse();
        return (T) this;
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

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by);
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(context, uiObject);
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(context, uiObject, by);
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

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(context, uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(context, uiObject, by);
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

    public <T extends User> T waitUntilIsDisplayed(UIObject uiObject) {
        inOpenedBrowser().wait(uiObject).untilIsDisplayed();
        return (T) this;
    }

    public <T extends User> T waitUntilIsNotDisplayed(UIObject uiObject) {
        inOpenedBrowser().wait(uiObject).untilIsNotDisplayed();
        return (T) this;
    }

    public boolean see(UIObject uiObject) {
        try {
            waitUntilIsDisplayed(uiObject);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isOnDisplayed(UIObject uiObject) {
        try {
            waitUntilIsNotDisplayed(uiObject);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //Storage
    public Storage toDir(String dir) {
        return storage.toDir(dir);
    }

    public File put(File file) {
        return storage.put(file);
    }

    public File save(byte[] bytes, String path) throws IOException {
        return storage.save(bytes, path);
    }

    public <T> T put(String key, T value) {
        return storage.put(key, value);
    }

    public File save(RenderedImage image, String path) throws IOException {
        return storage.save(image, path);
    }

    public File save(Har har, String path) throws IOException {
        return storage.save(har, path);
    }

    public <T> T get(Class<T> key) {
        return storage.get(key);
    }

    public <T> T put(T value) {
        return storage.put(value);
    }

    public File save(String data, String path) throws IOException {
        return storage.save(data, path);
    }

    public File save(Screenshot screenshot, String path) throws IOException {
        return storage.save(screenshot, path);
    }

    public <T> T get(String key) {
        return storage.get(key);
    }
}
