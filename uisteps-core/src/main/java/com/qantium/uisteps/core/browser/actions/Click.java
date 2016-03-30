package com.qantium.uisteps.core.browser.actions;


import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

/**
 * Created by Solan on 28.03.2016.
 */
public class Click extends Action {

    private final UIElement uiElement;

    public Click(Browser browser, UIElement uiElement) {
        super(browser);
        this.uiElement = uiElement;
    }

    @Override
    public void toExecute() {
        WebElement webElement = uiElement.getWrappedElement();
        String attrTarget = webElement.getAttribute("target");
        webElement.click();
        if (!StringUtils.isEmpty(attrTarget) && !attrTarget.equals("_self")) {
            getBrowser().switchToNextWindow();
        }
    }

    @Override
    public String toString() {
        return "click \"" + uiElement + "\"";
    }
}
