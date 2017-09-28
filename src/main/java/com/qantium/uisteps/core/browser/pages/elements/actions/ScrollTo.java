package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

public class ScrollTo extends UIElementAction<Object, UIElement> {

    public ScrollTo(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected Object apply(Object... args) {
        WebElement wrappedElement = getUIObject().getWrappedElement();
        return getUIObject().inOpenedBrowser().executeScript("arguments[0].scrollIntoView();", wrappedElement);
    }

    @Override
    protected ActionException errorHandler(Exception ex) {
        sleep(getPollingTime());
        return new ActionException(this, ex);
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
