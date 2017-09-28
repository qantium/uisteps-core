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
        E element = get();
        element.setFinder(this);
        return element;
    }

    public FinderGet<E> clone(UIElements<E> elements) {

        FinderGet cloned = new FinderGet(elements);
        cloned.by = by;
        cloned.how = how;
        cloned.values = values;
        cloned.attribute = attribute;

        return new FinderGet(elements);
    }
}
