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

import com.qantium.uisteps.core.browser.*;
import com.qantium.uisteps.core.browser.factory.Driver;
import com.qantium.uisteps.core.browser.factory.DriverBuilder;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.screenshots.IPhotographer;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.storage.Storage;
import com.qantium.uisteps.core.then.Then;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.*;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Anton Solyankin
 */
public class User implements BrowserActions, IBrowserManager {

    private final IBrowserManager browserManager;
    private final Storage storage;

    public User(IBrowserManager browserManager, Storage storage) {
        this.browserManager = browserManager;
        this.storage = storage;
    }

    public User() {
        this(new BrowserManager(), new Storage());
    }

    public Storage getStorage() {
        return storage;
    }

    @Override
    public WebDriver getDriver() {
        if (!browserManager.hasAnyBrowser()) {
            throw new NoBrowserException();
        }
        return inOpenedBrowser().getDriver();
    }

    public IBrowser inOpenedBrowser() {
        if (!browserManager.hasAnyBrowser()) {
            openNewBrowser();
        }
        return browserManager.getCurrentBrowser();
    }

    @Override
    public Page openUrl(String url) {
        return inOpenedBrowser().openUrl(url);
    }

    @Override
    public Page openUrl(String url, String[] params) {
        return inOpenedBrowser().openUrl(url, params);
    }

    @Override
    public Page open(Url url) {
        return inOpenedBrowser().open(url);
    }

    @Override
    public Page open(Url url, String[] params) {
        return inOpenedBrowser().open(url, params);
    }

    @Override
    public <T extends Page> T open(Class<T> page, Url url) {
        return inOpenedBrowser().open(page, url);
    }

    @Override
    public <T extends Page> T open(Class<T> page, String[] params) {
        return inOpenedBrowser().open(page, params);
    }

    @Override
    public <T extends Page> T open(Class<T> page, Url url, String[] params) {
        return inOpenedBrowser().open(page, url, params);
    }

    @Override
    public <T extends Page> T open(Class<T> page) {
        return inOpenedBrowser().open(page);
    }

    @Override
    public <T extends Page> T open(T page) {
        return inOpenedBrowser().open(page);
    }

    @Override
    public String getCurrentUrl() {
        return inOpenedBrowser().getCurrentUrl();
    }

    @Override
    public String getCurrentTitle() {
        return inOpenedBrowser().getCurrentTitle();
    }

    @Override
    public Point getWindowPosition() {
        return inOpenedBrowser().getWindowPosition();
    }

    @Override
    public void setWindowPosition(int newX, int newY) {
        inOpenedBrowser().setWindowPosition(newX, newY);
    }

    @Override
    public void moveWindowBy(int xOffset, int yOffset) {
        inOpenedBrowser().moveWindowBy(xOffset, yOffset);
    }

    @Override
    public void moveWindowTo(int newX, int newY) {
        inOpenedBrowser().moveWindowTo(newX, newY);
    }

    @Override
    public void goBack() {
        inOpenedBrowser().goBack();
    }

    @Override
    public void goForward() {
        inOpenedBrowser().goForward();
    }

    @Override
    public void maximizeWindow() {
        inOpenedBrowser().maximizeWindow();
    }

    @Override
    public Dimension getWindowSize() {
        return inOpenedBrowser().getWindowSize();
    }

    @Override
    public void setWindowSize(int width, int height) {
        inOpenedBrowser().setWindowSize(width, height);
    }

    @Override
    public void setWindowWidth(int width) {
        inOpenedBrowser().setWindowWidth(width);
    }

    @Override
    public void setWindowHeight(int height) {
        inOpenedBrowser().setWindowHeight(height);
    }

    @Override
    public void refreshPage() {
        inOpenedBrowser().refreshPage();
    }

    @Override
    public void deleteAllCookies() {
        inOpenedBrowser().deleteAllCookies();
    }

    @Override
    public void deleteCookie(String name) {
        inOpenedBrowser().deleteCookie(name);
    }

    @Override
    public Set<Cookie> getCookies() {
        return inOpenedBrowser().getCookies();
    }

    @Override
    public void click() {
        inOpenedBrowser().click();
    }

    @Override
    public void clickAndHold() {
        inOpenedBrowser().clickAndHold();
    }

    @Override
    public void doubleClick() {
        inOpenedBrowser().doubleClick();
    }

    @Override
    public void contextClick() {
        inOpenedBrowser().contextClick();
    }

    @Override
    public void releaseMouse() {
        inOpenedBrowser().releaseMouse();
    }

    @Override
    public void keyDown(Keys theKey) {
        inOpenedBrowser().keyDown(theKey);
    }

    @Override
    public void keyUp(Keys theKey) {
        inOpenedBrowser().keyUp(theKey);
    }

    @Override
    public void moveMouseByOffset(int xOffset, int yOffset) {
        inOpenedBrowser().moveMouseByOffset(xOffset, yOffset);
    }

    @Override
    public void closeAllBrowsers() {
        browserManager.closeAllBrowsers();
    }

