package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.pages.ElementaryElement;
import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 *
 * @author ASolyankin
 */
public class Link extends UIElement implements ElementaryElement {

    public String getReference() {
        return getWrappedElement().getAttribute("href");
    }
}
