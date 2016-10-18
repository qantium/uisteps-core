package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

/**
 * Created by by Anton Solyankin
 */
public class FinderFactory<E extends UIElement> {

    private final UIElements<E> elements;

    public FinderFactory(UIElements<E> elements) {
        this.elements = elements;
    }

    public SingleFinder<E> getSingleFinder() {
        return new SingleFinder(elements);
    }
    public MultiFinder<E> getMultiFinder() {
        return new MultiFinder(elements);
    }

}
