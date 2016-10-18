package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

/**
 * Created by Anton Solyankin
 */
public enum Find {

    ATTRIBUTE {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsNotEmpty(attribute);
            return getWrappedElement(element).getAttribute(attribute);
        }
    }, CSS {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsNotEmpty(attribute);
            return getWrappedElement(element).getCssValue(attribute);
        }
    }, TEXT {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsEmpty(attribute);
            return getWrappedElement(element).getText();
        }
    }, HTML {
        @Override
        public String get(UIElement element, String attribute) {
            verifyIsEmpty(attribute);
            return getWrappedElement(element).getAttribute("innerHtml");
        }
    };

    protected void verifyIsNotEmpty(String attribute) {
        if (StringUtils.isEmpty(attribute)) {
            throw new IllegalArgumentException("Attribute " + this + " cannot be empty!");
        }
    }

    protected void verifyIsEmpty(String attribute) {
        if (!StringUtils.isEmpty(attribute)) {
            throw new IllegalArgumentException("Attribute " + this + " must be empty!");
        }
    }

    protected WebElement getWrappedElement(UIElement element) {
        return element.getWrappedElement();
    }

    protected abstract String get(UIElement element, String attr);

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", " ");
    }
}
