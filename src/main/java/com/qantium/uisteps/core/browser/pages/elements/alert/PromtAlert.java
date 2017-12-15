package com.qantium.uisteps.core.browser.pages.elements.alert;

import com.qantium.uisteps.core.browser.NotInit;

/**
 * Created by Anton Solyankin
 */
@NotInit
public class PromtAlert extends ConfirmAlert {

    public PromtAlert enter(String text) {
        return inOpenedBrowser().enterInto(this, text);
    }

    @Override
    protected Object setValue(Object value) {
        if (value != null) {
            return enter(value.toString());
        }
        return accept();
    }
}
