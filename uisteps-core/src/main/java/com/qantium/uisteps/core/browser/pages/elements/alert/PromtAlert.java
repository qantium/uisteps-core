package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Solan on 02.02.2016.
 */
public class PromtAlert extends ComfirmAlert {

    public void enter(String text) {
        inOpenedBrowser().enterInto(this, text);
    }
}
