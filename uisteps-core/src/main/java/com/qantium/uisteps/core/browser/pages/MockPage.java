/*
 * Copyright 2015 ASolyankin.
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

/**
 *
 * @author ASolyankin
 */
public class MockPage extends Page {

    private final Browser browser;

    public MockPage(String name, Url url, Browser browser) {
        super(url, name);
        this.browser = browser;

    }

    public <T extends MockPage> T open() {
        browser.getDriver().get(getUrl().toString());

        if (!browser.isOpened()) {
            browser.open();
        }
        return (T) this;
    }

    @Override
    public Browser inOpenedBrowser() {
        return browser;
    }

    @Override
    public String toString() {
        return getName() + " by url <a href='" + getUrl() + "' target='blank'>" + getUrl() + "</a> with title " + getTitle();
    }

    @Override
    public MockPage setParams(String[] params) {
        return (MockPage) super.setParams(params);
    }

}
