package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author ASolyankin
 */
public class UIElement extends AbstractUIObject implements WrapsElement {

    private WebElement wrappedElement;
    private By locator;
    private UIObject context;

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
    public String toString() {
        return getName();
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

    @Override
    public List<WebElement> findElements(By by) {
        return getWrappedElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getWrappedElement().findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        try {
            return getWrappedElement().isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }
}
