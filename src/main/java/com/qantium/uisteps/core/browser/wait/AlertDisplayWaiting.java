package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import org.openqa.selenium.NoAlertPresentException;

/**
 * Created by Anton Solyankin
 */
public class AlertDisplayWaiting extends Waiting {

    private final Alert alert;

    public AlertDisplayWaiting(Alert alert) {
        this.alert = alert;
    }

    @Override
    protected boolean until() {
        try {
            alert.getText();
            return !isNot();
        } catch (NoAlertPresentException ex) {
            if(isNot()) {
                return true;
            } else {
                throw new IsNotDisplayException(alert, ex);
            }
        }
    }
}
