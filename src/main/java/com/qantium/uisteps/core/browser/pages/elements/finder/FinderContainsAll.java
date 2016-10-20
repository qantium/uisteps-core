package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

/**
 * Created by Anton Solyankin
 */
public class FinderContainsAll<E extends UIElement> extends Finder<Boolean, E> {

    public FinderContainsAll(UIElements<E> elements) {
        super(elements);
    }

    @Override
    protected Boolean find() {
        return containsAll();
    }
}
