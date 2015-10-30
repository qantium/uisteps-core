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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class Browser {

    private final WebDriver driver;
    private final WindowManager windowManager;
    private final LocatorFactory locatorFactory;
    private boolean opened;

    public Browser(WebDriver driver) {
        this.driver = driver;
        locatorFactory = new LocatorFactory();
        windowManager = new WindowManager(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isOpened() {
        return opened;
    }

    public Browser open() {
        opened = true;
        return this;
    }

    public WindowManager getWindowManager() {
        return windowManager;
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
        T uiObjectInstance = instatiate(uiObject);
        return populate(uiObjectInstance);
    }

    public void openUrl(String url, String... params) {
        try {
            open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new AssertionError("Cannot open url " + url + "\nCause:" + ex);
        }
    }

    public void open(Url url, String... params) {
        open(new MockPage("page", url, this).setParams(params).open());
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

    protected void open(MockPage page) {

    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    public MockPage getCurrentPage() {
        try {
            return new MockPage("page", new Url(getCurrentUrl()), this);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

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

    public void refreshCurrentPage() {
        getDriver().navigate().refresh();
    }

    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void click(WrapsElement element) {
        WebElement webElement = element.getWrappedElement();
        String attrTarget = webElement.getAttribute("target");

        webElement.click();

        if (attrTarget != null && !attrTarget.equals("") && !attrTarget.equals("_self")) {
            switchToNextWindow();
        }
    }

    public void clickOnPoint(WrapsElement element, int x, int y) {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element.getWrappedElement(), x, y).click().build().perform();
    }

    public void moveMouseOver(WrapsElement element) {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element.getWrappedElement()).build().perform();
    }

    public void typeInto(WrapsElement input, String text) {
        input.getWrappedElement().sendKeys(text);
    }

    public void clear(WrapsElement input) {
        input.getWrappedElement().clear();
    }

    public void enterInto(WrapsElement input, String text) {
        input.getWrappedElement().sendKeys(text);
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
        return "browser " + executeScript("return navigator.userAgent;").toString();
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

    //FileInput
    public void setTo(FileInput fileInput, String filePath) {
        fileInput.getWrappedFileInput().setFileToUpload(filePath);
    }

    public <T extends UIObject> T instatiate(Class<T> uiObject) {

        try {
            return (T) ConstructorUtils.invokeConstructor(uiObject, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject + ".\nCause: " + ex);
        }
    }

    public <T extends UIObject> T populate(T uiObject) {
        
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
        
        return uiObject;
    }

    private List<Field> getUIObjectFields(UIObject uiObject) {
        return getUIObjectFields(uiObject.getClass(), new ArrayList());
    }

    private List<Field> getUIObjectFields(Class<?> uiObject, List<Field> fields) {
        if (!UIObject.class.isAssignableFrom(uiObject) || uiObject == Page.class ||  uiObject == UIElements.class || ElementaryElement.class.isAssignableFrom(uiObject)) {
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
