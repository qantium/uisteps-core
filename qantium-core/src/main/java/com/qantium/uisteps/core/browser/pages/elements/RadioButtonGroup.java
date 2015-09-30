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
import com.qantium.uisteps.core.browser.pages.UIElement;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Radio;

/**
 *
 * @author ASolyankin
 */
public abstract class RadioButtonGroup extends UIElement {

    private final Radio wrappedRadio;

    public RadioButtonGroup(WebElement wrappedElement) {
        super(wrappedElement);
        wrappedRadio = new Radio(wrappedElement);
    }

    public List<RadioButton> getButtons() {

        List<RadioButton> buttons = new ArrayList();

        for (WebElement button : wrappedRadio.getButtons()) {
            buttons.add(new RadioButton(button));
        }

        return buttons;
    }

    public RadioButton getSelectedButton() {

        for (WebElement button : wrappedRadio.getButtons()) {
            if (button.isSelected()) {
                return new RadioButton(button);
            }
        }
        return null;
    }

    public boolean hasSelectedButton() {
        return wrappedRadio.hasSelectedButton();
    }

    public Object selectByValue(String value) {
        for (RadioButton button : getButtons()) {
            String buttonValue = button.getValue();
            if (value.equals(buttonValue)) {
                button.setName(button.getName() + "from " + this.getName());
                return button.select();
            }
        }
        return null;
    }

    public void selectByIndex(int index) {
        List<RadioButton> buttons = getButtons();

        if (index < 0 || index >= buttons.size()) {
            throw new AssertionError(String.format("Cannot locate radio button with index: %d", index));
        }

        RadioButton button = buttons.get(index);
        button.setName(button.getName() + "from " + this.getName());

        button.select();
    }

    public class RadioButton extends UIElement {

        public RadioButton(WebElement wrappedElement) {
            super(wrappedElement);
        }

        public Object select() {
            inOpenedBrowser().select(this);
            return null;
        }

        public String getValue() {
            return this.getWrappedElement().getAttribute("value");
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public Browser inOpenedBrowser() {
            return RadioButtonGroup.this.inOpenedBrowser();
        }
    }
}
