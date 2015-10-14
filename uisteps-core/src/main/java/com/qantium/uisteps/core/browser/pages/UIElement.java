package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 *
 * @author ASolyankin
 */
public abstract class UIElement extends TypifiedElement implements UIObject {

    public UIElement(WebElement wrappedElement) {
        super(wrappedElement);
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
    public <T extends UIObject> T as(Class<T> uiObject) {
        return inOpenedBrowser().displayed(uiObject, getWrappedElement());
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
        return inOpenedBrowser().find(uiObject);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().find(uiObject, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, by, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name) {
        return inOpenedBrowser().find(uiObject, name);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().find(uiObject, name, by);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, context);
    }

    public <T extends UIObject> T find(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().find(uiObject, name, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, by, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().findAll(uiObject, name);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().findAll(uiObject, name, by);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, context);
    }

    public <T extends UIObject> List<T> findAll(Class<T> uiObject, String name, By by, SearchContext context) {
        return inOpenedBrowser().findAll(uiObject, name, by, context);
    }
}
