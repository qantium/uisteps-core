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
package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.elements.groups.TextBlockGroup;
import org.openqa.selenium.support.FindBy;

/**
 * @author ASolyankin
 */
@NotInit
@FindBy(tagName = "select")
@Group.Elements(@FindBy(css = "option"))
public class Select extends TextBlockGroup {

    public Group<TextBlock> getSelectedOptions() {
        return inOpenedBrowser().getSelectedOptions(this);
    }

    public Group<TextBlock> getNotSelectedOptions(Select select) {
        return inOpenedBrowser().getNotSelectedOptions(select);
    }

    public Object selectFirstByVisibleValue(String... values) {
        inOpenedBrowser().selectFirstByVisibleValue(this, values);
        return null;
    }

    public Object selectAllByVisibleValue(String... values) {
        inOpenedBrowser().selectAllByVisibleValue(this, values);
        return null;
    }

    public Object selectFirstByValue(String... values) {
        inOpenedBrowser().selectFirstByValue(this, values);
        return null;
    }

    public Object selectAllByValue(String... values) {
        inOpenedBrowser().selectAllByValue(this, values);
        return null;
    }

    public Object selectAll() {
        inOpenedBrowser().selectAll(this);
        return null;
    }

    public Object deselectAll() {
        inOpenedBrowser().deselectAll(this);
        return null;
    }

    public Object selectByIndex(Integer... indexes) {
        inOpenedBrowser().selectByIndex(this, indexes);
        return null;
    }

    public Object deselectByIndex(Integer... indexes) {
        inOpenedBrowser().deselectByIndex(this, indexes);
        return null;
    }

    public Object deselectFirstByVisibleValue(String... values) {
        inOpenedBrowser().deselectFirstByVisibleValue(this, values);
        return null;
    }

    public Object deselectAllByVisibleValue(String... values) {
        inOpenedBrowser().deselectAllByVisibleValue(this, values);
        return null;
    }

    public Object deselectFirstByValue(String... values) {
        inOpenedBrowser().deselectFirstByValue(this, values);
        return null;
    }

    public Object deselectAllByValue(String... values) {
        inOpenedBrowser().deselectAllByValue(this, values);
        return null;
    }

    public boolean isMultiple() {
        return inOpenedBrowser().isMultiple(this);
    }

}
