package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
public class Link extends UIElement {

    private final ru.yandex.qatools.htmlelements.element.Link wrappedLink;
    
    public Link(WebElement wrappedElement) {
        super(wrappedElement);
        wrappedLink = new ru.yandex.qatools.htmlelements.element.Link(wrappedElement);
    }

    public String getReference() {
        return wrappedLink.getReference();
    }
}
