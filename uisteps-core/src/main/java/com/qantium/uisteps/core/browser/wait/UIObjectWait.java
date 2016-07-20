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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

/**
 * @author Anton Solyankin
 */
public class UIObjectWait {//extends Waiting {

    private final UIObject uiObject;

    public UIObjectWait(UIObject uiObject) {
       // super(uiObject.inOpenedBrowser());
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
            waiting.applay();
        } catch (WaitingException ex) {
            throw new IsNotDisplayException(uiObject, ex);
        }

//        try {
//            until(new ExpectedCondition<Boolean>() {
//                @Nullable
//                @Override
//                public Boolean apply(WebDriver driver) {
//                    try {
//                        return uiObject.isDisplayed();
//                    } catch (Exception ex) {
//                        return false;
//                    }
//                }
//            });

//        } catch (Exception ex) {
//            throw new DisplayException(uiObject, ex);
//        }
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
            waiting.applay();
        } catch (WaitingException ex) {
            throw new IsDisplayException(uiObject, ex);
        }

////        try {
//            until(new ExpectedCondition<Boolean>() {
//                @Nullable
//                @Override
//                public Boolean apply(WebDriver driver) {
//                    try {
//                        return !uiObject.isDisplayed();
//                    } catch (Exception ex) {
//                        return true;
//                    }
//                }
//            });
//        } catch (Exception ex) {
//            throw new NotDisplayException(uiObject, ex);
//        }
    }
}
