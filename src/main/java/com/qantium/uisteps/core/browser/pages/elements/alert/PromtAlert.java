package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Anton Solyankin
 */
public class PromtAlert extends ConfirmAlert {

    public PromtAlert enter(String text) {
        return inOpenedBrowser().enterInto(this, text);
    }
}
