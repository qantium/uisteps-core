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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
@Root
public class Page extends AbstractUIObject {

    private Url url;
    private final UrlFactory urlFactory = new UrlFactory();
    private String[] params = new String[0];
    public static final String DEFAULT_NAME = "page";

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    public String[] getParams() {
        return params;
    }

    public <T extends Page> T setParams(String[] params) {
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

    public String getTitle() {
        return inOpenedBrowser().getCurrentTitle();
    }

    @Override
    public boolean isDisplayed() {
        return executeScript("return document.readyState").equals("complete");
    }

    public void refresh() {
        inOpenedBrowser().refreshCurrentPage();
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
                .append(getName())
                .append(" by url <a href='")
                .append(getUrl())
                .append("' target='blank'>")
                .append(getUrl())
                .append("</a> with title ")
                .append(getTitle());

        return nameBuider.toString();
    }

    @Override
    public SearchContext getSearchContext() {
        return inOpenedBrowser().getDriver();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return inOpenedBrowser().getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return inOpenedBrowser().getDriver().findElement(by);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        
        final Page other = (Page) obj;
        
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        
        return Arrays.deepEquals(this.params, other.params);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.url);
        hash = 11 * hash + Arrays.deepHashCode(this.params);
        return hash;
    }

}
