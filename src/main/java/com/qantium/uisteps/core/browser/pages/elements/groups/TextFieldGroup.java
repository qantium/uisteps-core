package com.qantium.uisteps.core.browser.pages.elements.groups;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.elements.Group;
import com.qantium.uisteps.core.browser.pages.elements.TextField;

@NotInit
public class TextFieldGroup extends Group<TextField> {

    public TextFieldGroup() throws IllegalArgumentException {
        super(TextField.class);
    }
}
