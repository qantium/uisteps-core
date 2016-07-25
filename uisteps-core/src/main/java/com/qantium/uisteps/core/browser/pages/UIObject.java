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
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.then.Then;
import org.openqa.selenium.SearchContext;

/**
 * Any element (Page, UIElement or Alert) of user interface
 *
 * @author Anton Solyankin
 * @see com.qantium.uisteps.core.browser.pages.Page
 * @see com.qantium.uisteps.core.browser.pages.UIElement
 * @see com.qantium.uisteps.core.browser.pages.UIElements
 */
@NotInit
public interface UIObject extends Named, Visible {

    void setBrowser(Browser browser);

    Browser inOpenedBrowser();

    <T extends UIObject> Then<T> then(Class<T> uiObject);

    void afterInitialization();
}
