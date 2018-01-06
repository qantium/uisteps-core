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
import com.qantium.uisteps.core.data.DataContainer;
import com.qantium.uisteps.core.data.DataFillable;

/**
 * @author ASolyankin
 */
@NotInit
public class CheckBox extends UIElement implements DataContainer, DataFillable {

    public Object select() {
        return inOpenedBrowser().select(this);
    }

    public Object deselect() {
        return inOpenedBrowser().deselect(this);
    }

    public boolean isSelected() {
        return inOpenedBrowser().isSelected(this);
    }

    public Object select(boolean flag) {
        return inOpenedBrowser().select(this, flag);
    }

    public Object select(String flag) {
        return select(Boolean.valueOf(flag));
    }

    @Override
    public String getData() {
        return Boolean.toString(isSelected());
    }

    @Override
    public void setData(Object rawData) {
        select(rawData.toString());
    }
}
