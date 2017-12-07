package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public interface UIElementActions {

    default void perform(UIElement uiElement) throws ActionException {
        new UIElementAction<Void, UIElement>(uiElement) {
            @Override
            protected Void apply(Object... args) {
                WebDriver driver = getUIObject().inOpenedBrowser().getDriver();
                Actions actions = new Actions(driver);
                init(actions).build().perform();
                return null;
            }
        }.perform();
    }

    Actions init(Actions actions);
}
