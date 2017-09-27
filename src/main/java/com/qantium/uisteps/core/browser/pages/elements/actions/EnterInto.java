package com.qantium.uisteps.core.browser.pages.elements.actions;

import com.qantium.uisteps.core.browser.pages.elements.TextField;

/**
 * Created by Anton Solyankin
 */
public class EnterInto extends TypeInto {

    public EnterInto(TextField input, Object text) {
        super(input, text);
    }

    @Override
    protected Object apply(Object... args) {
        if(textIsNotEmpty()) {
            new Clear(getUIObject()).apply(args);
        }
        return super.apply(args);
    }

    @Override
    protected boolean textIsNotEmpty() {
        return  !getNullValue().equals(getText());
    }

    @Override
    public String toString() {
        return "enter \"" + getText() + "\" into \"" + getUIObject() + "\"";
    }
}
