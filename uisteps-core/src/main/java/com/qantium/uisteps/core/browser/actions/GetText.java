package com.qantium.uisteps.core.browser.actions;

import com.qantium.uisteps.core.browser.pages.UIElement;

/**
 * Created by Anton Solyankin
 */
public class GetText extends Action<String> {

    private final UIElement uiElement;

    public GetText(UIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    protected String apply() {
        if ("input".equals(uiElement.getTagName())) {
            String enteredText = uiElement.getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return uiElement.getWrappedElement().getText();
        }
    }

    @Override
    public String toString() {
        return "click \"" + uiElement + "\"";
    }
}