    @Override
    public void closeCurrentBrowser() {
        browserManager.closeCurrentBrowser();
    }

    @Override
    public IBrowser getCurrentBrowser() {
        return inOpenedBrowser();
    }

    @Override
    public IBrowser openNewBrowser() {
        return browserManager.openNewBrowser();
    }

    @Override
    public IBrowser openNewBrowser(WebDriver driver) {
        return browserManager.openNewBrowser(driver);
    }

    @Override
    public IBrowser openNewBrowser(Driver driver) {
        return browserManager.openNewBrowser(driver);
    }

    @Override
    public IBrowser openNewBrowser(String driver) {
        return browserManager.openNewBrowser(driver);
    }

    @Override
    public IBrowser openNewBrowser(DriverBuilder driverBuilder) {
        return browserManager.openNewBrowser(driverBuilder);
    }

    @Override
    public IBrowser open(IBrowser browser) {
        return browserManager.open(browser);
    }

    @Override
    public IBrowser switchToNextBrowser() {
        return browserManager.switchToNextBrowser();
    }

    @Override
    public IBrowser switchToPreviousBrowser() {
        return browserManager.switchToPreviousBrowser();
    }

    @Override
    public IBrowser switchToFirstBrowser() {
        return browserManager.switchToFirstBrowser();
    }

    @Override
    public IBrowser switchToLastBrowser() {
        return browserManager.switchToLastBrowser();
    }

    @Override
    public IBrowser switchToBrowserByIndex(int index) {
        return browserManager.switchToBrowserByIndex(index);
    }

    @Override
    public boolean hasNextBrowser() {
        return browserManager.hasNextBrowser();
    }

    @Override
    public boolean hasPreviousBrowser() {
        return browserManager.hasPreviousBrowser();
    }

    @Override
    public boolean hasAnyBrowser() {
        return browserManager.hasAnyBrowser();
    }

    @Override
    public IPhotographer ignore(By... locators) {
        return inOpenedBrowser().ignore(locators);
    }

    @Override
    public IPhotographer ignore(UIElement... elements) {
        return inOpenedBrowser().ignore(elements);
    }

    @Override
    public IPhotographer ignore(Coords... areas) {
        return inOpenedBrowser().ignore(areas);
    }

    @Override
    public IPhotographer ignore(int width, int height) {
        return inOpenedBrowser().ignore(width, height);
    }

    @Override
    public IPhotographer ignore(int x, int y, int width, int height) {
        return inOpenedBrowser().ignore(x, y, width, height);
    }

    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot();
    }

    @Override
    public Screenshot takeScreenshot(UIElement... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }

    @Override
    public Screenshot takeScreenshot(Ignored... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }

    @Override
    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    @Override
    public UIElement onDisplayed(By... locators) {
        return inOpenedBrowser().onDisplayed(locators);
    }

    @Override
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By... locators) {
        return inOpenedBrowser().onDisplayed(uiObject, locators);
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, Supplier<By[]> supplier) {
        return inOpenedBrowser().onDisplayed(uiObject, supplier);
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By... locators) {
        return inOpenedBrowser().onAllDisplayed(uiObject, locators);
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, Supplier<By[]> supplier) {
        return inOpenedBrowser().onAllDisplayed(uiObject, supplier);
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By... locators) {
        return inOpenedBrowser().getAll(uiObject, locators);
    }

    @Override
    public UIElement get(By... locators) {
        return inOpenedBrowser().get(locators);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return inOpenedBrowser().get(uiObject);
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By... locators) {
        return inOpenedBrowser().get(uiObject, locators);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By... locators) {
        return inOpenedBrowser().get(uiObject, context, locators);
    }

    @Override
    public void openNewWindow() {
        inOpenedBrowser().openNewWindow();
    }

    @Override
    public void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    @Override
    public void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    @Override
    public void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    @Override
    public void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }

    @Override
    public boolean hasNextWindow() {
        return inOpenedBrowser().hasNextWindow();
    }

    @Override
    public boolean hasPreviousWindow() {
        return inOpenedBrowser().hasPreviousWindow();
    }

    @Override
    public int getCountOfWindows() {
        return inOpenedBrowser().getCountOfWindows();
    }

    @Override
    public int getCurrentWindowIndex() {
        return inOpenedBrowser().getCurrentWindowIndex();
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return inOpenedBrowser().executeAsyncScript(script, args);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return inOpenedBrowser().executeScript(script, args);
    }

    //Storage
    public Storage toDir(String dir) {
        return getStorage().toDir(dir);
    }

    public File save(byte[] bytes, String path) throws IOException {
        return getStorage().save(bytes, path);
    }

    public File save(RenderedImage image, String path) throws IOException {
        return getStorage().save(image, path);
    }

    public File save(Har har, String path) throws IOException {
        return getStorage().save(har, path);
    }

    public File save(String data, String path) throws IOException {
        return getStorage().save(data, path);
    }

    public File save(Screenshot screenshot, String path) throws IOException {
        return getStorage().save(screenshot, path);
    }

}