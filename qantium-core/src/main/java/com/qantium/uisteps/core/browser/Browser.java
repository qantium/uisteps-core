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

        try {
            WebElement webElement = element.getWrappedElement();
            String attrTarget = webElement.getAttribute("target");

            webElement.click();

            if (attrTarget != null && !attrTarget.equals("") && !attrTarget.equals("_self")) {
                switchToNextWindow();
            }
        } catch (Exception ex) {
            throw new AssertionError("Cannot click " + element + "! " + ex);
        }
    }

    public void clickOnPoint(WrapsElement element, int x, int y) {

        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element.getWrappedElement(), x, y).click().build().perform();

        } catch (Exception ex) {
            throw new AssertionError("Cannot click " + element + "on point (" + x + "; " + y + ") !\n" + ex);
        }
    }

    public void moveMouseOver(WrapsElement element) {

        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element.getWrappedElement()).build().perform();
        } catch (Exception ex) {
            throw new AssertionError("Cannot move mouse over " + element + "!\n" + ex);
        }
    }

    public void typeInto(WrapsElement input, String text) {

        try {
            input.getWrappedElement().sendKeys(text);
        } catch (Exception ex) {
            throw new AssertionError("Cannot type " + text + " into " + input + "!\n" + ex);
        }
    }

    public void clear(WrapsElement input) {

        try {
            input.getWrappedElement().clear();
        } catch (Exception ex) {
            throw new AssertionError("Cannot clear " + input + "!\n" + ex);
        }
    }

    public void enterInto(WrapsElement input, String text) {

        try {
            input.getWrappedElement().clear();
            input.getWrappedElement().sendKeys(text);
        } catch (Exception ex) {
            throw new AssertionError("Cannot enter " + text + " into " + input + "!" + ex);
        }
    }

    public String getTextFrom(WrapsElement input) {
        try {

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

        } catch (Exception ex) {
            throw new AssertionError("Cannot get text from " + input + "\n" + ex);
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

        try {
            select.getWrappedSelect().deselectAll();
        } catch (Exception ex) {
            throw new AssertionError("Cannot deselect all values in " + select + "\nCause:" + ex);
        }
    }

    public void deselect(Option option) {
        option.deselect();
    }

    //Radio button
    public void select(RadioButton button) {
        try {
            if (!button.isSelected()) {
                button.click();
            }
        } catch (Exception ex) {
            throw new AssertionError("Cannot select radio button " + button + "\nCause:" + ex);
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