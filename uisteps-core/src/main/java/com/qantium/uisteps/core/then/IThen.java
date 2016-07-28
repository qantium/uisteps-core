package com.qantium.uisteps.core.then;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import org.openqa.selenium.By;

/**
 * Created by Solan on 26.07.2016.
 */
public interface IThen {

    <T extends UIElement> Then<T> then(Class<T> uiElement, By locator);

    <T extends UIObject> Then<T> then(Class<T> uiObject);
}
