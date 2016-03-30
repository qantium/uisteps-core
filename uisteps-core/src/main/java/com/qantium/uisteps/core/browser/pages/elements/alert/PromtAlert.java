package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Solan on 02.02.2016.
 */
public class PromtAlert extends ConfirmAlert {

    public PromtAlert enter(String text) {
        return inOpenedBrowser().enterInto(this, text);
    }
}
