package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.Named;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 *
 * @author ASolyankin
 */
public abstract class UIElement extends TypifiedElement implements UIObject {

    public UIElement(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public <T extends WrapsElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, getWrappedElement());
    }

    public <T extends Named & WrapsElement> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by, getWrappedElement());
    }

    public <T extends WrapsElement> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, getWrappedElement());
    }

    public <T extends Named & WrapsElement> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by, getWrappedElement());
    }

    public String getText() {
        return inOpenedBrowser().getTextFrom(this);
    }

    public Object click() {
        inOpenedBrowser().click(this);
        return this;
    }

    public Object moveMouseOver() {
        inOpenedBrowser().moveMouseOver(this);
        return this;
    }

    public Object clickOnPoint(int x, int y) {
        inOpenedBrowser().clickOnPoint(this, x, y);
        return this;
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    public <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }
}
