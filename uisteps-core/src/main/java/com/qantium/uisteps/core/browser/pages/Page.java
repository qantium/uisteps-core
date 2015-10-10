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

/**
 *
 * @author ASolyankin
 */
@Root
public abstract class Page implements UIObject, Named {

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

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    protected <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    protected <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    protected Object executeScript(String script) {
        return inOpenedBrowser().executeScript(script);
    }

    public String getTitle() {
        return inOpenedBrowser().getCurrentTitle();
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

    protected <T extends WrapsElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by);
    }

    protected <T extends WrapsElement> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by);
    }

    public void refresh() {
        inOpenedBrowser().refreshCurrentPage();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Page setName(String name) {
        this.name = name;
        return this;
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

}
