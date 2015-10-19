package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import java.util.NoSuchElementException;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 *
 * @author ASolyankin
 */
public class UIElement extends TypifiedElement implements UIObject {

    private WebElement wrappedElement;

    public UIElement(WebElement wrappedElement) {
        super(wrappedElement);
        this.wrappedElement = wrappedElement;
    }

    public UIElement() {
        super(null);
    }

    @Override
    public WebElement getWrappedElement() {
        return wrappedElement;
    }

    public void setWrappedElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
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

    @Override
    public String getName() {

        if (StringUtils.isEmpty(super.getName())) {
            setName(NameConvertor.humanize(getClass()));
        }

        return super.getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    //Find 
    public <T extends UIObject> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject, getWrappedElement());
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, getWrappedElement());
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name) {
        return inOpenedBrowser().find(uiObject, name, getWrappedElement());
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by, getWrappedElement());
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject, getWrappedElement());
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, getWrappedElement());
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().findAll(uiObject, name, getWrappedElement());
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by, getWrappedElement());
    }

    public <T extends UIElement> UIElements<T> uiElements(List<T> proxyElements) {
        return inOpenedBrowser().uiElements(proxyElements);
    }

    public <T extends UIElement> UIElements<T> uiElements(Class<T> uiObject) {
        return inOpenedBrowser().uiElements(uiObject, getWrappedElement());
    }

    public <T extends UIElement> UIElements<T> uiElements(Class<T> uiObject, By by) {
        return inOpenedBrowser().uiElements(uiObject, by, getWrappedElement());
    }

    public <T extends UIElement> UIElements<T> uiElements(Class<T> uiObject, String name) {
        return inOpenedBrowser().uiElements(uiObject, name, getWrappedElement());
    }

    public <T extends UIElement> UIElements<T> uiElements(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().uiElements(uiObject, name, by, getWrappedElement());
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject, getWrappedElement());
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, getWrappedElement());
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayed(uiObject, name, getWrappedElement());
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by, getWrappedElement());
    }

    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    @Override
    public SearchContext getSearchContext() {
        return getWrappedElement();
    }
}
