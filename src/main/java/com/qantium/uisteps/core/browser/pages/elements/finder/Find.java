package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Anton Solyankin
 */
public enum Find {

    ATTRIBUTE {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsNotEmpty(attribute);
            return element.getWrappedElement().getAttribute(attribute);
        }
    }, CSS {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsNotEmpty(attribute);
            return element.getWrappedElement().getCssValue(attribute);
        }
    }, TEXT {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsEmpty(attribute);
            return element.getWrappedElement().getText();
        }
    }, HTML {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsEmpty(attribute);
            return element.getWrappedElement().getAttribute("innerHtml");
        }
    };

    protected void verifyIsNotEmpty(String attribute) {
        if (isEmpty(attribute)) {
            throw new IllegalArgumentException("Attribute " + this + " cannot be empty!");
        }
    }

    protected void verifyIsEmpty(String attribute) {
        if (isNotEmpty(attribute)) {
            throw new IllegalArgumentException("Attribute " + this + " must be empty!");
        }
    }

    protected abstract String get(UIElement element, String attr);

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", " ");
    }
}
