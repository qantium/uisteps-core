package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Solyankin
 */
public class GetText extends UIElementAction<String, UIElement> {


    public GetText(UIElement uiElement) {
        super(uiElement);
    }

    @Override
    protected String apply(Object... args) {
        if ("input".equals(getUIObject().getTagName())) {
            String enteredText = getUIObject().getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return getUIObject().getWrappedElement().getText();
        }
    }

    @Override
    public String toString() {
        return "get text from \"" + getUIObject() + "\"";
    }
}