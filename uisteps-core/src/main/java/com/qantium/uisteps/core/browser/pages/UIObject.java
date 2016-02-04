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

import com.google.common.base.Function;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.UIObjectWait;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.screenshots.Screenshot;

import java.util.List;

import com.qantium.uisteps.core.then.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Any element (Page or UIElement) of user interface
 *
 * @author ASolyankin
 * @see com.qantium.uisteps.core.browser.pages.Page
 * @see com.qantium.uisteps.core.browser.pages.UIElement
 * @see com.qantium.uisteps.core.browser.pages.UIElements
 */
@NotInit
public interface UIObject extends Named, SearchContext {

    boolean isDisplayed();

    void setBrowser(Browser browser);

    Browser inOpenedBrowser();

     <T extends UIObject> Then<T> then(Class<T> uiObject);

    <T> Then<T> then(T value);

    void waitUntilIsDisplayed(UIObject uiObject);

    void waitUntilIsDisplayed();

    void waitUntil(UIObject uiObject, Function<UIObject, Boolean> condition);

    void waitUntil(Function<UIObject, Boolean> condition);

    void afterInitialization();

    SearchContext getSearchContext();

    Screenshot takeScreenshot();
}
