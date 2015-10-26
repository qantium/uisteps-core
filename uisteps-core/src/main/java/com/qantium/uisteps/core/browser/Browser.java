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

import com.qantium.uisteps.core.then.Then;
import com.qantium.uisteps.core.browser.pages.MockPage;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIBlock;
import com.qantium.uisteps.core.browser.pages.UIBlockOrElement;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

/**
 *
 * @author ASolyankin
 */
public class Browser {

    private final WebDriver driver;
    private final WindowManager windowManager;
    private final LocatorFactory locatorFactory;
    private final Finder finder;
    private boolean opened;

    public Browser(WebDriver driver) {
        this.driver = driver;
        locatorFactory = new LocatorFactory();
        finder = new Finder(this);
        windowManager = new WindowManager(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public Finder getFinder() {
        return finder;
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

    public <T extends UIObject> T displayed(Class<T> uiObject, WebElement withWebElement) {
        return populateAsSearchContext(instatiate(uiObject, withWebElement));
    }

    public <T extends UIObject> T displayed(Class<T> uiObject) {
        return populate(instatiate(uiObject));
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

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {

        if (Page.class.isAssignableFrom(uiObject)) {
            return onDisplayed(displayed(uiObject));
        } else {
            return onDisplayed((T) getFinder().find((Class<UIBlockOrElement>) uiObject));
        }
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by) {
        return onDisplayed(getFinder().find(uiObject, by));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, SearchContext context) {
        return onDisplayed(getFinder().find(uiObject, context));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by, SearchContext context) {
        return onDisplayed(getFinder().find(uiObject, by, context));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name) {
        return onDisplayed(getFinder().find(uiObject, name));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, By by) {
        return onDisplayed(getFinder().find(uiObject, name, by));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, SearchContext context) {
        return onDisplayed(getFinder().find(uiObject, name, context));
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, By by, SearchContext context) {
        return onDisplayed(getFinder().find(uiObject, name, by, context));
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return uiObject;
    }
    
    public <T extends UIElements> T onDisplayed(T uiElements) {
        return uiElements;
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(List<T> proxyElements) {
        return onDisplayed(getFinder().uiElements(proxyElements));
    }
    
    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return onDisplayed(getFinder().uiElements(uiObject));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return onDisplayed(getFinder().uiElements(uiObject, by));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, SearchContext context) {
        return onDisplayed(getFinder().uiElements(uiObject, context));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by, SearchContext context) {
        return onDisplayed(getFinder().uiElements(uiObject, by, context));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name) {
        return onDisplayed(getFinder().uiElements(uiObject, name));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, By by) {
        return onDisplayed(getFinder().uiElements(uiObject, name, by));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, SearchContext context) {
        return onDisplayed(getFinder().uiElements(uiObject, name, context));
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return onDisplayed(getFinder().uiElements(uiObject, name, by, context));
    }

    public <T extends UIObject> T instatiate(Class<T> uiObject) {

        try {
            return (T) ConstructorUtils.invokeConstructor(uiObject, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject + ".\nCause: " + ex);
        }
    }

    public <T extends UIObject> T instatiate(Class<T> uiObject, WebElement wrappedElement) {

        if (UIBlock.class.isAssignableFrom(uiObject)) {

            T uiObjectInstance = instatiate(uiObject);
            ((UIBlock) uiObjectInstance).setWrappedElement(wrappedElement);
            return uiObjectInstance;

        }

        if (UIElement.class.isAssignableFrom(uiObject)) {

            try {
                return (T) ConstructorUtils.invokeConstructor(uiObject, wrappedElement);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                throw new RuntimeException("Cannot instantiate " + uiObject + " with parameter " + wrappedElement + ".\nCause: " + ex);
            }
        }

        throw new RuntimeException("Cannot instantiate! " + uiObject + " is not assignable from UIBlock or UIElement!");
    }

    public <T extends UIObject> T populate(T uiObject) {
        populate(uiObject, getDriver());
        HtmlElementLoader.populate(uiObject, getDriver());
        return uiObject;
    }

    public <T extends UIObject> T populateAsSearchContext(T context) {
        HtmlElementLoader.populatePageObject(context, context.getSearchContext());
        populate(context, context.getSearchContext());
        return context;
    }
    
    private void populate(UIObject uiObject, SearchContext context) {
        
        for(Field field: getFields(uiObject)) {
            
            try {
                UIBlockOrElement value = getFinder().find((Class<UIBlockOrElement>) field.getType(), getLocatorFactory().getLocator(field), context);
                field.set(uiObject, value);
                populateAsSearchContext(value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private List<Field> getFields(UIObject uiObject) {
        return getFields(uiObject.getClass(), new ArrayList());
    }
    
    private List<Field> getFields(Class<?> uiObject, List<Field> fields) {
        
        if(uiObject == Object.class) {
            return fields;
        }
        
        for(Field field: uiObject.getDeclaredFields()) {
            
            if(UIBlockOrElement.class.isAssignableFrom(field.getType()) || UIElements.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        
        return getFields(uiObject.getSuperclass(), fields);
    }
}
