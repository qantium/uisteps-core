package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ASolyankin
 */
public class UIElement extends AbstractUIObject implements WrapsElement {

    private WebElement wrappedElement;
    private By locator;
    private UIObject context;

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
            WebElement element = getWrappedElement();
            int waitInSeconds = Integer.parseInt(UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT)) / 1000;
            WebDriverWait wait = new WebDriverWait(inOpenedBrowser().getDriver(), waitInSeconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }

        final UIElement other = (UIElement) obj;

        if (!Objects.equals(this.locator, other.locator)) {
            return false;
        }

        return Objects.equals(this.context, other.context);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.locator);
        hash = 97 * hash + Objects.hashCode(this.context);
        return hash;
    }

    //Elements
    public String getText() {
        return inOpenedBrowser().getTextFrom(this);
    }

    public Object click() {
        inOpenedBrowser().click(this);
        return null;
    }

    public Object clickAndHold() {
        inOpenedBrowser().clickAndHold(this);
        return null;
    }

    public Object clickOnPoint(int x, int y) {
        inOpenedBrowser().clickOnPoint(this, x, y);
        return null;
    }

    public Object doubleClick() {
        inOpenedBrowser().doubleClick(this);
        return null;
    }

    public Object dragAndDrop(WrapsElement target) {
        inOpenedBrowser().dragAndDrop(this, target);
        return null;
    }

    public Object dragAndDrop(int xOffset, int yOffset) {
        inOpenedBrowser().dragAndDrop(this, xOffset, yOffset);
        return null;
    }

    public Object keyDown(Keys theKey) {
        inOpenedBrowser().keyDown(this, theKey);
        return null;
    }

    public Object keyUp(Keys theKey) {
        inOpenedBrowser().keyUp(this, theKey);
        return null;
    }

    public Object moveMouseOver() {
        inOpenedBrowser().moveMouseOver(this);
        return null;
    }

    public Object moveWithOffset(int xOffset, int yOffset) {
        inOpenedBrowser().moveToElement(this, xOffset, yOffset);
        return null;
    }

    //Tags
    public String getTagName() {
        return inOpenedBrowser().getTagNameOf(this);
    }

    public String getCSSProperty(String cssProperty) {
        return inOpenedBrowser().getCSSPropertyOf(this, cssProperty);
    }

    public Point getPosition() {
        return inOpenedBrowser().getPositionOf(this);
    }

    public Dimension getSize() {
        return inOpenedBrowser().getSizeOf(this);
    }
}
