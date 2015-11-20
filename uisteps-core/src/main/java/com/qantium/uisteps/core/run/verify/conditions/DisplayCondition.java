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
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import org.eclipse.aether.util.StringUtils;
import ru.yandex.qatools.ashot.comparison.ImageDiff;

/**
 *
 * @author A.Solyankin
 */
public class DisplayCondition extends ConditionBuilder {

    public DisplayCondition(Browser browser) {
        super(browser);
    }

    public Condition seeEqual(Screenshot screenshot1, Screenshot screenshot2) {
        return seeEqual("", screenshot1, screenshot2);
    }

    public Condition seeEqual(String description, Screenshot screenshot1, Screenshot screenshot2) {

        ImageDiff diff = screenshot1.getDiffFrom(screenshot2);
        boolean noDiff = !diff.hasDiff();

        String expected = "screenshots have no diff";
        StringBuilder actual = new StringBuilder("");

        if (!noDiff) {
            String fromDir = UIStepsProperties.getProperty(SCREENSHOTS_FROM_DIR);
            String diffImage = UUID.randomUUID() + ".png";

            try {
                new Screenshot(diff.getMarkedImage()).save(diffImage);
                String path = fromDir + "/" + diffImage;
                actual.append("diff ")
                        .append("<a target='blank' href='").append(path).append("'>")
                        .append("<img class='screenshot' width='")
                        .append(UIStepsProperties.getProperty(SCREENSHOTS_SCALE_WIDTH))
                        .append("' height='")
                        .append(UIStepsProperties.getProperty(SCREENSHOTS_SCALE_HEIGHT))
                        .append("' src='").append(path)
                        .append("' href='").append(path)
                        .append("'></a>");
            } catch (IOException ex) {
                throw new RuntimeException("Cannot save screenshot diff with name " + diffImage + "\nCause:" + ex);
            }
        }

        if (not) {
            return compile(description, noDiff, actual.toString(), expected, "", "", "");
        } else {
            return compile(description, noDiff, expected, actual.toString(), "", "", "");
        }
    }

    protected Condition see(String description, boolean successful, String expected, String actual) {
        return compile(description, successful, expected, actual, "is", "displayed");
    }

    public Condition see(UIObject obj) {
        return see("", obj);
    }

    public Condition see(Class<? extends UIObject> uiObject) {
        return see("", uiObject);
    }

    public Condition see(String description, UIObject obj) {
        browser.populate(obj);
        return see(description, isDisplayed(obj), quoted(obj), quoted(obj));
    }

    public Condition see(String description, String obj) {
        return see(description, isDisplayed(obj), quoted(obj), quoted(obj));
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

        return see(description, obj.equals(value), quoted(value), quoted(obj));
    }

    public Condition see(String description, UIObject obj, String value) {

        String text = "";

        if (isDisplayed(obj)) {

            if (obj instanceof UIElement) {
                text = ((UIElement) obj).getText();
            } else {
                text = ((Page) obj).getName();
            }
        }

        return see(description, text.equals(value), quoted(value), quoted(obj));
    }

    public Condition see(String description, Class<? extends UIObject> uiObject, String value) {

        UIObject obj = uiObjectInstance(uiObject);

        String text = "";

        if (isDisplayed(obj)) {

            if (obj instanceof UIElement) {
                text = ((UIElement) obj).getText();
            } else {
                text = ((Page) obj).getName();
            }
        }

        return see(description, text.equals(value), quoted(value), quoted(uiObject));
    }

    public Condition seePartOf(UIElement obj, String value) {
        return seePartOf("", obj, value);
    }

    public Condition seePartOf(String obj, String value) {
        return seePartOf("", obj, value);
    }

    public Condition seePartOf(Class<? extends UIElement> uiObject, String value) {
        return seePartOf("", uiObject, value);
    }

    public Condition seePartOf(String description, UIElement obj, String value) {

        String text = "";

        if (isDisplayed(obj)) {
            text = obj.getText();
        }
        String partOf = partOf(value, text);
        return see(description, text.contains(value), partOf, partOf);
    }

    public Condition seePartOf(String description, String obj, String value) {
        String partOf = partOf(value, obj);
        return see(description, obj.contains(value), partOf, partOf);
    }

    public Condition seePartOf(String description, Class<? extends UIElement> uiObject, String value) {
        String text = uiObjectInstance(uiObject).getText();
        String partOf = partOf(value, text);
        return see(description, text.contains(value), partOf, partOf);
    }

    protected <T extends UIObject> T uiObjectInstance(Class<T> uiObject) {
        return browser.displayed(uiObject);
    }

    protected String partOf(String part, String of) {

        StringBuilder partOf = new StringBuilder();

        partOf
                .append("part ")
                .append(quoted(part))
                .append(" of ")
                .append(quoted(of));

        return partOf.toString();
    }

    protected String quoted(Object value) {

        StringBuilder quoted = new StringBuilder();

        if (value != null && !StringUtils.isEmpty(value.toString())) {
            quoted.append("\"").append(value).append("\"");
        }
        return quoted.toString();
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
