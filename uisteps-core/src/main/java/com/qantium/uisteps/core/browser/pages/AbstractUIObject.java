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

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;

/**
 *
 * @author A.Solyankin
 */
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private boolean populated;

    public Browser inOpenedBrowser() {
        return BrowserManager.getCurrentBrowser();
    }

    @Override
    public boolean isPopulated() {
        return populated;
    }

    @Override
    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public <T extends Named> T withName(String name) {
        setName(name);
        return (T) this;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {

        if (StringUtils.isEmpty(name)) {
            setName(NameConvertor.humanize(getClass()));
        }

        return name;
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    public <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

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
            return (T) inOpenedBrowser().onDisplayed((Class<UIElement>) uiObject, this);
        }
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, this);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }

    public <T extends UIElement> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject, this);
    }

    public <T extends UIElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, this);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject, this);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, this);
    }
}
