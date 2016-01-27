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
package com.qantium.uisteps.core.browser.pages.elements.scroll;

import com.qantium.uisteps.core.browser.NotInit;
import org.openqa.selenium.internal.WrapsElement;


/**
 *
 * @author A.Solyankin
 */
@NotInit
public class VerticalScroll extends Scroll {

    public Object scroll(int pixels) {
        inOpenedBrowser().verticalScroll(this, pixels);
        return null;
    }
    
    @Override
    public Object scrollTo(WrapsElement target) {
        inOpenedBrowser().verticalScrollToTarget(this, target);
        return null;
    }
}