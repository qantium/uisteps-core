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
package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.UIObjectFactory;
import com.qantium.uisteps.core.browser.pages.UIObjectInitializer;
import com.qantium.uisteps.core.name.Named;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author A.Solyankin
 */
public class Finder {

    private final Browser browser;

    public Finder(Browser browser) {
        this.browser = browser;
    }

    public <T extends UIObject> T find(Class<T> uiObject) {
        return find(uiObject, browser.getLocatorFactory().getLocator(uiObject));
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by) {
        return find(uiObject, by, browser.getDriver());
    }

    public <T extends UIObject> T find(Class<T> uiObject, SearchContext context) {
        return find(uiObject, browser.getLocatorFactory().getLocator(uiObject), context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by, SearchContext context) {
        return browser.displayed(uiObject, context.findElement(by));
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name) {
        return find(uiObject).withName(name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by) {
        return find(uiObject, by).withName(name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, SearchContext context) {
        return find(uiObject, context).withName(name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by, SearchContext context) {
        return find(uiObject, by, context).withName(name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject) {
        return findAll(uiObject, browser.getLocatorFactory().getLocator(uiObject));
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by) {
        return findAll(uiObject, by, browser.getDriver());
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, SearchContext context) {
        return findAll(uiObject, browser.getLocatorFactory().getLocator(uiObject), context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by, SearchContext context) {

        List<WebElement> elements = context.findElements(by);
        List<T> uiObjects = new ArrayList();

        for (WebElement element : elements) {
            uiObjects.add(browser.displayed(uiObject, element));
        }
        return uiObjects;
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name) {
        return withName(findAll(uiObject), name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by) {
        return withName(findAll(uiObject, by), name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, SearchContext context) {
        return withName(findAll(uiObject, context), name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return withName(findAll(uiObject, by, context), name);
    }

    protected <T extends UIObject> List<T> withName(List<T> uiObjects, String name) {

        for (Named uiObject : uiObjects) {
            uiObject.withName(name);
        }
        return uiObjects;
    }
}
