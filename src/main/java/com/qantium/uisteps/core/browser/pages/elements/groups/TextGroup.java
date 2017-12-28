package com.qantium.uisteps.core.browser.pages.elements.groups;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.elements.Group;
import com.qantium.uisteps.core.browser.pages.elements.TextBlock;

@NotInit
public class TextGroup extends Group<TextBlock> {

    public TextGroup() throws IllegalArgumentException {
        super(TextBlock.class);
    }
}
