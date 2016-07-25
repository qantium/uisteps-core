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
import com.qantium.uisteps.core.factory.IUIObjectFactory;
import com.qantium.uisteps.core.factory.UIObjectFactory;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
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
public class Browser implements ISearchContext, ScriptExecutor, IUIObjectFactory, SearchContext, WithSearchContext {

    private WebDriver driver;
    private String name;
    private WindowManager windowManager;
    private UrlFactory urlFactory = new UrlFactory();
    private UIObjectFactory uiObjectFactory = new UIObjectFactory(this);
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

    public UIObjectFactory getUIObjectFactory() {
        return uiObjectFactory;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager = new WindowManager(driver);
    }

    public WindowManager getWindowManager() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get window manager");
        }
        return windowManager;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isAlive() {
        try {
            driver.getWindowHandles().size();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void setUrlFactory(UrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
    }

    public Photographer getPhotographer() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get photographer");
        }

        if (photographer == null) {
            photographer = new Photographer(driver);
        }

        return photographer;
    }

    public BrowserMobProxyServer getProxy() {
        return proxy;
    }

    public void setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
    }

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
    public Page openUrl(String url) {
        return openUrl(url, null);
    }

    public Page openUrl(String url, String[] params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot open url " + url, ex);
        }
    }

    public Page open(Url url) {
        return open(url, null);
    }

    public Page open(Url url, String[] params) {
        return open(Page.class, url, params);
    }

    public <T extends Page> T open(Class<T> page, Url url) {
        return open(page, url, null);
    }

    public <T extends Page> T open(Class<T> page, String[] params) {
        return open(page, null, params);
    }

    public <T extends Page> T open(Class<T> page, Url url, String[] params) {
        T pageInstance = uiObjectFactory.get(page);

        if (url != null) {
            pageInstance.setUrl(url);
        }

        if (!isEmpty(params)) {
            Url withParams = getUrlFactory().getUrlOf(page, params);
            pageInstance.setUrl(withParams);
        }

        return open(page);
    }

    public <T extends Page> T open(Class<T> page) {
        T pageInstance = uiObjectFactory.get(page);
        return open(pageInstance);
    }

    public <T extends Page> T open(T page) {
        Url url = page.getUrl();
        getDriver().get(url.toString());
        page.afterInitialization();
        return page;
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    //Window
    public void openNewWindow() {
        executeScript("window.open()");
        getWindowManager().switchToNextWindow();
    }

    public void switchToNextWindow() {
        getWindowManager().switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        getWindowManager().switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        getWindowManager().switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        getWindowManager().switchToWindowByIndex(index);
    }

    //Window position
    public Point getWindowPosition() {
        return getDriver().manage().window().getPosition();
    }

    public void setWindowPosition(int newX, int newY) {
        getDriver().manage().window().setPosition(new Point(newX, newY));
    }

    public void moveWindowBy(int xOffset, int yOffset) {
        getDriver().manage().window().getPosition().moveBy(xOffset, yOffset);

    }

    public void moveWindowTo(int newX, int newY) {
        getDriver().manage().window().getPosition().moveBy(newX, newY);
    }

    public void maximizeWindow() {
        getDriver().manage().window().maximize();
    }

    //Navigation
    public void goBack() {
        getDriver().navigate().back();
    }

    public void goForward() {
        getDriver().navigate().forward();
    }

    //Window size
    public Dimension getWindowSize() {
        return getDriver().manage().window().getSize();
    }

    public void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    public void setWindowWidth(int width) {
        setWindowSize(width, getWindowSize().getHeight());
    }

    public void setWindowHeight(int height) {
        setWindowSize(getWindowSize().getWidth(), height);

    }

    public void setWindowSize(String size) {
        Dimension defaultDimension = getWindowSize();
        int width = defaultDimension.width;
        int height = defaultDimension.height;

        String[] dimension = size.split("x");

        if (dimension.length > 0) {

            if (!StringUtils.isEmpty(size)) {
                width = Integer.parseInt(dimension[0]);
            }
        }

        if (dimension.length > 1) {
            height = Integer.parseInt(dimension[1]);
        }

        setWindowSize(width, height);
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
    }

    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void deleteCookieNamed(String name) {
        getDriver().manage().deleteCookieNamed(name);

    }

    public Set<Cookie> getCookies() {
        return getDriver().manage().getCookies();
    }

    public Actions getActions() {
        return new Actions(getDriver());
    }

    //Elements
    public void click() {
        getActions().click().perform();
    }

    public void clickAndHold() {
        getActions().clickAndHold().perform();
    }

    public void clickAndHold(UIElement element) {
        getActions().clickAndHold(element.getWrappedElement()).perform();
    }

    public void doubleClick() {
        getActions().doubleClick().perform();
    }

    public void doubleClick(UIElement element) {
        getActions().doubleClick(element.getWrappedElement()).perform();
    }

    public void contextClick() {
        getActions().contextClick().perform();
    }

    public void contextClick(UIElement element) {
        getActions().contextClick(element.getWrappedElement()).perform();
    }

    public void releaseMouse() {
        getActions().release().perform();
    }

    public void releaseMouse(UIElement element) {
        getActions().release(element.getWrappedElement()).perform();
    }

    public void dragAndDrop(UIElement source, UIElement target) {
        getActions().dragAndDrop(source.getWrappedElement(), target.getWrappedElement()).perform();
    }

    public void dragAndDrop(UIElement element, int xOffset, int yOffset) {
        getActions().dragAndDropBy(element.getWrappedElement(), xOffset, yOffset).perform();
    }

    public void keyDown(Keys theKey) {
        getActions().keyDown(theKey).perform();
    }

    public void keyDown(UIElement element, Keys theKey) {
        getActions().keyDown(element.getWrappedElement(), theKey).perform();
    }

    public void keyUp(Keys theKey) {
        getActions().keyUp(theKey).perform();
    }

    public void keyUp(UIElement element, Keys theKey) {
        getActions().keyUp(element.getWrappedElement(), theKey).perform();
    }

    public void click(UIElement element) {
        new Click(element).perform();
    }

    public void clickOnPoint(UIElement element, int x, int y) {
        getActions().moveToElement(element.getWrappedElement(), x, y).click().build().perform();
    }

    public void moveMouseByOffset(int xOffset, int yOffset) {
        getActions().moveByOffset(xOffset, yOffset).perform();
    }

    public void moveToElement(UIElement element, int xOffset, int yOffset) {
        getActions().moveToElement(element.getWrappedElement(), xOffset, yOffset).perform();
    }

    public void moveMouseOver(UIElement element) {
        getActions().moveToElement(element.getWrappedElement()).perform();
    }

    public void typeInto(UIElement input, Object text) {
        input.getWrappedElement().sendKeys(text.toString());
    }

    public void clear(UIElement input) {
        new Clear(input).perform();
    }

    public void enterInto(UIElement input, Object text) {
        new EnterInto(input, text).perform();
    }

    //Tags
    public String getTagNameOf(UIElement element) {
        return element.getWrappedElement().getTagName();
    }

    public String getAttribute(UIElement element, String attribute) {
        return element.getWrappedElement().getAttribute(attribute);
    }

    public String getCSSPropertyOf(UIElement element, String cssProperty) {
        return element.getWrappedElement().getCssValue(cssProperty);
    }

    public Point getPositionOf(UIElement element) {
        return element.getWrappedElement().getLocation();
    }

    public Point getMiddlePositionOf(UIElement element) {
        Point position = getPositionOf(element);
        Dimension size = getSizeOf(element);

        int x = position.x + size.width / 2;
        int y = position.y + size.height / 2;

        return new Point(x, y);
    }

    public Point getRelativePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getPositionOf(element);
        Point targetPosition = getPositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Point getRelativeMiddlePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getMiddlePositionOf(element);
        Point targetPosition = getMiddlePositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Dimension getSizeOf(UIElement element) {
        return element.getWrappedElement().getSize();
    }

    public String getTextFrom(UIElement input) {
        WebElement inputWrappedElement = input.getWrappedElement();

        if ("input".equals(input.getWrappedElement().getTagName())) {
            String enteredText = inputWrappedElement.getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return inputWrappedElement.getText();
        }
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    public String getName() {
        return name;
    }

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

    //Select
    public void select(Option option) {
        option.select();
    }

    public void deselectAllValuesFrom(Select select) {
        select.getWrappedSelect().deselectAll();
    }

    public void deselect(Option option) {
        option.deselect();
    }

    //Radio button
    public void select(RadioButton button) {
        if (!button.isSelected()) {
            new Click(button).perform();
        }
    }

    //CheckBox
    public void select(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().select();
    }

    public void deselect(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().deselect();
    }

    //Scrooll window
    public void scrollWindowByOffset(int x, int y) {
        executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    public void scrollWindowToTarget(UIElement element) {
        executeScript("arguments[0].scrollIntoView();", element.getWrappedElement());
    }

    public void scrollWindowToTargetByOffset(UIElement element, int x, int y) {
        WebElement target = element.getWrappedElement();
        Point location = target.getLocation();

        int xLocation = location.x + x;
        int yLocation = location.y + y;
        String script = "window.scroll(arguments[0],arguments[1]);";

        executeScript(script, xLocation, yLocation);
    }

    //Scroll
    public void scrollToTarget(UIElement scroll, UIElement target) {
        Point scrollPosition = getPositionOf(scroll);
        Point targetPosition = getPositionOf(target);
        scroll(scroll, new Point(targetPosition.x - scrollPosition.x, targetPosition.y - scrollPosition.y));
    }

    public void verticalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        verticalScroll(scroll, targetPosition.y - scrollPosition.y);
    }

    public void horizontalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        horizontalScroll(scroll, targetPosition.x - scrollPosition.x);
    }

    public void horizontalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(pixels, position.y));
    }

    public void verticalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(position.x, pixels));
    }

    public void scroll(UIElement scroll, int x, int y) {
        getActions()
                .clickAndHold(scroll.getWrappedElement())
                .moveByOffset(x, y)
                .release()
                .perform();
    }

    protected void scroll(UIElement scroll, Point point) {
        getActions()
                .clickAndHold(scroll.getWrappedElement())
                .moveByOffset(point.x, point.y)
                .release()
                .perform();
    }

    //FileInput
    public void setFileToUpload(FileInput fileInput, String filePath) {
        fileInput.getWrappedFileInput().setFileToUpload(filePath);
    }

    //Alert

    public void accept(Alert alert) {
        alert.getWrappedAlert().accept();
    }

    public void dismiss(ConfirmAlert confirm) {
        confirm.getWrappedAlert().dismiss();
    }

    public PromtAlert enterInto(PromtAlert promt, String text) {
        promt.getWrappedAlert().sendKeys(text);
        return promt;
    }

    public void authenticateUsing(AuthenticationAlert authenticationAlert, String login, String password) {
        Credentials credentials = new UserAndPassword(login, password);
        authenticationAlert.getWrappedAlert().authenticateUsing(credentials);
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
    public Photographer inScreenshotIgnoring(By... locators) {
        return getPhotographer().ignore(locators);
    }

    public Photographer inScreenshotIgnoring(UIElement... elements) {
        return getPhotographer().ignore(elements);
    }

    public Photographer inScreenshotIgnoring(Coords... areas) {
        return getPhotographer().ignore(areas);
    }

    public Screenshot takeScreenshot() {
        return getPhotographer().takeScreenshot();
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        return getPhotographer().takeScreenshot(elements);
    }

    public Screenshot takeScreenshot(Ignored... elements) {
        return getPhotographer().takeScreenshot(elements);
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
    public SearchContext getSearchContext() {
        return getDriver();
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject) {
        return getUIObjectFactory().getAll(uiObject);
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By locator) {
        return getUIObjectFactory().getAll(uiObject, locator);
    }

    @Override
    public UIElement get(By locator) {
        return getUIObjectFactory().get(locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return getUIObjectFactory().get(uiObject);
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By locator) {
        return getUIObjectFactory().get(uiObject, locator);
    }


}
