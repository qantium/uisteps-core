/*
 * Copyright 2014 ASolyankin.
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
package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.actions.Clear;
import com.qantium.uisteps.core.browser.actions.Click;
import com.qantium.uisteps.core.browser.actions.EnterInto;
import com.qantium.uisteps.core.browser.actions.GetText;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import com.qantium.uisteps.core.browser.pages.elements.FileInput;
import com.qantium.uisteps.core.browser.pages.elements.RadioButtonGroup.RadioButton;
import com.qantium.uisteps.core.browser.pages.elements.Select;
import com.qantium.uisteps.core.browser.pages.elements.Select.Option;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.browser.pages.elements.alert.AuthenticationAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.ConfirmAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.PromtAlert;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.screenshots.IPhotographer;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.then.Then;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.SOURCE_TAKE_FAKE;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * @author Anton Solyankin
 */
public class Browser implements IBrowser {

    private WebDriver driver;
    private String name;
    private IWindowManager windowManager;
    private UrlFactory urlFactory = new UrlFactory();
    private IUIObjectFactory uiObjectFactory = new UIObjectFactory(this);
    private BrowserMobProxyServer proxy;
    private Photographer photographer;

    public Browser() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager = new WindowManager(driver);
        photographer = new Photographer(driver);
    }

    private IWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public WebDriver getDriver() {
        if (driver == null) {
            throw new WebDriverException("WebDriver is not set in browser " + this + "!");
        }
        return driver;
    }

    @Override
    public boolean isAlive() {
        try {
            driver.getWindowHandles().size();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public BrowserMobProxyServer getProxy() {
        return proxy;
    }

    public void setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void close() {
        if (driver != null) {
            driver.quit();
        }

        if (proxy != null) {
            proxy.stop();
        }
    }

    @Override
    public List<WebElement> findElements(By locator) {
        return getDriver().findElements(locator);
    }

    @Override
    public WebElement findElement(By locator) {
        return getDriver().findElement(locator);
    }

    //Open
    @Override
    public Page openUrl(String url) {
        return openUrl(url, null);
    }

    @Override
    public Page openUrl(String url, String[] params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot open url " + url, ex);
        }
    }

    @Override
    public Page open(Url url) {
        return open(url, null);
    }

    @Override
    public Page open(Url url, String[] params) {
        return open(Page.class, url, params);
    }

    @Override
    public <T extends Page> T open(Class<T> page, Url url) {
        return open(page, url, null);
    }

    @Override
    public <T extends Page> T open(Class<T> page, String[] params) {
        return open(page, null, params);
    }

    @Override
    public <T extends Page> T open(Class<T> page) {
        return open(page, null, null);
    }

    @Override
    public <T extends Page> T open(Class<T> page, Url url, String[] params) {
        T pageInstance = uiObjectFactory.get(page);
        Url pageUrl;

        if (url != null) {
            pageUrl = url;
        } else {
            pageUrl = urlFactory.getUrlOf(pageInstance);
        }
        pageInstance.setUrl(pageUrl);

        if(!isEmpty(params)) {
            pageUrl = urlFactory.getUrlOf(pageInstance, params);
            pageInstance.setUrl(pageUrl);
        }

        return open(pageInstance);
    }

    @Override
    public <T extends Page> T open(T page) {
        Url url = page.getUrl();
        getDriver().get(url.toString());
        page.afterInitialization();
        return page;
    }

    @Override
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    @Override
    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    //Window
    @Override
    public void openNewWindow() {
        getWindowManager().openNewWindow();
    }

    @Override
    public void switchToNextWindow() {
        getWindowManager().switchToNextWindow();
    }

    @Override
    public void switchToPreviousWindow() {
        getWindowManager().switchToPreviousWindow();
    }

    @Override
    public void switchToDefaultWindow() {
        getWindowManager().switchToDefaultWindow();
    }

    @Override
    public void switchToWindowByIndex(int index) {
        getWindowManager().switchToWindowByIndex(index);
    }

    @Override
    public boolean hasNextWindow() {
        return getWindowManager().hasNextWindow();
    }

    @Override
    public boolean hasPreviousWindow() {
        return getWindowManager().hasPreviousWindow();
    }

    @Override
    public int getCountOfWindows() {
        return getWindowManager().getCountOfWindows();
    }

    @Override
    public int getCurrentWindowIndex() {
        return getWindowManager().getCurrentWindowIndex();
    }

    //Window position
    @Override
    public Point getWindowPosition() {
        return getDriver().manage().window().getPosition();
    }

    @Override
    public void setWindowPosition(int newX, int newY) {
        getDriver().manage().window().setPosition(new Point(newX, newY));
    }

    @Override
    public void moveWindowBy(int xOffset, int yOffset) {
        getDriver().manage().window().getPosition().moveBy(xOffset, yOffset);
    }

    @Override
    public void moveWindowTo(int newX, int newY) {
        getDriver().manage().window().getPosition().moveBy(newX, newY);
    }

    //Navigation
    @Override
    public void goBack() {
        getDriver().navigate().back();
    }

    @Override
    public void goForward() {
        getDriver().navigate().forward();
    }

    //Window size
    @Override
    public void maximizeWindow() {
        getDriver().manage().window().maximize();
    }

    @Override
    public Dimension getWindowSize() {
        return getDriver().manage().window().getSize();
    }

    @Override
    public void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    @Override
    public void setWindowWidth(int width) {
        setWindowSize(width, getWindowSize().getHeight());
    }

    @Override
    public void setWindowHeight(int height) {
        setWindowSize(getWindowSize().getWidth(), height);

    }

    @Override
    public void refreshPage() {
        getDriver().navigate().refresh();
    }

    @Override
    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    @Override
    public void deleteCookie(String name) {
        getDriver().manage().deleteCookieNamed(name);
    }

    @Override
    public Set<Cookie> getCookies() {
        return getDriver().manage().getCookies();
    }

    private Actions getActions() {
        return new Actions(getDriver());
    }

    //Elements
    @Override
    public void click() {
        getActions().click().perform();
    }

    @Override
    public void clickAndHold() {
        getActions().clickAndHold().perform();
    }

    @Override
    public void clickAndHold(UIElement element) {
        getActions().clickAndHold(element.getWrappedElement()).perform();
    }

    @Override
    public void doubleClick() {
        getActions().doubleClick().perform();
    }

    @Override
    public void doubleClick(UIElement element) {
        getActions().doubleClick(element.getWrappedElement()).perform();
    }

    @Override
    public void contextClick() {
        getActions().contextClick().perform();
    }

    @Override
    public void contextClick(UIElement element) {
        getActions().contextClick(element.getWrappedElement()).perform();
    }

    @Override
    public void releaseMouse() {
        getActions().release().perform();
    }

    @Override
    public void releaseMouse(UIElement element) {
        getActions().release(element.getWrappedElement()).perform();
    }

    @Override
    public void dragAndDrop(UIElement source, UIElement target) {
        getActions().dragAndDrop(source.getWrappedElement(), target.getWrappedElement()).perform();
    }

    public void dragAndDrop(UIElement element, int xOffset, int yOffset) {
        getActions().dragAndDropBy(element.getWrappedElement(), xOffset, yOffset).perform();
    }

    @Override
    public void keyDown(Keys theKey) {
        getActions().keyDown(theKey).perform();
    }

    @Override
    public void keyDown(UIElement element, Keys theKey) {
        getActions().keyDown(element.getWrappedElement(), theKey).perform();
    }

    @Override
    public void keyUp(Keys theKey) {
        getActions().keyUp(theKey).perform();
    }

    @Override
    public void keyUp(UIElement element, Keys theKey) {
        getActions().keyUp(element.getWrappedElement(), theKey).perform();
    }

    @Override
    public void click(UIElement element) {
        new Click(element).perform();
    }

    @Override
    public void clickOnPoint(UIElement element, int x, int y) {
        getActions().moveToElement(element.getWrappedElement(), x, y).click().build().perform();
    }

    @Override
    public void moveMouseByOffset(int xOffset, int yOffset) {
        getActions().moveByOffset(xOffset, yOffset).perform();
    }

    @Override
    public void moveToElement(UIElement element, int xOffset, int yOffset) {
        getActions().moveToElement(element.getWrappedElement(), xOffset, yOffset).perform();
    }

    @Override
    public void moveMouseOver(UIElement element) {
        getActions().moveToElement(element.getWrappedElement()).perform();
    }

    @Override
    public void typeInto(UIElement input, Object text) {
        input.getWrappedElement().sendKeys(text.toString());
    }

    @Override
    public void clear(UIElement input) {
        new Clear(input).perform();
    }

    @Override
    public void enterInto(UIElement input, Object text) {
        new EnterInto(input, text).perform();
    }

    //Tags
    @Override
    public String getTagNameOf(UIElement element) {
        return element.getWrappedElement().getTagName();
    }

    @Override
    public String getAttribute(UIElement element, String attribute) {
        return element.getWrappedElement().getAttribute(attribute);
    }

    @Override
    public String getCSSPropertyOf(UIElement element, String cssProperty) {
        return element.getWrappedElement().getCssValue(cssProperty);
    }

    @Override
    public Point getPositionOf(UIElement element) {
        return element.getWrappedElement().getLocation();
    }

    @Override
    public Point getMiddlePositionOf(UIElement element) {
        Point position = getPositionOf(element);
        Dimension size = getSizeOf(element);

        int x = position.x + size.width / 2;
        int y = position.y + size.height / 2;

        return new Point(x, y);
    }

    @Override
    public Point getRelativePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getPositionOf(element);
        Point targetPosition = getPositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    @Override
    public Point getRelativeMiddlePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getMiddlePositionOf(element);
        Point targetPosition = getMiddlePositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    @Override
    public Dimension getSizeOf(UIElement element) {
        return element.getWrappedElement().getSize();
    }

    @Override
    public String getTextFrom(UIElement element) {
        return new GetText(element).perform();
    }

    //Select
    @Override
    public void select(Option option) {
        option.select();
    }

    @Override
    public void deselectAllValuesFrom(Select select) {
        select.getWrappedSelect().deselectAll();
    }

    @Override
    public void deselect(Option option) {
        option.deselect();
    }

    //Radio button
    @Override
    public void select(RadioButton button) {
        if (!button.isSelected()) {
            new Click(button).perform();
        }
    }

    //CheckBox
    @Override
    public void select(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().select();
    }

    @Override
    public void deselect(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().deselect();
    }

    //Scroll window
    @Override
    public void scrollWindowByOffset(int x, int y) {
        executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    @Override
    public void scrollWindowToTarget(UIElement element) {
        executeScript("arguments[0].scrollIntoView();", element.getWrappedElement());
    }

    @Override
    public void scrollWindowToTargetByOffset(UIElement element, int x, int y) {
        WebElement target = element.getWrappedElement();
        Point location = target.getLocation();

        int xLocation = location.x + x;
        int yLocation = location.y + y;
        String script = "window.scroll(arguments[0],arguments[1]);";

        executeScript(script, xLocation, yLocation);
    }

    //Scroll
    @Override
    public void scrollToTarget(UIElement scroll, UIElement target) {
        Point scrollPosition = getPositionOf(scroll);
        Point targetPosition = getPositionOf(target);
        int targetX = targetPosition.x - scrollPosition.x;
        int targetY = targetPosition.y - scrollPosition.y;
        scroll(scroll, targetX, targetY);
    }

    @Override
    public void verticalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        verticalScroll(scroll, targetPosition.y - scrollPosition.y);
    }

    @Override
    public void horizontalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        horizontalScroll(scroll, targetPosition.x - scrollPosition.x);
    }

    @Override
    public void horizontalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, pixels, position.y);
    }

    @Override
    public void verticalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, position.x, pixels);
    }

    @Override
    public void scroll(UIElement scroll, int x, int y) {
        getActions()
                .clickAndHold(scroll.getWrappedElement())
                .moveByOffset(x, y)
                .release()
                .perform();
    }


    //FileInput
    @Override
    public void setFileToUpload(FileInput fileInput, String filePath) {
        fileInput.getWrappedFileInput().setFileToUpload(filePath);
    }

    //Alert
    @Override
    public void accept(Alert alert) {
        alert.getWrappedAlert().accept();
    }

    @Override
    public void dismiss(ConfirmAlert confirm) {
        confirm.getWrappedAlert().dismiss();
    }

    @Override
    public PromtAlert enterInto(PromtAlert promt, String text) {
        promt.getWrappedAlert().sendKeys(text);
        return promt;
    }

    @Override
    public void authenticateUsing(AuthenticationAlert authenticationAlert, String login, String password) {
        Credentials credentials = new UserAndPassword(login, password);
        authenticationAlert.getWrappedAlert().authenticateUsing(credentials);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
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
        StringBuilder browserName = new StringBuilder();

        String os = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        Dimension size = getWindowSize();

        browserName.append(NameConverter.humanize(getDriver().getClass()).replace(" driver", ""))
                .append(" os.name: ").append(os)
                .append(" os.arch: ").append(osArch)
                .append(" os.version: ").append(osVersion)
                .append(" width: ").append(size.width)
                .append(" height: ").append(size.height);

        if (!StringUtils.isEmpty(name)) {
            browserName.append(" name: ").append(name);
        }

        return browserName.toString();
    }

    //onDisplayed
    @Override
    public <T extends UIObject> T onDisplayed(T uiObject) {
        T uiObjectInstance;

        if (this.equals(uiObject.inOpenedBrowser())) {
            uiObjectInstance = uiObject;
        } else {
            uiObjectInstance = get((Class<T>) uiObject.getClass());
        }
        uiObjectInstance.afterInitialization();
        return uiObjectInstance;
    }

    @Override
    public UIElement onDisplayed(By locator) {
        return onDisplayed(get(locator));
    }

    @Override
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(get(uiObject));
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By locator) {
        return onDisplayed(get(uiObject, locator));
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject) {
        return onDisplayed(getAll(uiObject));
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By locator) {
        return onDisplayed(getAll(uiObject, locator));
    }

    //Screenshots
    @Override
    public IPhotographer ignore(By... locators) {
        return photographer.ignore(locators);
    }

    @Override
    public IPhotographer ignore(UIElement... elements) {
        return photographer.ignore(elements);
    }

    @Override
    public IPhotographer ignore(Coords... areas) {
        return photographer.ignore(areas);
    }

    @Override
    public IPhotographer ignore(int width, int height) {
        return photographer.ignore(width, height);
    }

    @Override
    public IPhotographer ignore(int x, int y, int width, int height) {
        return photographer.ignore(x, y, width, height);
    }

    @Override
    public Screenshot takeScreenshot() {
        return photographer.takeScreenshot();
    }

    @Override
    public Screenshot takeScreenshot(UIElement... elements) {
        return photographer.takeScreenshot(elements);
    }

    @Override
    public Screenshot takeScreenshot(Ignored... elements) {
        return photographer.takeScreenshot(elements);
    }

    //Page source
    public String getPageSource() {
        try {
            return getDriver().getPageSource();
        } catch (Exception ex) {

            if ("true".equals(getProperty(SOURCE_TAKE_FAKE).toLowerCase())) {
                return "CANNOT TAKE PAGE SOURCE!";
            } else {
                throw ex;
            }
        }
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject) {
        return uiObjectFactory.getAll(uiObject);
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By locator) {
        return uiObjectFactory.getAll(uiObject, locator);
    }

    @Override
    public UIElement get(By locator) {
        return uiObjectFactory.get(locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return uiObjectFactory.get(uiObject);
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By locator) {
        return uiObjectFactory.get(uiObject, locator);
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return new Then(uiObject, this);
    }
}
