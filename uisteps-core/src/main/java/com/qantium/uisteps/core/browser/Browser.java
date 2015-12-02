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

import com.qantium.uisteps.core.browser.pages.ElementaryElement;
import com.qantium.uisteps.core.then.Then;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.browser.pages.UrlFactory;
import com.qantium.uisteps.core.then.GetValueAction;
import com.qantium.uisteps.core.then.OnDisplayedAction;
import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import com.qantium.uisteps.core.browser.pages.elements.FileInput;
import com.qantium.uisteps.core.browser.pages.elements.Select;
import com.qantium.uisteps.core.browser.pages.elements.Select.Option;
import com.qantium.uisteps.core.browser.pages.elements.RadioButtonGroup.RadioButton;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.name.NameConvertor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

/**
 *
 * @author ASolyankin
 */
public class Browser {

    private WebDriver driver;
    private String name;
    private WindowManager windowManager;
    private LocatorFactory locatorFactory = new LocatorFactory();
    private UrlFactory urlFactory = new UrlFactory();
    private BrowserMobProxyServer proxy;
    private Photographer photographer;

    protected void setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager = new WindowManager(driver);
        photographer = new Photographer(driver);
    }

    protected WindowManager getWindowManager() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get windowManager");
        }
        return windowManager;
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected void setUrlFactory(UrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    protected void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
    }

    public Photographer getPhotographer() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get photographer");
        }
        return photographer;
    }

    public LocatorFactory getLocatorFactory() {
        return locatorFactory;
    }

    protected void setLocatorFactory(LocatorFactory locatorFactory) {
        this.locatorFactory = locatorFactory;
    }

    public BrowserMobProxyServer getProxy() {
        return proxy;
    }

    protected void setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    public <T extends UIElement> T displayed(Class<T> uiObject, By locator, UIObject context, WebElement wrappedElement) {
        T uiObjectInstance = instatiate(uiObject);
        uiObjectInstance.setLocator(locator);
        uiObjectInstance.setContext(context);
        uiObjectInstance.setWrappedElement(wrappedElement);
        return populate(uiObjectInstance);
    }

    public <T extends UIElement> T displayed(Class<T> uiObject, By locator, UIObject context) {
        return displayed(uiObject, locator, context, null);
    }

    public <T extends UIObject> T displayed(Class<T> uiObject) {
        return populate(instatiate(uiObject));
    }

    public Page openUrl(String url, String... params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot open url " + url + "\nCause:" + ex);
        }
    }

    public Page open(Url url, String... params) {
        return open(new Page(), url, params);
    }

    public <T extends Page> T open(Class<T> page, Url url, String... params) {
        return open(instatiate(page), url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        return open(page.setUrl(url), params);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return open(instatiate(page), params);
    }

    public <T extends Page> T open(T page, String... params) {
        page.setUrl(getUrlFactory().getUrlOf(page, params));
        populate(page);
        return open(page);
    }

    public <T extends Page> T open(T page) {
        getDriver().get(page.getUrl().toString());
        return page;
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    public Page getCurrentPage() {

        try {
            return new Page().<Page>withName("page").setUrl(new Url(getCurrentUrl()));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
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

    public void clickAndHold(WrapsElement element) {
        getActions().clickAndHold(element.getWrappedElement()).perform();

    }

    public void doubleClick() {
        getActions().doubleClick().perform();

    }

    public void doubleClick(WrapsElement element) {
        getActions().doubleClick(element.getWrappedElement()).perform();

    }

    public void contextClick() {
        getActions().contextClick().perform();
    }

    public void contextClick(WrapsElement element) {
        getActions().contextClick(element.getWrappedElement()).perform();
    }

    public void releaseMouse() {
        getActions().release().perform();
    }

    public void releaseMouse(WrapsElement element) {
        getActions().release(element.getWrappedElement()).perform();
    }

    public void dragAndDrop(WrapsElement source, WrapsElement target) {
        getActions().dragAndDrop(source.getWrappedElement(), target.getWrappedElement()).perform();

    }

    public void dragAndDrop(WrapsElement element, int xOffset, int yOffset) {
        getActions().dragAndDropBy(element.getWrappedElement(), xOffset, yOffset).perform();

    }

    public void keyDown(Keys theKey) {
        getActions().keyDown(theKey).perform();

    }

    public void keyDown(WrapsElement element, Keys theKey) {
        getActions().keyDown(element.getWrappedElement(), theKey).perform();

    }

    public void keyUp(Keys theKey) {
        getActions().keyUp(theKey).perform();
    }

    public void keyUp(WrapsElement element, Keys theKey) {
        getActions().keyUp(element.getWrappedElement(), theKey).perform();

    }

    public void click(WrapsElement element) {
        WebElement webElement = element.getWrappedElement();
        String attrTarget = webElement.getAttribute("target");

        getActions().click(webElement).perform();

        if (attrTarget != null && !attrTarget.equals("") && !attrTarget.equals("_self")) {
            switchToNextWindow();
        }

    }

    public void clickOnPoint(WrapsElement element, int x, int y) {
        getActions().moveToElement(element.getWrappedElement(), x, y).click().build().perform();

    }

    public void moveMouseByOffset(int xOffset, int yOffset) {
        getActions().moveByOffset(xOffset, yOffset).perform();

    }

    public void moveToElement(WrapsElement element, int xOffset, int yOffset) {
        getActions().moveToElement(element.getWrappedElement(), xOffset, yOffset).perform();

    }

    public void moveMouseOver(WrapsElement element) {
        getActions().moveToElement(element.getWrappedElement()).build().perform();

    }

    public void typeInto(WrapsElement input, String text) {
        getActions().sendKeys(input.getWrappedElement(), text).perform();

    }

    public void clear(WrapsElement input) {
        input.getWrappedElement().clear();

    }

    public void enterInto(WrapsElement input, String text) {
        getActions().sendKeys(input.getWrappedElement(), text).perform();

    }

    //Tags
    public String getTagNameOf(WrapsElement element) {
        return element.getWrappedElement().getTagName();
    }

    public String getCSSPropertyOf(WrapsElement element, String cssProperty) {
        return element.getWrappedElement().getCssValue(cssProperty);
    }

    public Point getPositionOf(WrapsElement element) {
        return element.getWrappedElement().getLocation();
    }

    public Point getMiddlePositionOf(WrapsElement element) {
        Point position = getPositionOf(element);
        Dimension size = getSizeOf(element);

        int x = position.x + size.width / 2;
        int y = position.y + size.height / 2;

        return new Point(x, y);
    }

    public Point getRelativePositionOf(WrapsElement element, WrapsElement target) {
        Point elementPosition = getPositionOf(element);
        Point targetPosition = getPositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Point getRelativeMiddlePositionOf(WrapsElement element, WrapsElement target) {
        Point elementPosition = getMiddlePositionOf(element);
        Point targetPosition = getMiddlePositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Dimension getSizeOf(WrapsElement element) {
        return element.getWrappedElement().getSize();
    }

    public String getTextFrom(WrapsElement input) {
        if ("input".equals(input.getWrappedElement().getTagName())) {
            String enteredText = input.getWrappedElement().getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return input.getWrappedElement().getText();
        }
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return new Then(new OnDisplayedAction<>(this, uiObject));
    }

    public <T> Then<T> then(T value) {
        return new Then(new GetValueAction<>(value));
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

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

        browserName.append(NameConvertor.humanize(getDriver().getClass()).replace(" driver", ""))
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
            button.getWrappedElement().click();
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

    public void scrollWindowToTarget(WrapsElement element) {
        executeScript("arguments[0].scrollIntoView();", element.getWrappedElement());
    }

    public void scrollWindowToTargetByOffset(WrapsElement element, int x, int y) {
        WebElement target = element.getWrappedElement();
        Point location = target.getLocation();

        int xLocation = location.x + x;
        int yLocation = location.y + y;
        String script = "window.scroll(arguments[0],arguments[1]);";

        executeScript(script, xLocation, yLocation);
    }

    //Scroll
    public void scrollToTarget(WrapsElement scroll, WrapsElement target) {
        Point scrollPosition = getPositionOf(scroll);
        Point targetPosition = getPositionOf(target);
        scroll(scroll, new Point(targetPosition.x - scrollPosition.x, targetPosition.y - scrollPosition.y));
    }

    public void verticalScrollToTarget(WrapsElement scroll, WrapsElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        verticalScroll(scroll, targetPosition.y - scrollPosition.y);
    }

    public void horizontalScrollToTarget(WrapsElement scroll, WrapsElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        horizontalScroll(scroll, targetPosition.x - scrollPosition.x);
    }

    public void horizontalScroll(WrapsElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(pixels, position.y));
    }

    public void verticalScroll(WrapsElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(position.x, pixels));
    }

    public void scroll(WrapsElement scroll, int x, int y) {
        getActions()
                .clickAndHold(scroll.getWrappedElement())
                .moveByOffset(x, y)
                .release()
                .perform();
    }

    protected void scroll(WrapsElement scroll, Point point) {
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

    public <T extends UIObject> T instatiate(Class<T> uiObject) {

        try {
            return (T) ConstructorUtils.invokeConstructor(uiObject, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject + ".\nCause: " + ex);
        }
    }

    public <T extends UIObject> T populate(T uiObject) {

        if (uiObject.isPopulatedBy(this)) {
            return uiObject;
        }

        if (uiObject instanceof UIElement) {
            UIElement uiElement = (UIElement) uiObject;

            if (uiElement.getLocator() == null) {
                By locator = getLocatorFactory().getLocator(uiElement);
                uiElement.setLocator(locator);
            }
        }

        for (Field field : getUIObjectFields(uiObject)) {
            Class<?> fieldType = field.getType();

            try {
                UIElement uiElement = instatiate((Class<UIElement>) fieldType);
                By locator = getLocatorFactory().getLocator(field);
                uiElement.setLocator(locator);
                uiElement.setContext(uiObject);
                uiElement.setName(NameConvertor.humanize(field));
                field.set(uiObject, uiElement);

                if (uiElement instanceof ElementaryElement) {
                    uiElement.setBrowser(this);
                } else {
                    populate(uiElement);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

        }
        uiObject.setBrowser(this);
        return uiObject;
    }

    private List<Field> getUIObjectFields(UIObject uiObject) {
        return getUIObjectFields(uiObject.getClass(), new ArrayList());
    }

    private List<Field> getUIObjectFields(Class<?> uiObject, List<Field> fields) {
        if (!UIObject.class.isAssignableFrom(uiObject) || uiObject == Page.class || uiObject == UIElements.class || ElementaryElement.class.isAssignableFrom(uiObject)) {
            return fields;
        }

        for (Field field : uiObject.getDeclaredFields()) {

            if (UIElement.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }

        return getUIObjectFields(uiObject.getSuperclass(), fields);
    }

    //find
    public <T extends UIElement> T find(Class<T> uiObject) {
        return find(uiObject, getLocatorFactory().getLocator(uiObject));
    }

    public <T extends UIElement> T find(Class<T> uiObject, By locator) {
        return find(uiObject, locator, null);
    }

    public <T extends UIElement> T find(Class<T> uiObject, UIObject context) {
        return find(uiObject, null, context);
    }

    public <T extends UIElement> T find(Class<T> uiObject, By locator, UIObject context) {
        return displayed(uiObject, locator, context);
    }

    //find all
    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject) {
        return findAll(uiObject, getLocatorFactory().getLocator(uiObject));
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By locator) {
        return findAll(uiObject, locator, null);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, UIObject context) {
        return findAll(uiObject, null, context);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By locator, UIObject context) {
        UIElements<T> uiElements = new UIElements(uiObject);
        uiElements.setLocator(locator);
        uiElements.setContext(context);
        return uiElements;
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(T uiObject) {
        return populate(uiObject);
    }

    public <T extends UIElement> T onDisplayed(T uiObject, UIObject context) {
        uiObject.setContext(context);
        return onDisplayed(uiObject);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {

        if (UIElement.class.isAssignableFrom(uiObject)) {
            return onDisplayed((T) find((Class<UIElement>) uiObject));
        } else {
            return onDisplayed(instatiate(uiObject));
        }
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return onDisplayed(find(uiObject, by));
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, UIObject context) {
        return onDisplayed(find(uiObject, context));
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by, UIObject context) {
        return onDisplayed(find(uiObject, by, context));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return onDisplayed(findAll(uiObject));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return onDisplayed(findAll(uiObject, by));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, UIObject context) {
        return onDisplayed(findAll(uiObject, context));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by, UIObject context) {
        return onDisplayed(findAll(uiObject, by, context));
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
}
