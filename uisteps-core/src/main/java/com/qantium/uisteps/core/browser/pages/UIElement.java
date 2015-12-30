package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.screenshots.Screenshot;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
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

    /**
     * Internal method
     * 
     * @param wrappedElement WebElement that will be wrapped
     * @throws IllegalStateException if wrappedElement is already set
     */
    public void setWrappedElement(WebElement wrappedElement) throws IllegalStateException {
        
        if(this.wrappedElement != null) {
            throw new IllegalStateException("WrappedElement is already set to " + this);
        }
        this.wrappedElement = wrappedElement;
    }

    @Override
    public WebElement getWrappedElement() {

        if (wrappedElement == null) {

            if(getContext() == null) {
                wrappedElement = inOpenedBrowser().getDriver().findElement(getLocator());
            } else {
                wrappedElement = getContext().findElement(getLocator());
            }
        }
        return wrappedElement;
    }

    /**
     * Internal method
     * 
     * @param context can be a page or another element
     * @throws IllegalStateException if context is already set
     */
    public void setContext(UIObject context) {
        
        if(this.context != null) {
            throw new IllegalStateException("Context is already set to " + this);
        }
        this.context = context;
    }

    public UIObject getContext() {

        if(context == null && this.getClass().isAnnotationPresent(Context.class)) {


        }
        return context;
    }

    public By getLocator() {
        return locator;
    }
    
    /**
     * Internal method
     * 
     * @param locator for elemnt search
     * @throws IllegalStateException if locator is already set
     */
    public void setLocator(By locator) {
        
        if(this.locator != null) {
            throw new IllegalStateException("Locator is already set to " + this);
        }
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
    public boolean isDisplayed() {

        try {
            return getWrappedElement().isDisplayed();
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

        return Objects.equals(this.locator, other.locator) && Objects.equals(this.context, other.context);
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

    public void contextClick() {
        inOpenedBrowser().contextClick(this);
    }

    public void releaseMouse() {
        inOpenedBrowser().releaseMouse(this);
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

    public Point getRelativePositionOf(WrapsElement target) {
        return inOpenedBrowser().getRelativePositionOf(this, target);
    }

    public Point getRelativeMiddlePositionOf(WrapsElement element, WrapsElement target) {
        return inOpenedBrowser().getRelativeMiddlePositionOf(this, target);
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
    public void afterInitialization() {
        //do nothing
    }
}
