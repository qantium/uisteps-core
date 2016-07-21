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
package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIObject;

/**
 * @author Anton Solyankin
 */
public class Wait {

    private final UIObject uiObject;

    public Wait(UIObject uiObject) {
        this.uiObject = uiObject;
    }


    public void untilIsDisplayed() throws IsNotDisplayException {

        Waiting waiting = new Waiting() {
            @Override
            public boolean until() {
                try {
                    return uiObject.isDisplayed();
                } catch (Exception ex) {
                     return false;
                }
            }
        };

        try {
            waiting.apply();
        } catch (WaitingException ex) {
            throw new IsNotDisplayException(uiObject, ex);
        }
    }

    public void untilIsNotDisplayed() throws IsDisplayException {

        Waiting waiting = new Waiting() {
            @Override
            public boolean until() {
                try {
                    return !uiObject.isDisplayed();
                } catch (Exception ex) {
                    return true;
                }
            }
        };

        try {
            waiting.apply();
        } catch (WaitingException ex) {
            throw new IsDisplayException(uiObject, ex);
        }
    }
}
