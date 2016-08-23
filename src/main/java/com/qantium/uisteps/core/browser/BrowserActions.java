package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.screenshots.IPhotographer;
import com.qantium.uisteps.core.then.IThen;
import org.openqa.selenium.*;

import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public interface BrowserActions extends IWindowManager, IThen, ScriptExecutor, IUIObjectFactory, ISearchContext, IPhotographer {

    WebDriver getDriver();

    Page openUrl(String url);

    Page openUrl(String url, String[] params);

    Page open(Url url);

    Page open(Url url, String[] params);

    <T extends Page> T open(Class<T> page, Url url);

    <T extends Page> T open(Class<T> page, String[] params);

    <T extends Page> T open(Class<T> page, Url url, String[] params);

    <T extends Page> T open(Class<T> page);

    <T extends Page> T open(T page);

    String getCurrentUrl();

    String getCurrentTitle();

    //Window position
    Point getWindowPosition();

    void setWindowPosition(int newX, int newY);

    void moveWindowBy(int xOffset, int yOffset);

    void moveWindowTo(int newX, int newY);

    //Navigation
    public void goBack();

    public void goForward();

    //Window size
    void maximizeWindow();

    Dimension getWindowSize();

    void setWindowSize(int width, int height);

    void setWindowWidth(int width);

    void setWindowHeight(int height);

    void refreshPage();

    void deleteAllCookies();

    void deleteCookie(String name);

    Set<Cookie> getCookies();

    //Actions
    void click();

    void clickAndHold();

    void doubleClick();

    void contextClick();

    void releaseMouse();

    void keyDown(Keys theKey);

    void keyUp(Keys theKey);

    void moveMouseByOffset(int xOffset, int yOffset);
}
