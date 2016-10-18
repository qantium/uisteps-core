package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.By;

/**
 * Created by Anton Solyankin
 */
public interface IUIObjectFactory {

    <T extends UIElement> UIElements<T> getAll(Class<T> uiObject);

    <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By locator);

    UIElement get(By locator);

    <T extends UIObject> T get(Class<T> uiObject);

    <T extends UIElement> T get(Class<T> uiObject, By locator);
}
