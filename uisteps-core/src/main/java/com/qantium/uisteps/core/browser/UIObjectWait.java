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
package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author A.Solyankin
 */
public class UIObjectWait extends FluentWait<UIObject> {

    public UIObjectWait(UIObject uiObject) {
        super(uiObject);
        long timeout = Integer.parseInt(UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        long pollingTime = Integer.parseInt(UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING));
        withTimeout(timeout, TimeUnit.MILLISECONDS).pollingEvery(pollingTime, TimeUnit.MILLISECONDS);
    }
}