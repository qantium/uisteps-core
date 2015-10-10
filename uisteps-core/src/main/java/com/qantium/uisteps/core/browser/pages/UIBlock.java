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
package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.Named;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 *
 * @author ASolyankin
 */
public abstract class UIBlock extends HtmlElement implements UIObject {

    public UIBlock() {
    }

    public UIBlock(By locator) {
        setWrappedElement(findElement(locator));
    }

    @Override
    public void click() {
        inOpenedBrowser().click(this);
    }

    public Object afterClick() {
        inOpenedBrowser().click(this);
        return this;
    }

    public Object moveMouseOver() {
        inOpenedBrowser().moveMouseOver(this);
        return this;
    }

    public Object clickOnPoint(int x, int y) {
        inOpenedBrowser().clickOnPoint(this, x, y);
        return this;
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    public <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
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

    public <T extends WrapsElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, getWrappedElement());
    }

    public <T extends Named & WrapsElement> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by, getWrappedElement());
    }

    public <T extends WrapsElement> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, getWrappedElement());
    }

    public <T extends Named & WrapsElement> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by, getWrappedElement());
    }

}
