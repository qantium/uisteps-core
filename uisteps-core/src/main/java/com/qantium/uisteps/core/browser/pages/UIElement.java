package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class UIElement implements UIObject, WrapsElement {

    private WebElement wrappedElement;
    private By locator;
    private UIObject context;
    private String name;

    public UIElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public UIElement() {
    }

    public void setWrappedElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public WebElement getWrappedElement() {

        if (wrappedElement != null) {
            return wrappedElement;
        }

        SearchContext searchContext;

        if (getContext() != null) {
            searchContext = getContext().getSearchContext();
        } else {
            searchContext = inOpenedBrowser().getDriver();
        }

        return searchContext.findElement(getLocator());
    }

    public void setContext(UIObject context) {
        this.context = context;
    }

    public UIObject getContext() {
        return context;
    }

    public By getLocator() {
        return locator;
    }

    public void setLocator(By locator) {
        this.locator = locator;
    }

    @Override
    public SearchContext getSearchContext() {
        return getWrappedElement();
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

    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    @Override
    public String getName() {

        if (StringUtils.isEmpty(name)) {
            setName(NameConvertor.humanize(getClass()));
        }

        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    //onDisplayed
    protected <T extends UIElement> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject, this);
    }
    
    protected <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        
        if (Page.class.isAssignableFrom(uiObject)) {
            return inOpenedBrowser().onDisplayed(uiObject);
        } else {
            return (T) inOpenedBrowser().onDisplayed((Class<UIElement>) uiObject, this);
        }
    }

    protected <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, this);
    }

    protected <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    protected <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }
    
    protected <T extends UIElement> T find(Class<T> uiObject) {
        return inOpenedBrowser().find(uiObject, this);
    }

    protected <T extends UIElement> T find(Class<T> uiObject, By by) {
        return inOpenedBrowser().find(uiObject, by, this);
    }

    protected <T extends UIElement> UIElements<T> finddAll(Class<T> uiObject) {
        return inOpenedBrowser().findAll(uiObject, this);
    }

    protected <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().findAll(uiObject, by, this);
    }

    public String getContextString() {

        StringBuilder contextStr = new StringBuilder();

        if (getContext() != null) {

            if (getContext() instanceof UIElement) {
                contextStr.append(((UIElement) getContext()).getContextString());
            } else {
                contextStr.append(getContext().getName());
            }
            contextStr.append(" => ");
        }
        contextStr.append(getName());
        return contextStr.toString();
    }

    public String getLocatorString() {

        StringBuilder contextStr = new StringBuilder();

        if (getContext() != null) {

            if (getContext() instanceof UIElement) {
                contextStr.append(((UIElement) getContext()).getLocatorString());
                contextStr.append(" => ");
            }
        }
        contextStr.append(getLocator());
        return contextStr.toString();
    }

    protected List<WebElement> findElements(By by) {
        return getWrappedElement().findElements(by);
    }

    //switch window
    protected void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    protected void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    protected void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    protected void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }
    
    @Override
    public boolean isDisplayed() {
        return getWrappedElement().isDisplayed();
    }
}
