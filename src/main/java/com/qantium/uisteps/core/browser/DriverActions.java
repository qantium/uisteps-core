package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.screenshots.IPhotographer;
import com.qantium.uisteps.core.then.IThen;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public interface DriverActions extends IWindowManager, IThen, ScriptExecutor, IUIObjectFactory, IPhotographer {

    WebDriver getDriver();

    Page openUrl(String url);

    Page openUrl(String url, String[] params);

    default Page open(Url url) {
        return open(url, null);
    }

    default Page open(Url url, String[] params) {
        return open(Page.class, url, params);
    }

    default <T extends Page> T open(Class<T> page, Url url) {
        return open(page, url, null);
    }

    default <T extends Page> T open(Class<T> page, String[] params) {
        return open(page, null, params);
    }

    default <T extends Page> T open(Class<T> page) {
        return open(page, null, null);
    }

    <T extends Page> T open(Class<T> page, Url url, String[] params);

    default <T extends Page> T open(T page) {
        Url url = page.getUrl();
        getDriver().get(url.toString());
        Waiting.waitUntil(page, () -> page.isCurrentlyDisplayed());
        return page;
    }

    default String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    default String getCurrentTitle() {
        return getDriver().getTitle();
    }

    //Window position
    default Point getWindowPosition() {
        return getDriver().manage().window().getPosition();
    }

    default void setWindowPosition(int newX, int newY) {
        getDriver().manage().window().setPosition(new Point(newX, newY));
    }

    default void moveWindowBy(int xOffset, int yOffset) {
        getDriver().manage().window().getPosition().moveBy(xOffset, yOffset);
    }

    default void moveWindowTo(int newX, int newY) {
        getDriver().manage().window().getPosition().moveBy(newX, newY);
    }

    //Navigation
    default void goBack() {
        getDriver().navigate().back();
    }

    default void goForward() {
        getDriver().navigate().forward();
    }

    //Window size
    default void maximizeWindow() {
        getDriver().manage().window().maximize();
    }

    default Dimension getWindowSize() {
        return getDriver().manage().window().getSize();
    }

    default void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    default void setWindowWidth(int width) {
        setWindowSize(width, getWindowSize().getHeight());
    }

    default void setWindowHeight(int height) {
        setWindowSize(getWindowSize().getWidth(), height);
    }

    default void refreshPage() {
        getDriver().navigate().refresh();
    }

    default void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    default void deleteCookie(String name) {
        getDriver().manage().deleteCookieNamed(name);
    }

    default Set<Cookie> getCookies() {
        return getDriver().manage().getCookies();
    }

    default void click() {
        Waiting.wait(() -> new Actions(getDriver()).click().perform());
    }

    default void clickAndHold() {
        Waiting.wait(() -> new Actions(getDriver()).clickAndHold().perform());
    }

    default void doubleClick() {
        Waiting.wait(() -> new Actions(getDriver()).doubleClick().perform());
    }

    default void contextClick() {
        Waiting.wait(() -> new Actions(getDriver()).contextClick().perform());
    }

    default void releaseMouse() {
        Waiting.wait(() -> new Actions(getDriver()).release().perform());
    }

    default void keyDown(Keys theKey) {
        Waiting.wait(() -> new Actions(getDriver()).keyDown(theKey).perform());
    }

    default void keyUp(Keys theKey) {
        Waiting.wait(() -> new Actions(getDriver()).keyUp(theKey).perform());
    }

    default void keyPress(Keys theKey) {
        keyDown(theKey);
        keyUp(theKey);
    }

    default void moveMouseByOffset(int xOffset, int yOffset) {
        Waiting.wait(() -> new Actions(getDriver()).moveByOffset(xOffset, yOffset).perform());
    }


    @Override
    default Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

    @Override
    default Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

}
