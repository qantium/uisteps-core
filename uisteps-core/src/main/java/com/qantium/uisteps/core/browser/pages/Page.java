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

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
@Root
public class Page implements UIObject {

    private Url url;
    private UrlFactory urlFactory;
    private String name;
    private String[] params = new String[0];
    public static final String DEFAULT_NAME = "page";

    public Page(UrlFactory urlFactory, String name) {
        this.urlFactory = urlFactory;
        this.name = name;
    }

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    public void setUrlFactory(UrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public String[] getParams() {
        return params;
    }

    public Page setParams(String[] params) {
        this.params = params;
        return this;
    }

    public Page(UrlFactory urlFactory) {
        this(urlFactory, DEFAULT_NAME);
    }

    public Page(Url url, String name) {
        this.url = url;
        this.name = name;
    }

    public Page(Url url) {
        this(url, DEFAULT_NAME);
    }

    public Url getUrl() {

        if (url == null) {

            if (urlFactory != null) {
                url = urlFactory.getUrlOf(this.getClass(), params);
            } else {
                throw new RuntimeException("Url and url factory are not set!");
            }
        }

        return url;
    }

    @Override
    public boolean isDisplayed() {
        return executeScript("return document.readyState").equals("complete");
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

    public String getTitle() {
        return inOpenedBrowser().getCurrentTitle();
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

    public void refresh() {
        inOpenedBrowser().refreshCurrentPage();
    }

    @Override
    public <T extends UIObject> T as(Class<T> uiObject) {
        return inOpenedBrowser().displayed(uiObject);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Page setUrl(Url url) {
        this.url = url;
        return this;
    }

    public String getUrlString() {
        return getUrl().toString();
    }

    @Override
    public String toString() {
        return getName() + " by url <a href='" + getUrl() + "' target='blank'>" + getUrl() + "</a> with title " + getTitle();
    }

    @Override
    public WebElement getWrappedElement() {
        throw new UnsupportedOperationException("Page does not contain wrapped element!");
    }

    @Override
    public String getName() {

        if (name.equals(Page.DEFAULT_NAME)) {
            setName(NameConvertor.humanize(getClass()));
        }

        return name;
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, context);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, by, context);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayed(uiObject, name);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, name, context);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by, context);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    //Find 
    public <T extends UIObject> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().find(uiObject, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, by, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name) {
        return inOpenedBrowser().find(uiObject, name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().findAll(uiObject, name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, by, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(List<T> proxyElements) {
        return inOpenedBrowser().uiElements(proxyElements);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject) {
        return inOpenedBrowser().uiElements(uiObject);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, By by) {
        return inOpenedBrowser().uiElements(uiObject, by);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, by, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name) {
        return inOpenedBrowser().uiElements(uiObject, name);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().uiElements(uiObject, name, by);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, name, context);
    }

    public <T extends UIObject> UIElements<T> uiElements(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().uiElements(uiObject, name, by, context);
    }
}
