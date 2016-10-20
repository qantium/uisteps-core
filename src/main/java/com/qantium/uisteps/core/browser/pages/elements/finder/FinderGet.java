package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

/**
 * Created by Anton Solyankin
 */
public class FinderGet<E extends UIElement> extends Finder<E, E> {

    public FinderGet(UIElements<E> elements) {
        super(elements);
    }

    @Override
    protected E find() {
        return get();
    }
}
