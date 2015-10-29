package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.pages.ElementaryElement;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
public class Link extends UIElement implements ElementaryElement {

    public Link() {
    }
    
    public Link(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public String getReference() {
        return getWrappedElement().getAttribute("href");
    }
}
