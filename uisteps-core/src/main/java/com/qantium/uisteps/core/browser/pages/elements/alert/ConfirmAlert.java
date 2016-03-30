package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Solan on 02.02.2016.
 */
public class ConfirmAlert extends Alert {

    public void dismiss() {
        inOpenedBrowser().dismiss(this);
    }
}
