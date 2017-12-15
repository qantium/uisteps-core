package com.qantium.uisteps.core.browser.pages.elements.actions;


import com.qantium.uisteps.core.browser.pages.elements.UIElements;

public abstract class UIElementsAction<T, E extends UIElements> extends Action<T> {

    protected final E uiElements;

    public UIElementsAction(E uiElements) {
        super(uiElements.getTimeout(), uiElements.getPollingTime(), uiElements.getDelay());
        this.uiElements = uiElements;
    }

    @Override
    protected ActionException errorHandler(Exception ex) {
        uiElements.refresh();
        return super.errorHandler(ex);
    }
}
