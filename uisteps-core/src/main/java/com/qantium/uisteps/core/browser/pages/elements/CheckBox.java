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

import com.qantium.uisteps.core.browser.pages.ElementaryElement;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
public class CheckBox extends UIElement implements ElementaryElement {
    
    public CheckBox() {
    }
    
    public CheckBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public String getLabelText() {
        return getWrappedCheckBox().getLabelText();
    }

    public Object select() {
        inOpenedBrowser().select(this);
        return null;
    }

    public Object deselect() {
        inOpenedBrowser().deselect(this);
        return null;
    }
    
    public ru.yandex.qatools.htmlelements.element.CheckBox getWrappedCheckBox() {
        return new ru.yandex.qatools.htmlelements.element.CheckBox(getWrappedElement());
    }
    
    public boolean isEnabled() {
        return getWrappedElement().isEnabled();
    }
    
    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }
}
