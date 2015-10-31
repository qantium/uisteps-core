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

import com.qantium.uisteps.core.name.NameConvertor;
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

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    public void setUrlFactory(UrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public String[] getParams() {
        return params;
    }

    public <T extends Page> T  setParams(String[] params) {
        this.params = params;
        return (T) this;
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
    public void setName(String name) {
        this.name = name;
    }

    public <T extends Page> T setUrl(Url url) {
        this.url = url;
        return (T) this;
    }

    public String getUrlString() {
        return getUrl().toString();
    }

    @Override
    public String toString() {
        StringBuilder nameBuider = new StringBuilder();

        nameBuider
                .append("\"").append(getName()).append("\"")
                .append(" by url <a style='text-decoration: underline' href='")
                .append(getUrl())
                .append("' target='blank'>")
                .append(getUrl())
                .append("</a> with title ")
                .append("\"").append(getTitle()).append("\"");

        return nameBuider.toString();
    }

    @Override
    public SearchContext getSearchContext() {
        return inOpenedBrowser().getDriver();
    }

    @Override
    public String getName() {

        if (name.equals(Page.DEFAULT_NAME)) {
            setName(NameConvertor.humanize(getClass()));
        }

        return name;
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
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    protected <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }

    protected List<WebElement> findElements(By by) {
        return inOpenedBrowser().getDriver().findElements(by);
    }
}
