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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final UIObjectFactory uiObjectFactory;
    private final UIObjectInitializer initializer;
    private final WindowList windowList;
    private boolean opened;

    public Browser(WebDriver driver, UIObjectFactory uiObjectFactory, UIObjectInitializer initializer) {
        this.driver = driver;
        this.uiObjectFactory = uiObjectFactory;
        this.initializer = initializer;
        windowList = new WindowList(driver);
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

    public <T extends WrapsElement> T getUIObject(Class<T> uiObject, WebElement withWebElement) {
        T uiObjectInstance = uiObjectFactory.instatiate(uiObject, withWebElement);
        initializer.initializeWithSearchContext(uiObjectInstance);
        return uiObjectInstance;
    }

    public <T extends WrapsElement> T find(Class<T> uiObject, By by) {
        WebElement element = driver.findElement(by);
        return getUIObject(uiObject, element);
    }

    public <T extends WrapsElement> List<T> findAll(Class<T> uiObject, By by) {
        List<WebElement> elements = driver.findElements(by);
        List<T> uiObjects = new ArrayList();

        for (WebElement element : elements) {
            uiObjects.add(getUIObject(uiObject, element));
        }
        return uiObjects;
    }

    public void openUrl(String url) {
        try {
            open(new Url(url));
        } catch (MalformedURLException ex) {
            throw new AssertionError("Cannot open url " + url + "\nCause:" + ex);
        }
    }

    public void open(Url url) {
        open(new MockPage("page", url, this));
    }

    public <T extends Page> T open(Class<T> page, Url url) {
        return open(uiObjectFactory.instatiate(page), url);
    }

    public <T extends Page> T open(T page, Url url) {
        page.setUrl(url);
        return open(page);
    }

    public <T extends Page> T open(Class<T> page) {
        return open(uiObjectFactory.instatiate(page));
    }

    public <T extends Page> T open(T page) {
        open(new MockPage(page.getName().toString(), page.getUrl(), this));
        initializer.initialize(page);
        return page;
    }

    protected void open(MockPage page) {

    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(uiObjectFactory.instatiate(uiObject));
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        initializer.initialize(uiObject);
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
        windowList.switchToNextWindow();
    }

    public void switchToNextWindow() {
        windowList.switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        windowList.switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        windowList.switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        windowList.switchToWindowByIndex(index);
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

    public WindowList getWindowList() {
        return windowList;
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
            button.click();
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

}
