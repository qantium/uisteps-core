package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 *
 * @author ASolyankin
 */
@NotInit
public class Link extends UIElement {

    public String getReference() {
        return inOpenedBrowser().getAttribute(this, "href");
    }

}
