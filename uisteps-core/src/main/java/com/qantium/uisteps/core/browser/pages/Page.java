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

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.PageDisplayWaiting;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Anton Solyankin
 */
@NotInit
@Root
public class Page extends HtmlObject {

    private Url url = new Url();

    public Page() {
        setName("");
    }

    public Url getUrl() {
        return url;
    }

    public String getTitle() {
        return inOpenedBrowser().getCurrentTitle();
    }

    public <T extends Page> T setUrl(Url url) {
        this.url = url;
        return (T) this;
    }

    @Override
    public String toString() {
        String pageUrl = getUrl().toString();

        if (StringUtils.isEmpty(pageUrl)) {
            pageUrl = inOpenedBrowser().getDriver().getCurrentUrl();
        }
        return getName() + " by url " + pageUrl;
    }

    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot();
    }

    @Override
    public List<WebElement> findElements(By locator) {
        return inOpenedBrowser().findElements(locator);
    }

    @Override
    public WebElement findElement(By locator) {
        return inOpenedBrowser().findElement(locator);
    }

    @Override
    public SearchContext getSearchContext() {
        return inOpenedBrowser().getDriver();
    }

    @Override
    protected Waiting getDisplayWaiting() {
        return new PageDisplayWaiting(this);
    }
}
