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
package com.qantium.uisteps.core.verify.conditions;

import com.qantium.uisteps.core.NotDisplayException;
import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.storage.Saved;
import java.io.File;
import java.util.Collection;
import java.util.UUID;
import org.eclipse.aether.util.StringUtils;
import ru.yandex.qatools.ashot.comparison.ImageDiff;

/**
 *
 * @author Anton Solyankin
 */
public class DisplayCondition extends ConditionBuilder {

    public DisplayCondition(Browser browser) {
        super(browser);
    }

    public Condition seeEqual(Screenshot screenshot, File file) {
        return seeEqual("", screenshot, file);
    }

    public Condition seeEqual(String description, Screenshot screenshot, File file) {
        try {
            return seeEqual(description, screenshot, Screenshot.getFrom(file));
        } catch (Exception ex) {
            throw new RuntimeException("Cannot get file!\nCause: " + ex);
        }
    }

    public Condition seeEqual(Screenshot screenshot1, Screenshot screenshot2) {
        return seeEqual("", screenshot1, screenshot2);
    }

    public Condition seeEqual(String description, Screenshot screenshot1, Screenshot screenshot2) {

        StringBuilder expected = new StringBuilder("screanshots ");
        String format = "png";

        try {
            expected
                    .append(new Saved(screenshot1.save(UUID.randomUUID() + "." + format)))
                    .append(" and ")
                    .append(new Saved(screenshot2.save(UUID.randomUUID() + "." + format)))
                    .append(" have no diff");

            ImageDiff diff = screenshot1.getDiffFrom(screenshot2);
            boolean noDiff = !diff.hasDiff();
            StringBuilder actual = new StringBuilder("");

            if (!noDiff) {
                Screenshot diffFile = new Screenshot(diff.getMarkedImage());
                actual
                        .append("diff ")
                        .append(new Saved(diffFile.save(UUID.randomUUID() + "." + format)));
            }

            if (not) {
                return compile(description, noDiff, actual.toString(), expected.toString(), "", "", "");
            } else {
                return compile(description, noDiff, expected.toString(), actual.toString(), "", "", "");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Cannot save file!\nCause: " + ex);
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
        return see(description, isDisplayed(obj), quoted(obj), quoted(obj));
    }

    public Condition see(String description, String obj) {
        return see(description, isDisplayed(obj), quoted(obj), quoted(obj));
    }

    public Condition see(String description, Class<? extends UIObject> uiObject) {
        try {
            return see(description, uiObjectInstance(uiObject));
        } catch (NotDisplayException ex) {
            UIObject obj = browser.instatiate(uiObject);
            return see(description, isDisplayed(obj), quoted(obj), quoted(obj));
        }
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
        return browser.init(uiObject, null, null);
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
