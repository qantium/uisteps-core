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

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;

import java.util.ArrayList;
import java.util.List;

import com.qantium.uisteps.core.browser.pages.elements.form.Fillable;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Radio;

/**
 * @author Anton Solyankin
 */
@NotInit
public class RadioButtonGroup extends UIElement implements Fillable {

    protected Radio getWrappedRadio() {
        return new Radio(getWrappedElement());
    }

    public List<RadioButton> getButtons() {

        List<RadioButton> buttons = new ArrayList();

        for (WebElement button : getWrappedRadio().getButtons()) {
            buttons.add(new RadioButton(button));
        }

        return buttons;
    }

    public RadioButton getSelectedButton() {

        for (WebElement button : getWrappedRadio().getButtons()) {

            if (button.isSelected()) {
                return new RadioButton(button);
            }
        }
        return null;
    }

    public boolean hasSelectedButton() {
        return getWrappedRadio().hasSelectedButton();
    }

    public Object selectByValue(String value) {

        for (RadioButton button : getButtons()) {
            String buttonValue = button.getValue();

            if (value.equals(buttonValue)) {
                return button.select();
            }
        }
        return null;
    }

    public Object selectByIndex(int index) {
        List<RadioButton> buttons = getButtons();

        if (index < 0 || index >= buttons.size()) {
            throw new AssertionError("Cannot locate radio button with index: " + index);
        }

        RadioButton button = buttons.get(index);
        button.setIndex(index);
        button.select();
        return null;
    }

    @Override
    public Object setValue(Object value) {
        try {
            return selectByIndex((Integer) value);
        } catch (Exception ex) {
            return selectByValue(value.toString());
        }
    }

    public class RadioButton extends UIElement {

        private Integer index = null;

        public RadioButton(WebElement wrappedElement) {
            setWrappedElement(wrappedElement);
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Object select() {
            inOpenedBrowser().select(this);
            return null;
        }

        public String getValue() {
            return getWrappedElement().getAttribute("value");
        }

        @Override
        public Browser inOpenedBrowser() {
            return RadioButtonGroup.this.inOpenedBrowser();
        }

        @Override
        public String toString() {
            String name = getValue() + " from " + RadioButtonGroup.this;

            if (index != null) {
                return name + " by index " + index;
            } else {
                return name;
            }
        }

        public boolean isEnabled() {
            return getWrappedElement().isEnabled();
        }

        public boolean isSelected() {
            return getWrappedElement().isSelected();
        }
    }
}
