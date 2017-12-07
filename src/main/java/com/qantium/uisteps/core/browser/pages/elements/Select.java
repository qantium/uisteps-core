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

import com.qantium.uisteps.core.browser.LocatorFactory;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASolyankin
 */
@NotInit
@FindBy(tagName = "select")
@Select.OptionsBy(@FindBy(css = ""))
public class Select extends UIElement {

    private By[] optionsLocator = {By.tagName("option")};

    public Select withOptionsLocator(By... optionsLocator) {
        if (ArrayUtils.isEmpty(optionsLocator)) {
            throw new IllegalArgumentException("Options locator list is empty");
        }
        this.optionsLocator = optionsLocator;
        return this;
    }

    protected By[] getOptionsLocator() {
        if (getClass().isAnnotationPresent(Select.OptionsBy.class)) {
            FindBy[] selectOptionsBy = getClass().getAnnotation(OptionsBy.class).value();
            List<By> locators = new ArrayList<>();
            LocatorFactory locatorFactory = new LocatorFactory();

            for (FindBy findBy : selectOptionsBy) {
                locators.add(locatorFactory.getLocator(findBy));
            }
            optionsLocator = locators.toArray(new By[selectOptionsBy.length]);
        }

        return optionsLocator;
    }

    public Options getOptions() {
        return get(Options.class, getOptionsLocator());
    }

    public boolean isMultiple() {
        return inOpenedBrowser().isMultiple(this);
    }

    @Override
    public List<WebElement> getAllSelectedOptions() {
        return getWrappedSelect().getAllSelectedOptions();
    }

    @Override
    public WebElement getFirstSelectedOption() {
        return getWrappedSelect().getFirstSelectedOption();
    }

    public boolean hasSelectedOption() {
        for (WebElement option : getOptions()) {
            if (option.isSelected()) {
                return true;
            }
        }

        return false;
    }

    public Object select(Option option) {
        inOpenedBrowser().select(option);
        return null;
    }

    public Object select(String value, By by) {
        return select(new Option(this, value, by));
    }

    @Override
    public Object selectByIndex(int index) {
        return select(new Option(this, index));
    }

    @Override
    public Object selectByValue(String value) {
        return select(value, By.VALUE);
    }

    @Override
    public Object selectByVisibleText(String value) {
        return select(value, By.VISIBLE_VALUE);
    }

    @Override
    public Object deselectAll() {
        inOpenedBrowser().deselectAllValuesFrom(this);
        return null;
    }

    public Object deselect(Option option) {
        inOpenedBrowser().deselect(option);
        return null;
    }

    public Object deselect(String value, By by) {
        return deselect(new Option(this, value, by));
    }

    @Override
    public Object deselectByIndex(int index) {
        return deselect(new Option(this, index));
    }

    @Override
    public Object deselectByValue(String value) {
        return deselect(value, By.VALUE);
    }

    @Override
    public Object deselectByVisibleText(String value) {
        return deselect(value, By.VISIBLE_VALUE);
    }

    @Override
    protected Object setValue(Object value) {
        try {
            return selectByIndex((Integer) value);
        } catch (Exception ex) {
            return selectByVisibleText(value.toString());
        }
    }

    @Override
    public List<WebElement> getContent() {
        return getAllSelectedOptions();
    }

//    public enum By {
//        VALUE, VISIBLE_VALUE, INDEX
//    }

    @NotInit
    public static class Options extends UIElements<Option> {

        public Options() {
            super(Option.class);
        }
    }

    @NotInit
    public static class Option extends Button {

        private String name = "option";
        private final Select select;
        private final org.openqa.selenium.support.ui.Select wrappedSelect;
        private final String value;
        private final By by;

        public Option(Select select, String value, By by) {
            getWrappedElement().getTagName()
            this.select = select;
            wrappedSelect = select.getWrappedSelect();
            this.value = value;
            this.by = by;
        }

        public Option(Select select, Integer index) {
            this(select, index.toString(), By.INDEX);
        }

        public Object select() {

            try {
                switch (by) {
                    case VISIBLE_VALUE:
                        wrappedSelect.selectByVisibleText(value);
                        break;
                    case VALUE:
                        wrappedSelect.selectByValue(value);
                        break;
                    case INDEX:
                        wrappedSelect.selectByIndex(Integer.parseInt(value));
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception ex) {
                throw new AssertionError("Cannot select by " + by.name().toLowerCase().replace("_", " ") + " " + value + " in " + select + "\nCause:" + ex);
            }
            return null;
        }

        public Object deselect() {

            try {
                switch (by) {
                    case VISIBLE_VALUE:
                        wrappedSelect.deselectByVisibleText(value);
                        break;
                    case VALUE:
                        wrappedSelect.deselectByValue(value);
                        break;
                    case INDEX:
                        wrappedSelect.deselectByIndex(Integer.parseInt(value));
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception ex) {
                throw new AssertionError("Cannot deselect by " + by.name().toLowerCase().replace("_", " ") + " " + value + " in " + select + "\nCause:" + ex);
            }
            return null;
        }

        public By selectBy() {
            return by;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder
                    .append(getName())
                    .append(" with ")
                    .append(by.name().toLowerCase().replace("_", " "))
                    .append(" ")
                    .append(value)
                    .append(" from ")
                    .append(select);

            return builder.toString();
        }

        public boolean isSelected() {
            return getWrappedElement().isSelected();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface OptionsBy {
        FindBy[] value();
    }
}
