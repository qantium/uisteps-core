package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.then.Then;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 *
 * @author ASolyankin
 */
public class UIElement extends TypifiedElement implements UIBlockOrElement {

    private WebElement wrappedElement;
    private By locator;
    private UIObject context;

    public UIElement(WebElement wrappedElement) {
        super(wrappedElement);
        this.wrappedElement = wrappedElement;
    }

    public UIElement() {
        super(null);
    }

    @Override
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

    @Override
    public void setContext(UIObject context) {
        this.context = context;
    }

    @Override
    public UIObject getContext() {
        return context;
    }

    @Override
    public By getLocator() {
        return locator;
    }

    @Override
    public void setLocator(By locator) {
        this.locator = locator;
    }

    @Override
    public SearchContext getSearchContext() {
        return getWrappedElement();
    }

    @Override
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

    //onDisplayed
    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject, this);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, this);
    }

    public <T extends UIBlockOrElement> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }

    @Override
    public String getContextString() {

        StringBuilder contextStr = new StringBuilder();

        if (getContext() != null) {

            if (getContext() instanceof UIBlockOrElement) {
                contextStr.append(((UIBlockOrElement) getContext()).getContextString());
            } else {
                contextStr.append(getContext().getName());
            }
            contextStr.append(" => ");
        }
        contextStr.append(getName());
        return contextStr.toString();
    }

    @Override
    public String getLocatorString() {

        StringBuilder contextStr = new StringBuilder();

        if (getContext() != null) {

            if (getContext() instanceof UIBlockOrElement) {
                contextStr.append(((UIBlockOrElement) getContext()).getLocatorString());
                contextStr.append(" => ");
            }
        }
        contextStr.append(getLocator());
        return contextStr.toString();
    }

}
