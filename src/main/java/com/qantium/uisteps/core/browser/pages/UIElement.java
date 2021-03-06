package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.By.ByXPath;

/**
 * @author Anton Solyankin
 */
@NotInit
public class UIElement extends HtmlObject implements WrapsElement {

    private By[] locators;
    private HtmlObject context;
    private WebElement wrappedElement;

    public void setWrappedElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public WebElement getWrappedElement() {
        return getWrappedElement(true);
    }

    private WebElement getWrappedElement(boolean isDisplay) {
        if (!checkWrappedElement(isDisplay)) {
            if (ArrayUtils.isEmpty(locators)) {
                throw new IllegalStateException("Locator for UIElement " + this + " is not set!");
            }
            wrappedElement = getWebElement(isDisplay);
        }


        if (wrappedElement != null) {
            new UIElementDecorator(this, wrappedElement).execute();
        }
        return wrappedElement;
    }

    private WebElement getWebElement(boolean isDisplay) {
        Iterator<By> iterator = Arrays.asList(locators).iterator();

        while (iterator.hasNext()) {
            By locator = iterator.next();
            if (locator == null) {
                throw new IllegalArgumentException("Locator is null");
            }
            try {
                SearchContext searchContext = getSearchContext();
                WebElement webElement = searchContext.findElement(locator);
                if (isDisplay && !webElement.isDisplayed()) {
                    if (iterator.hasNext()) {
                        continue;
                    }
                }
                return webElement;
            } catch (IsNotDisplayedException ex) {
                throw ex;
            } catch (Exception ex) {
                if (!iterator.hasNext()) {
                    if (isDisplay) {
                        throw new IsNotDisplayedException("Cannot locate " + this + ". Locators: " + getLocatorString(), ex);
                    } else {
                        return null;
                    }
                }
            }
        }

        throw new IsNotDisplayedException("Cannot locate " + this + ". Locators: " + getLocatorString());
    }

    private boolean checkWrappedElement(boolean isDisplay) {

        boolean OK = wrappedElement != null;

        if (OK) {
            try {
                wrappedElement.getLocation();
            } catch (Exception ex) {
                if (isDisplay) {
                    OK = false;
                } else {
                    wrappedElement = null;
                    OK = true;
                }
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
        this.locators = checkForRootXpath(locators);
    }

    private By[] checkForRootXpath(By[] locators) {

        if (locators == null) {
            return locators;
        } else {
            return Arrays.asList(locators).stream().map((locator) -> {
                if (context != null && (locator instanceof ByXPath)) {
                    try {
                        Field xpathField = ByXPath.class.getDeclaredField("xpathExpression");
                        xpathField.setAccessible(true);
                        String xpath = (String) xpathField.get(locator);
                        xpathField.set(locator, xpath.replaceAll("^//", ".//").replaceAll("^/", ""));
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                return locator;
            }).collect(Collectors.toList()).toArray(new By[locators.length]);
        }
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
        verifyIsDisplayed();
        return getWrappedElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        verifyIsDisplayed();
        return getWrappedElement().findElement(by);
    }

    private void verifyIsDisplayed() {
        if (!DisplayStack.contains(this)) {
            DisplayStack.add(this);
            Waiting.waitUntil(this, () -> {
                getWrappedElement();
                return isDisplayed();
            });
            DisplayStack.remove(this);
        }
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

    public String getHtml() {
        return inOpenedBrowser().getHtmlOf(this);
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
    public boolean isDisplayed() {
        try {
            return getWrappedElement(true).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean isNotDisplayed() {
        try {
            return !getWrappedElement(false).isDisplayed();
        } catch (Exception ex) {
            return true;
        }
    }

    public boolean isEnabled() {
        return inOpenedBrowser().isEnabled(this);
    }

    public <T extends UIElement> T as(Class<T> type) {
        T as = inOpenedBrowser().get(type, context, locators)
                .withName(getName())
                .withTimeout(getTimeout())
                .pollingEvery(getPollingTime())
                .withDelay(getDelay());
        as.setWrappedElement(wrappedElement);
        return as;
    }
}

