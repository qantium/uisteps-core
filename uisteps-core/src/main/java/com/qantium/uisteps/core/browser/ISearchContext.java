package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.factory.IUIObjectFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * Created Anton Solyankin
 */
public interface ISearchContext {

    <T extends UIObject> T onDisplayed(T uiObject);

    UIElement onDisplayed(By locator);

    <T extends UIObject> T onDisplayed(Class<T> uiObject);

    <T extends UIElement> T onDisplayed(Class<T> uiObject, By locator);

    <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject);

    <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By locator);

}
