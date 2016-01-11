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
package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 *
 * @author ASolyankin
 */
@NotInit
public class TextField extends UIElement {

    public Object sendKeys(String keys) {
        return type(keys);
    }

    public Object type(String keys) {
        inOpenedBrowser().typeInto(this, keys);
        return null;
    }

    public Object clear() {
        inOpenedBrowser().clear(this);
        return null;
    }

    public Object enter(String text) {
        inOpenedBrowser().enterInto(this, text);
        return null;
    }
    
    public boolean isEnabled() {
        return getWrappedElement().isEnabled();
    }
}
