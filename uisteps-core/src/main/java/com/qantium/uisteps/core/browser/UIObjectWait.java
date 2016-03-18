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

import com.google.common.base.Function;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;

/**
 * @author Anton Solyankin
 */
public class UIObjectWait extends FluentWait<UIObject> {

    private final UIObject uiObject;

    public UIObjectWait(UIObject uiObject) {
        super(uiObject);
        this.uiObject = uiObject;
        long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
        withTimeout(timeout, TimeUnit.MILLISECONDS).pollingEvery(pollingTime, TimeUnit.MILLISECONDS);
    }

    public void untilIsDisplayed() {

        Function<UIObject, Boolean> condition = new Function<UIObject, Boolean>() {
            @Override
            public Boolean apply(UIObject uiObject) {

                try {
                    return uiObject != null && uiObject.isDisplayed();
                } catch (Exception ex) {
                    return false;
                }
            }
        };

        try {
            until(condition);
        } catch (Exception ex) {

            if (uiObject instanceof UIElement) {
                String locator = ((UIElement) uiObject).getLocatorString();
                throw new RuntimeException(uiObject + "by locator " + locator + " is not displayed!", ex);
            } else {
                throw new RuntimeException(uiObject + " is not displayed!", ex);
            }
        }
    }

    public void untilIsNotDisplayed() {

        Function<UIObject, Boolean> condition = new Function<UIObject, Boolean>() {
            @Override
            public Boolean apply(UIObject uiObject) {

                try {
                    if(uiObject == null || uiObject.isDisplayed()) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    return true;
                }
            }
        };

        try {
            until(condition);
        } catch (Exception ex) {

            if (uiObject instanceof UIElement) {
                String locator = ((UIElement) uiObject).getLocatorString();
                throw new RuntimeException(uiObject + "by locator " + locator + " is still displayed!", ex);
            } else {
                throw new RuntimeException(uiObject + " is still displayed!", ex);
            }
        }
    }
}
