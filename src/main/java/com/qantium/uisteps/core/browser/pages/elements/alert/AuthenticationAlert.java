package com.qantium.uisteps.core.browser.pages.elements.alert;

/**
 * Created by Anton Solyankin
 */
public class AuthenticationAlert extends ConfirmAlert {

    public void authenticateUsing(String login, String password) {
        inOpenedBrowser().authenticateUsing(this, login, password);
    }
}
