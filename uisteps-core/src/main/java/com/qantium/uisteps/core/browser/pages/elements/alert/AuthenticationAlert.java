package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Solan on 02.02.2016.
 */
public class AuthenticationAlert extends ComfirmAlert {

    public void authenticateUsing(String login, String password) {
        inOpenedBrowser().authenticateUsing(this, login, password);
    }
}
