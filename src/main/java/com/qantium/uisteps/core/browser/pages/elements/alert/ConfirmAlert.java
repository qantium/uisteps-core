package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Anton Solyankin
 */
public class ConfirmAlert extends Alert {

    public void dismiss() {
        inOpenedBrowser().dismiss(this);
    }
}
