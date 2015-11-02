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

    public Browser inOpenedBrowser() {
        return BrowserManager.getCurrentBrowser();
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

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    protected Object executeScript(String script) {
        return inOpenedBrowser().executeScript(script);
    }

    protected void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    protected void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    protected void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    protected void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }

    //onDisplayed
    protected <T extends UIElement> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject, this);
    }

    protected <T extends UIObject> T onDisplayed(Class<T> uiObject) {

        if (Page.class.isAssignableFrom(uiObject)) {
            return inOpenedBrowser().onDisplayed(uiObject);
        } else {
            return (T) inOpenedBrowser().onDisplayed((Class<UIElement>) uiObject, this);
        }
    }

    protected <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, this);
    }

    protected <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    protected <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }

    protected <T extends UIElement> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject, this);
    }

    protected <T extends UIElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, this);
    }

    protected <T extends UIElement> UIElements<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject, this);
    }

    protected <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, this);
    }

}
