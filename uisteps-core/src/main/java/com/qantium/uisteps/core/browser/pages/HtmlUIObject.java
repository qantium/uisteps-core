/*
 * Copyright 2015 A.Solyankin.
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
package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author A.Solyankin
 */

public abstract class HtmlUIObject extends AbstractUIObject {

    public Object executeScript(String script) {
        return inOpenedBrowser().executeScript(script);
    }

    public void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }

    //onDisplayed
    public <T extends UIElement> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject, this);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {

        if (Page.class.isAssignableFrom(uiObject)) {
            return inOpenedBrowser().onDisplayed(uiObject);
        } else {
            return (T) inOpenedBrowser().onDisplayed(this, (Class<UIElement>) uiObject);
        }
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(this, uiObject, by);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(this, uiObject);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(this, uiObject, by);
    }

    public <T extends UIElement> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(this, uiObject);
    }

    public <T extends UIElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(this, uiObject, by);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(this, uiObject);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(this, uiObject, by);
    }
}
