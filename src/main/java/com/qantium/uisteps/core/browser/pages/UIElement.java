package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.browser.pages.elements.finder.FinderGet;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.internal.WrapsElement;

import java.util.*;

/**
 * @author Anton Solyankin
 */
@NotInit
public class UIElement extends HtmlObject implements WrapsElement {

    private By[] locators;
    private HtmlObject context;
    private WebElement wrappedElement;
    private UIElements contextList;
    private int contextListIndex;
    private FinderGet finder;

    public void setWrappedElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public void setContextList(UIElements contextList) {
        this.contextList = contextList;
    }

    public void setContextListIndex(int contextListIndex) {
        this.contextListIndex = contextListIndex;
    }

    public void setFinder(FinderGet finder) {
        this.finder = finder;
    }

    @Override
    public WebElement getWrappedElement() {

        if (checkWrappedElement()) {
            return wrappedElement;
        }

        if(contextList != null) {
            if(finder != null) {
                UIElement elem = finder.clone(contextList.clone().refresh()).get().withDelay(0).immediately();
                wrappedElement = elem.getWrappedElement();
            } else {
                Iterator<By> iterator = Arrays.asList(contextList.getLocators()).iterator();

                List<WebElement> elements = new ArrayList<>();

                while (iterator.hasNext()) {

                    try {
                        By locator = iterator.next();
                        for (WebElement element : getSearchContext().findElements(locator)) {
                            elements.add(element);
                        }
                    } catch (Exception ex) {
                        if (!iterator.hasNext() && elements.isEmpty()) {
                            throw ex;
                        }
                    }
                }

                if (contextList.size() != elements.size()) {
                    throw new IllegalArgumentException("Size of contextList '" + contextList + "' was changed from " + contextList.size() + " to " + elements.size());
                }

                wrappedElement = elements.get(contextListIndex);
            }
            return wrappedElement;
        } else {
            Iterator<By> iterator = Arrays.asList(locators).iterator();

            while (iterator.hasNext()) {
                try {
                    wrappedElement =  getSearchContext().findElement(iterator.next());
                    return wrappedElement;
                } catch (Exception ex) {
                    if (!iterator.hasNext()) {
                        throw ex;
                    }
                }
            }
            throw new IllegalStateException("Locator for UIElement " + this + " is not set!");
        }
    }

    private boolean checkWrappedElement() {

        boolean OK = wrappedElement != null;

        if (OK) {
            try {
                wrappedElement.getLocation();
            } catch (Exception ex) {
                OK = false;
            }

        }
        return OK;
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

    public By[] getLocators() {
        return locators;
    }

    public void setLocators(By... locators) {
        this.locators = locators;
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

        contextStr.append(Arrays.asList(getLocators()));
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

        if (!(Objects.equals(this.getLocators(), other.getLocators()) && Objects.equals(this.context, other.context))) {
            return false;
        }

        if (!(getWrappedElement() != null && other.getWrappedElement() != null && getPosition().equals(other.getPosition()))) {
            return false;
        }

        if (getWrappedElement() != null && other.getWrappedElement() == null) {
            return false;
        }

        if (getWrappedElement() == null && other.getWrappedElement() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.getLocators());
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
        inOpenedBrowser().moveMouseOver(this, xOffset, yOffset);
        return null;
    }

    public <T extends UIElement> T scrollWindowtByOffset(int x, int y) {
        inOpenedBrowser().scrollWindowToTargetByOffset(this, x, y);
        return (T) this;
    }

    public <T extends UIElement> T scrollWindowTo() {
        inOpenedBrowser().scrollWindowToTarget(this);
        return (T) this;
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
        try {
            return getWrappedElement().isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }
}
