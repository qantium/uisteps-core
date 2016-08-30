package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.utils.zk.ZK;
import com.qantium.uisteps.core.utils.zk.ZKSiblingLocator;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;
import java.util.Objects;

/**
 * @author Anton Solyankin
 */
@NotInit
public class UIElement extends HtmlObject implements WrapsElement {

    private By locator;
    private HtmlObject context;
    private WebElement wrappedElement;

    protected void setWrappedElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public WebElement getWrappedElement() {

        if(wrappedElement != null) {
            return wrappedElement;
        }

        if (getLocator() != null) {
            return getSearchContext().findElement(getLocator());
        } else {
            throw new IllegalStateException("Locator for UIElement " + this + " is not set!");
        }
    }

    @Override
    public SearchContext getSearchContext() {

        if (getContext() == null) {
            return inOpenedBrowser();
        } else {
            return getContext();
        }
    }

    /**
     * Internal method
     *
     * @param context can be a page or another element
     * @throws IllegalStateException if context is already set
     */
    public void setContext(HtmlObject context) {

        if (this.context != null) {
            throw new IllegalStateException("Context " + this.context + " is already set to " + this + "! Cannot set " + context + " context");
        }
        this.context = context;
    }

    public HtmlObject getContext() {
        return context;
    }

    public By getLocator() {

        if (locator != null) {
            if (locator instanceof ZKSiblingLocator) {
                return getSiblingLocator();
            }

            if (locator instanceof By.ById) {
                ZK zk = new ZK(inOpenedBrowser());
                By.ById id = (By.ById) locator;

                if (zk.isId(id)) {
                    if (zk.isShiftId(id)) {
                        locator = zk.getLocator(id, getContext());
                    } else {
                        locator = zk.getLocator(id);
                    }
                }
            }
        }
        return locator;
    }

    private By getSiblingLocator() {
        ZK zk = new ZK(inOpenedBrowser());
        ZKSiblingLocator siblingLocator = (ZKSiblingLocator) locator;
        return zk.getLocator(siblingLocator.getContext(), siblingLocator.getShift());
    }

    /**
     * Internal method
     *
     * @param locator for element search
     * @throws IllegalStateException if locator is already set
     */
    public void setLocator(By locator) {
        this.locator = locator;
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
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }

        final UIElement other = (UIElement) obj;

        return Objects.equals(this.getLocator(), other.getLocator()) && Objects.equals(this.context, other.context);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.getLocator());
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

    public void contextClick() {
        inOpenedBrowser().contextClick(this);
    }

    public void releaseMouse() {
        inOpenedBrowser().releaseMouse(this);
    }

    public Object dragAndDrop(UIElement target) {
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

    public Object moveMouseOverWithOffset(int xOffset, int yOffset) {
        inOpenedBrowser().moveToElement(this, xOffset, yOffset);
        return null;
    }

    //Tags
    public String getTagName() {
        return inOpenedBrowser().getTagNameOf(this);
    }

    public String getAttribute(String attribute) {
        return inOpenedBrowser().getAttribute(this, attribute);
    }

    public String getCSSProperty(String cssProperty) {
        return inOpenedBrowser().getCSSPropertyOf(this, cssProperty);
    }

    public Point getPosition() {
        return inOpenedBrowser().getPositionOf(this);
    }

    public Point getMiddlePosition() {
        return inOpenedBrowser().getMiddlePositionOf(this);
    }

    public Point getRelativePositionOf(UIElement element) {
        return inOpenedBrowser().getRelativePositionOf(this, element);
    }

    public Point getRelativeMiddlePositionOf(UIElement element) {
        return inOpenedBrowser().getRelativeMiddlePositionOf(this, element);
    }

    public Dimension getSize() {
        return inOpenedBrowser().getSizeOf(this);
    }

    //Screenshots
    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot(this);
    }

    @Override
    public boolean isCurrentlyDisplayed() {
        return getWrappedElement().isDisplayed();
    }
}
