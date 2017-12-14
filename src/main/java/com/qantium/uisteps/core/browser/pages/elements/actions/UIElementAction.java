package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.name.NameConverter;

/**
 * Created by Anton Sopyankin
 */
public abstract class UIElementAction<T, E extends UIElement> extends Action<T> {

    protected final E uiElement;

    public UIElementAction(E uiElement) {
        super(uiElement.getTimeout(), uiElement.getPollingTime(), uiElement.getDelay());
        this.uiElement = uiElement;
    }

    protected E getUIObject() {
        return uiElement;
    }

    @Override
    protected ActionException errorHandler(Exception ex) {
        try {
            getUIObject().scrollWindowTo();
        } catch (Exception e) {
        }
        return super.errorHandler(ex);
    }

    @Override
    public String toString() {
        return NameConverter.humanize(this.getClass()) + " \"" + getUIObject() + "\"";
    }

    public T perform(Object... args) throws ActionException {

        IBrowser browser = uiElement.inOpenedBrowser();

        int windowCountBefore = browser.getCountOfWindows();
        T returnObject = super.perform(args);
        int windowCountAfter = browser.getCountOfWindows();

        if (windowCountAfter > windowCountBefore) {
            browser.switchToNextWindow();
        } else if (windowCountAfter < windowCountBefore && windowCountAfter > 0 && browser.hasPreviousWindow()) {
            browser.switchToPreviousWindow();
        }
        return returnObject;
    }
}