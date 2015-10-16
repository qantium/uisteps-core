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
import com.qantium.uisteps.core.browser.pages.UIObjectInitializer;
import com.qantium.uisteps.core.browser.pages.MockPage;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.UIObjectFactory;
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.then.GetValueAction;
import com.qantium.uisteps.core.then.OnDisplayedAction;
import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import com.qantium.uisteps.core.browser.pages.elements.FileInput;
import com.qantium.uisteps.core.browser.pages.elements.Select;
import com.qantium.uisteps.core.browser.pages.elements.Select.Option;
import com.qantium.uisteps.core.browser.pages.elements.RadioButtonGroup.RadioButton;
import java.net.MalformedURLException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
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
    private final UIObjectFactory uiObjectFactory;
    private final UIObjectInitializer initializer;
    private final WindowManager windowManager;
    private final LocatorFactory locatorFactory;
    private final Finder finder;
    private boolean opened;

    public Browser(WebDriver driver, UIObjectFactory uiObjectFactory, UIObjectInitializer initializer, LocatorFactory locatorFactory) {
        this.driver = driver;
        this.uiObjectFactory = uiObjectFactory;
        this.initializer = initializer;
        this.locatorFactory = locatorFactory;
        finder = new Finder(this);
        windowManager = new WindowManager(driver);
    }

    public Browser(WebDriver driver) {
        this(driver, new UIObjectFactory(), new UIObjectInitializer(driver) , new LocatorFactory());
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

    public <T extends UIObject> T displayed(Class<T> uiObject, WebElement withWebElement) {
        T uiObjectInstance = getUIObjectFactory().instatiate(uiObject, withWebElement);
        getUIObjectInitializer().initializeWithSearchContext(uiObjectInstance);
        return uiObjectInstance;
    }

    public <T extends UIObject> T displayed(Class<T> uiObject) {
        T uiObjectInstance = getUIObjectFactory().instatiate(uiObject);
        getUIObjectInitializer().initialize(uiObjectInstance);
        return uiObjectInstance;
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
        return open(uiObjectFactory.instatiate(page), url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        page.setUrl(url);
        return open(page, params);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return open(uiObjectFactory.instatiate(page), params);
    }

    public <T extends Page> T open(T page, String... params) {
        page.setParams(params);
        open(new MockPage(page.getName(), page.getUrl(), this).open());
        getUIObjectInitializer().initialize(page);
        return page;
    }

    protected void open(MockPage page) {

    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(find(uiObject));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, By by) {
        return onDisplayed(find(uiObject, by));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, SearchContext context) {
        return onDisplayed(find(uiObject, context));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, By by, SearchContext context) {
        return onDisplayed(find(uiObject, by, context));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name) {
        return onDisplayed(find(uiObject, name));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, By by) {
        return onDisplayed(find(uiObject, name, by));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, SearchContext context) {
        return onDisplayed(find(uiObject, name, context));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, By by, SearchContext context) {
        return onDisplayed(find(uiObject, name, by, context));
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return uiObject;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getCurrentTitle() {
        return driver.getTitle();
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
        windowManager.switchToNextWindow();
    }

    public void switchToNextWindow() {
        windowManager.switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        windowManager.switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        windowManager.switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        windowManager.switchToWindowByIndex(index);
    }

    public void refreshCurrentPage() {
        driver.navigate().refresh();
    }

    public void deleteCookies() {
        driver.manage().deleteAllCookies();
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
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getWrappedElement(), x, y).click().build().perform();
    }

    public void moveMouseOver(WrapsElement element) {
        Actions actions = new Actions(driver);
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

    public UIObjectInitializer getUIObjectInitializer() {
        return initializer;
    }

    public UIObjectFactory getUIObjectFactory() {
        return uiObjectFactory;
    }

    public WindowManager getWindowList() {
        return windowManager;
    }

    public LocatorFactory getLocatorFactory() {
        return locatorFactory;
    }
    
    public Object executeScript(String script) {
        return ((JavascriptExecutor) driver).executeScript(script);
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
    
    //Find 
    public <T extends UIObject> T find(Class<T> uiObject) {
        return finder.find(uiObject);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by) {
        return finder.find(uiObject, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, SearchContext context) {
        return finder.find(uiObject, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by, SearchContext context) {
        return finder.find(uiObject, by, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name) {
        return finder.find(uiObject, name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by) {
        return finder.find(uiObject, name, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, SearchContext context) {
        return finder.find(uiObject, name, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by, SearchContext context) {
        return finder.find(uiObject, name, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject) {
        return finder.findAll(uiObject);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by) {
        return finder.findAll(uiObject, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, SearchContext context) {
        return finder.findAll(uiObject, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by, SearchContext context) {
        return finder.findAll(uiObject, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name) {
        return finder.findAll(uiObject, name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by) {
        return finder.findAll(uiObject, name, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, SearchContext context) {
        return finder.findAll(uiObject, name, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return finder.findAll(uiObject, name, by, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(List<T> proxyElements) {
        return finder.uiElements(proxyElements);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject) {
        return finder.uiElements(uiObject);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, By by) {
        return finder.uiElements(uiObject, by);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, SearchContext context) {
        return finder.uiElements(uiObject, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, By by, SearchContext context) {
        return finder.uiElements(uiObject, by, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name) {
        return finder.uiElements(uiObject, name);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, By by) {
        return finder.uiElements(uiObject, name, by);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, SearchContext context) {
        return finder.uiElements(uiObject, name, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, By by, SearchContext context) {
        return finder.uiElements(uiObject, name, by, context);
    } 
}
