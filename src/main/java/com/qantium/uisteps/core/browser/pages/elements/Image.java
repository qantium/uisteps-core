/*
 * Copyright 2015 A.Solyankin.
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
 * @author A.Solyankin
 */
@NotInit
public class Image extends UIElement {
    
    public String getSource() {
        return getWrappedElement().getAttribute("src");
    }
    
    public String getAlt() {
        return getWrappedElement().getAttribute("alt");
    }
    
    public int getWidth() {
        return getWrappedElement().getSize().width;
    }
    
    public int getHeight() {
        return getWrappedElement().getSize().height;
    }
}
