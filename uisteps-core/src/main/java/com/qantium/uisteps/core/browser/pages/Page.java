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

import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Screenshot;
import java.util.List;
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
    public static final String DEFAULT_NAME = "page";

    public Page() {
        url = UrlFactory.getUrlOf(getClass());
        setName(DEFAULT_NAME);
    }

    public Url getUrl() {
        return url;
    }

    public String getTitle() {
        return inOpenedBrowser().getCurrentTitle();
    }

    @Override
    public boolean isDisplayed() {
        return executeScript("return document.readyState").equals("complete");
    }

    public <T extends Page> T setUrl(Url url) {
        this.url = url;
        return (T) this;
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
                .append("</a>");

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

    //Screenshots
    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot();
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }

    public Screenshot takeScreenshot(Ignored... elements) {
        return inOpenedBrowser().takeScreenshot(elements);
    }

}
