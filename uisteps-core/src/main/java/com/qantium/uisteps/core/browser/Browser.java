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
import com.qantium.uisteps.core.browser.pages.MockPage;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.then.GetValueAction;
import com.qantium.uisteps.core.then.OnDisplayedAction;
import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import com.qantium.uisteps.core.browser.pages.elements.FileInput;
import com.qantium.uisteps.core.browser.pages.elements.Select;
import com.qantium.uisteps.core.browser.pages.elements.Select.Option;
import com.qantium.uisteps.core.browser.pages.elements.RadioButtonGroup.RadioButton;
import com.qantium.uisteps.core.name.NameConvertor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class Browser {

    private WebDriver driver;
    private final WindowManager windowManager = new WindowManager();
    private final LocatorFactory locatorFactory = new LocatorFactory();
    private boolean opened;

    public WebDriver getDriver() {
        return driver;
    }

    public <T extends Browser> T setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager.setDriver(driver);
        return (T) this;
    }

    public boolean isOpened() {
        return opened;
    }

    public <T extends Browser> T open() {
        opened = true;
        return (T) this;
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

    public <T extends Browser> T openUrl(String url, String... params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new AssertionError("Cannot open url " + url + "\nCause:" + ex);
        }
    }

    public <T extends Browser> T open(Url url, String... params) {
        return open(new MockPage(Page.DEFAULT_NAME, url, this).setParams(params).open());
    }

    public <T extends Page> T open(Class<T> page, Url url, String... params) {
        return open(instatiate(page), url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        page.setUrl(url);
        return open(page, params);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return open(instatiate(page), params);
    }

    public <T extends Page> T open(T page, String... params) {
        page.setParams(params);
        open(new MockPage(page.getName(), page.getUrl(), this).open());
        return populate(page);
    }

    protected <T extends Browser> T open(MockPage page) {
        return (T) this;
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    public MockPage getCurrentPage() {
        try {
            return new MockPage(Page.DEFAULT_NAME, new Url(getCurrentUrl()), this);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Window
    public <T extends Browser> T openNewWindow() {
        executeScript("window.open()");
        windowManager.switchToNextWindow();
        return (T) this;
    }

    public <T extends Browser> T switchToNextWindow() {
        windowManager.switchToNextWindow();
        return (T) this;
    }

    public <T extends Browser> T switchToPreviousWindow() {
        windowManager.switchToPreviousWindow();
        return (T) this;
    }

    public <T extends Browser> T switchToDefaultWindow() {
        windowManager.switchToDefaultWindow();
        return (T) this;
    }

    public <T extends Browser> T switchToWindowByIndex(int index) {
        windowManager.switchToWindowByIndex(index);
        return (T) this;
    }

    //Window position
    public Point getWindowPosition() {
        return getDriver().manage().window().getPosition();
    }

    public <T extends Browser> T setWindowPosition(int newX, int newY) {
        getDriver().manage().window().setPosition(new Point(newX, newY));
        return (T) this;
    }

    public <T extends Browser> T moveWindowBy(int xOffset, int yOffset) {
        getDriver().manage().window().getPosition().moveBy(xOffset, yOffset);
        return (T) this;
    }

    public <T extends Browser> T moveWindowTo(int newX, int newY) {
        getDriver().manage().window().getPosition().moveBy(newX, newY);
        return (T) this;
    }

    public <T extends Browser> T maximizeWindow() {
        getDriver().manage().window().maximize();
        return (T) this;
    }

    //Window size
    public Dimension getWindowSize() {
        return getDriver().manage().window().getSize();
    }

    public <T extends Browser> T setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
        return (T) this;
    }

    public <T extends Browser> T setWindowWidth(int width) {
        getDriver().manage().window().setSize(new Dimension(width, getWindowSize().getHeight()));
        return (T) this;
    }

    public <T extends Browser> T setWindowHeight(int height) {
        getDriver().manage().window().setSize(new Dimension(getWindowSize().getWidth(), height));
        return (T) this;
    }

    public <T extends Browser> T refreshCurrentPage() {
        getDriver().navigate().refresh();
        return (T) this;
    }

    public <T extends Browser> T deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
        return (T) this;
    }

    public <T extends Browser> T deleteCookieNamed(String name) {
        getDriver().manage().deleteCookieNamed(name);
        return (T) this;
    }

    public Actions getActions() {
        return new Actions(getDriver());
    }

    //Elements
    public <T extends Browser> T click() {
        getActions().click().perform();
        return (T) this;
    }

    public <T extends Browser> T clickAndHold() {
        getActions().clickAndHold().perform();
        return (T) this;
    }

    public <T extends Browser> T clickAndHold(WrapsElement element) {
        getActions().clickAndHold(element.getWrappedElement()).perform();
        return (T) this;
    }

    public <T extends Browser> T doubleClick() {
        getActions().doubleClick().perform();
        return (T) this;
    }

    public <T extends Browser> T doubleClick(WrapsElement element) {
        getActions().doubleClick(element.getWrappedElement()).perform();
        return (T) this;
    }

    public <T extends Browser> T dragAndDrop(WrapsElement source, WrapsElement target) {
        getActions().dragAndDrop(source.getWrappedElement(), target.getWrappedElement()).perform();
        return (T) this;
    }

    public <T extends Browser> T dragAndDrop(WrapsElement element, int xOffset, int yOffset) {
        getActions().dragAndDropBy(element.getWrappedElement(), xOffset, yOffset).perform();
        return (T) this;
    }

    public <T extends Browser> T keyDown(Keys theKey) {
        getActions().keyDown(theKey).perform();
        return (T) this;
    }

    public <T extends Browser> T keyDown(WrapsElement element, Keys theKey) {
        getActions().keyDown(element.getWrappedElement(), theKey).perform();
        return (T) this;
    }

    public <T extends Browser> T keyUp(Keys theKey) {
        getActions().keyUp(theKey).perform();
        return (T) this;
    }

    public <T extends Browser> T keyUp(WrapsElement element, Keys theKey) {
        getActions().keyUp(element.getWrappedElement(), theKey).perform();
        return (T) this;
    }

    public <T extends Browser> T click(WrapsElement element) {
        WebElement webElement = element.getWrappedElement();
        String attrTarget = webElement.getAttribute("target");

        getActions().click(webElement).perform();

        if (attrTarget != null && !attrTarget.equals("") && !attrTarget.equals("_self")) {
            switchToNextWindow();
        }
        return (T) this;
    }

    public <T extends Browser> T clickOnPoint(WrapsElement element, int x, int y) {
        getActions().moveToElement(element.getWrappedElement(), x, y).click().build().perform();
        return (T) this;
    }

    public <T extends Browser> T moveMouseByOffset(int xOffset, int yOffset) {
        getActions().moveByOffset(xOffset, yOffset).perform();
        return (T) this;
    }

    public <T extends Browser> T moveToElement(WrapsElement element, int xOffset, int yOffset) {
        getActions().moveToElement(element.getWrappedElement(), xOffset, yOffset).perform();
        return (T) this;
    }

    public <T extends Browser> T moveMouseOver(WrapsElement element) {
        getActions().moveToElement(element.getWrappedElement()).build().perform();
        return (T) this;
    }

    public <T extends Browser> T typeInto(WrapsElement input, String text) {
        getActions().sendKeys(input.getWrappedElement(), text).perform();
        return (T) this;
    }

    public <T extends Browser> T clear(WrapsElement input) {
        input.getWrappedElement().clear();
        return (T) this;
    }

    public <T extends Browser> T enterInto(WrapsElement input, String text) {
        getActions().sendKeys(input.getWrappedElement(), text).perform();
        return (T) this;
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

    public LocatorFactory getLocatorFactory() {
        return locatorFactory;
    }

    public Object executeScript(String script) {
        return ((JavascriptExecutor) getDriver()).executeScript(script);
    }

    @Override
    public String toString() {
        return getDriver().toString();//;"browser " + executeScript("return navigator.userAgent;").toString();
    }

    //Select
    public <T extends Browser> T select(Option option) {
        option.select();
        return (T) this;
    }

    public <T extends Browser> T deselectAllValuesFrom(Select select) {
        select.getWrappedSelect().deselectAll();
        return (T) this;
    }

    public <T extends Browser> T deselect(Option option) {
        option.deselect();
        return (T) this;
    }

    //Radio button
    public <T extends Browser> T select(RadioButton button) {

        if (!button.isSelected()) {
            button.getWrappedElement().click();
        }
        return (T) this;
    }

    //CheckBox
    public <T extends Browser> T select(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().select();
        return (T) this;
    }

    public <T extends Browser> T deselect(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().deselect();
        return (T) this;
    }

    //FileInput
    public <T extends Browser> T setTo(FileInput fileInput, String filePath) {
        fileInput.getWrappedFileInput().setFileToUpload(filePath);
        return (T) this;
    }

    public <T extends UIObject> T instatiate(Class<T> uiObject) {

        try {
            return (T) ConstructorUtils.invokeConstructor(uiObject, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject + ".\nCause: " + ex);
        }
    }

    public <T extends UIObject> T populate(T uiObject) {

        if (uiObject.isPopulated()) {
            return uiObject;
        }

        if (uiObject instanceof UIElement) {
            UIElement uiElement = (UIElement) uiObject;

            if (uiElement.getLocator() == null) {
                By locator = getLocatorFactory().getLocator(uiObject);
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

                if (!(uiElement instanceof ElementaryElement)) {
                    populate(uiElement);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

        }
        uiObject.setPopulated(true);
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
}
