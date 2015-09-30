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

import com.qantium.uisteps.core.name.Name;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 *
 * @author ASolyankin
 */
@Root
public abstract class Page implements UIObject, Named {

    private Url url;
    private UrlFactory urlFactory;
    private Name name;
    public static final String DEFAULT_NAME = "page";

    public Page(UrlFactory urlFactory, Name name) {
        this.urlFactory = urlFactory;
        this.name = name;
    }

    public Page(UrlFactory urlFactory) {
        this(urlFactory, new Name(DEFAULT_NAME));
    }

    public Page(Url url, Name name) {
        this.url = url;
        this.name = name;
    }

    public Page(Url url) {
        this(url, new Name(DEFAULT_NAME));
    }

    public Url getUrl() {

        if (url == null) {

            if (urlFactory != null) {
                url = urlFactory.getUrlOf(this.getClass());
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

    public <T extends UIObject> T on(Class<T> uiObject) {
        return uiObject.cast(this);
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

    public void refresh() {
        inOpenedBrowser().refreshCurrentPage();
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Page setName(String name) {
        this.name.setValue(name);
        return this;
    }

    public Page setUrl(Url url) {
        this.url = url;
        return this;
    }
    
    @Override
    public String toString() {
        return getName() + " by url <a href='" + this.getUrl() + "' target='blank'>" + this.getUrl() + "</a> with title " + this.getTitle();
    }

}
