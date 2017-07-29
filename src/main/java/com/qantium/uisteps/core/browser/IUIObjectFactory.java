package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.HtmlObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.By;

import java.util.function.Supplier;

/**
 * Created by Anton Solyankin
 */
public interface IUIObjectFactory {

    <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By... locators);

    default <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, Supplier<By[]> supplier) {
        return getAll(uiObject, supplier.get());
    }

    UIElement get(By... locators);

    default UIElement get(Supplier<By[]> supplier) {
        return get(supplier.get());
    }

    <T extends UIObject> T get(Class<T> uiObject);

    <T extends UIElement> T get(Class<T> uiObject, By... locators);

    <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By... locators);

    default <T extends UIElement> T get(Class<T> uiObject, Supplier<By[]> supplier) {
        return get(uiObject, supplier.get());
    }

    default <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, Supplier<By[]> supplier) {
        return get(uiObject, context, supplier.get());
    }
}
