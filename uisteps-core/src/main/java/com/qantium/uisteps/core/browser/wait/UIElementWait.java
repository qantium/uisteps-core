package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * Created by Solan on 23.03.2016.
 */
public class UIElementWait extends UIObjectWait {

    private final UIElement uiElement;

    public UIElementWait (UIElement uiElement) {
        super(uiElement);
        this.uiElement = uiElement;
     }

    public void untilIsClickable() {
        untilIsDisplayed();
        until(elementToBeClickable(uiElement.getWrappedElement()));
    }
}
