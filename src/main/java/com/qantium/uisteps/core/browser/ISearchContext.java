package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.By;

import java.util.function.Supplier;

/**
 * Created Anton Solyankin
 */
public interface ISearchContext {

    UIElement onDisplayed(By... locator);

    <T extends UIObject> T onDisplayed(T uiObject);

    <T extends UIObject> T onDisplayed(Class<T> uiObject);

    <T extends UIElement> T onDisplayed(Class<T> uiObject, By... locator);

    default <T extends UIElement> T onDisplayed(Class<T> uiObject, Supplier<By[]> supplier) {
        return onDisplayed(uiObject, supplier.get());
    }

    <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By... locator);

    default <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, Supplier<By[]> supplier) {
        return onAllDisplayed(uiObject, supplier.get());
    }

}
