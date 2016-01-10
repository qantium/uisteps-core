package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.Init;
import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 *
 * @author ASolyankin
 */
@Init(false)
public class Link extends UIElement {

    public String getReference() {
        return getWrappedElement().getAttribute("href");
    }
}
