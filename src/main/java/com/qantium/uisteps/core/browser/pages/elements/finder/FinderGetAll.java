package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

/**
 * Created by Anton Solyankin
 */
public class FinderGetAll<E extends UIElement> extends Finder<UIElements<E>, E> {

    public FinderGetAll(UIElements<E> elements) {
        super(elements);
    }

    @Override
    protected UIElements<E> find() {
        return getAll();
    }
}
