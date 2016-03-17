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
import org.apache.commons.lang3.StringUtils;

/**
 * @author ASolyankin
 */
@NotInit
@Root
public class Page extends HtmlUIObject {

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

    @Override
    public boolean isDisplayed() {
        return executeScript("return document.readyState").equals("complete");
    }

    @Override
    public void afterInitialization() {
        waitUntilIsDisplayed();
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
}
