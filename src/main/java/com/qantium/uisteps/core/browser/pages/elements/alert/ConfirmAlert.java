package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;

/**
 * Created by Anton Solyankin
 */
@NotInit
public class ConfirmAlert extends Alert {

    public void dismiss() {
        inOpenedBrowser().dismiss(this);
    }
}
