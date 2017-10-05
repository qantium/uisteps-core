package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.name.NameConverter;

/**
 * Created by Anton Sopyankin
 */
public abstract class UIElementAction<T, E extends UIElement> extends Action<T> {

    private final E uiElement;

    public UIElementAction(E uiElement) {
        super(uiElement.getTimeout(), uiElement.getPollingTime(), uiElement.getDelay());
        this.uiElement = uiElement;
    }

    @Override
    protected E getUIObject() {
        return uiElement;
    }

    @Override
    protected ActionException errorHandler(Exception ex) {
        try {
            new ScrollTo(getUIObject()).perform();
        } catch (Exception e) {
        }
        return super.errorHandler(ex);
    }

    @Override
    public String toString() {
        return NameConverter.humanize(this.getClass()) + " \"" + getUIObject() + "\"";
    }
}
