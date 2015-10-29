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
package com.qantium.uisteps.core.run.verify.conditions;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIBlockOrElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import java.util.Collection;
import org.eclipse.aether.util.StringUtils;

/**
 *
 * @author A.Solyankin
 */
public class DisplayCondition {

    public final Browser browser;
    private boolean not = false;

    public DisplayCondition(Browser browser) {
        this.browser = browser;
    }

    public DisplayCondition not(boolean not) {
        this.not = not;
        return this;
    }

    public DisplayCondition not(String not) {
        return not(Boolean.valueOf(not));
    }

    protected Condition see(String description, boolean successful, String expected, String actual) {
        return see(description, successful, expected, expected, actual);
    }

    protected Condition see(String description, boolean successful, String expected, String altExpected, String actual) {
        String message = " is displayed";

        if (!StringUtils.isEmpty(description)) {
            description += " ";
        } else {
            description = "";
        }

        expected = description + expected + message;
        actual = description + actual + message;
        altExpected = description + altExpected + message;
        return Condition.isTrue(not, successful, expected, altExpected, actual);
    }

    public Condition see(UIObject obj) {
        return see("", obj);
    }

    public Condition see(Class<? extends UIObject> uiObject) {
        return see("", uiObject);
    }

    protected String getAltExpected() {

        if (not) {
            return "nothing";
        } else {
            return "some object";
        }
    }

    public Condition see(String description, UIObject obj) {
        browser.populate(obj);
        return see(description, isDisplayed(obj), "\"" + obj + "\"", getAltExpected(), "\"" + obj + "\"");
    }

    public Condition see(String description, String obj) {
        return see(description, isDisplayed(obj), "\"" + obj + "\"", getAltExpected(), "\"" + obj + "\"");
    }

    public Condition see(String description, Class<? extends UIObject> uiObject) {
        return see(description, uiObjectInstance(uiObject));
    }

    public Condition see(UIObject uiObject, String value) {
        return see("", uiObject, value);
    }

    public Condition see(Class<? extends UIObject> uiObject, String value) {
        return see("", uiObject, value);
    }

    public Condition see(String description, String obj, String value) {

        if (!isDisplayed(obj)) {
            obj = "";
        }

        return see(description, isDisplayed(obj) && obj.equals(value), "\"" + value + "\"", "\"" + obj + "\"");
    }

    public Condition see(String description, UIObject obj, String value) {

        String text = "";

        if (isDisplayed(obj)) {

            if (obj instanceof UIBlockOrElement) {
                text = ((UIBlockOrElement) obj).getText();
            } else {
                text = ((Page) obj).getName();
            }
        }

        return see(description, isDisplayed(obj) && text.equals(value), "\"" + value + "\"", "\"" + obj + "\"");
    }

    public Condition see(String description, Class<? extends UIObject> uiObject, String value) {

        UIObject obj = uiObjectInstance(uiObject);

        String text = "";

        if (isDisplayed(obj)) {

            if (obj instanceof UIBlockOrElement) {
                text = ((UIBlockOrElement) obj).getText();
            } else {
                text = ((Page) obj).getName();
            }
        }

        return see(description, text.equals(value), "\"" + value + "\"", "\"" + uiObject + "\"");
    }

    public Condition seePartOf(UIBlockOrElement obj, String value) {
        return seePartOf("", obj, value);
    }

    public Condition seePartOf(String obj, String value) {
        return seePartOf("", obj, value);
    }

    public Condition seePartOf(Class<? extends UIBlockOrElement> uiObject, String value) {
        return see("", uiObject, value);
    }

    public Condition seePartOf(String description, UIBlockOrElement obj, String value) {
        String text = "";

        if (isDisplayed(obj)) {
            text = obj.getText();
        }

        return see(description, isDisplayed(obj) && text.contains(value), "part \"" + value + "\" of \"" + text + "\"", "\"" + obj + "\"");
    }

    public Condition seePartOf(String description, String obj, String value) {

        if (!isDisplayed(obj)) {
            obj = "";
        }

        return see(description, isDisplayed(obj) && obj.contains(value), "part \"" + value + "\" of \"" + obj + "\"", "\"" + obj + "\"");
    }

    public Condition seePartOf(String description, Class<? extends UIBlockOrElement> uiObject, String value) {
        String text = uiObjectInstance(uiObject).getText();
        return see(description, text.contains(value), "part \"" + value + "\" of \"" + text + "\"", "\"" + uiObject + "\"");
    }

    protected <T extends UIObject> T uiObjectInstance(Class<T> uiObject) {
        return browser.displayed(uiObject);
    }

    public static boolean isDisplayed(Object obj) {

        if (obj == null) {
            return false;
        } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return false;
        } else if (obj instanceof UIObject && !((UIObject) obj).isDisplayed()) {
            return false;
        } else if (StringUtils.isEmpty(obj.toString())) {
            return false;
        }

        return true;

    }

}
