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

import com.qantium.uisteps.core.browser.*;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * @author Anton Solyankin
 */

public abstract class HtmlObject extends AbstractUIObject implements ScriptExecutor, IUIObjectFactory, ISearchContext, SearchContext, WithSearchContext {


    @Override
    public Object executeScript(String script, Object... args) {
        return inOpenedBrowser().executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return inOpenedBrowser().executeAsyncScript(script, args);
    }

    @Override
    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    @Override
    public UIElement onDisplayed(By locator) {
        return onDisplayed(get(locator));
    }

    @Override
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(get(uiObject));
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By locator) {
        return onDisplayed(get(uiObject, locator));
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject) {
        return onDisplayed(getAll(uiObject));
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By locator) {
        return onDisplayed(getAll(uiObject, locator));
    }

    @Override
    public UIElement get(By locator) {
        return getUIObjectFactory().get(UIElement.class, getChildContext(), locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        if (UIElement.class.isAssignableFrom(uiObject)) {
            return (T) getUIObjectFactory().get((Class<UIElement>) uiObject, getChildContext());
        } else if (Page.class.isAssignableFrom(uiObject) || Alert.class.isAssignableFrom(uiObject)) {
            return inOpenedBrowser().get(uiObject);
        } else {
            throw new IllegalArgumentException("Cannot get " + uiObject + " from HtmlObject! Only Alerts and HtmlObjects are allowed!");
        }
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By locator) {
        return getUIObjectFactory().get(uiObject, getChildContext(), locator);
    }

    private UIObjectFactory getUIObjectFactory() {
        return new UIObjectFactory(inOpenedBrowser());
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject) {
        return getUIObjectFactory().getAll(uiObject, getChildContext());
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By locator) {
        return getUIObjectFactory().getAll(uiObject, getChildContext(), locator);
    }

    public abstract Screenshot takeScreenshot();


    protected <T extends UIObject> HtmlObject getChildContext() {
        return this;
    }

}
