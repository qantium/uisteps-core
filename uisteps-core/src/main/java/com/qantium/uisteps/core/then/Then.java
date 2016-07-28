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
package com.qantium.uisteps.core.then;

import com.qantium.uisteps.core.browser.ISearchContext;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.By;

/**
 *
 * @author Anton Solyankin
 */
public class Then<T extends UIObject> {

    private final Class<T> uiObject;
    private final By locator;
    private final ISearchContext context;

    public Then(Class<T> uiObject, ISearchContext context) {
        this(uiObject, context, null);
    }

    public Then(Class<T> uiObject, ISearchContext context, By locator) {
        this.uiObject = uiObject;
        this.context = context;
        this.locator = locator;
    }

    public T then() {
        if(locator != null) {
            return (T) context.onDisplayed((Class<UIElement>) uiObject, locator);
        } else {
            return context.onDisplayed(uiObject);
        }
    }
}