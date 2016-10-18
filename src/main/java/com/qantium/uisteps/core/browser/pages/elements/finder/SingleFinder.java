package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

import java.util.NoSuchElementException;

/**
 * Created by Anton Solyankin
 */
public class SingleFinder<E extends UIElement> extends Finder<E> {

    public SingleFinder(UIElements<E> elements) {
        super(elements);
    }

    @Override
    public E get() {
        for (E element : elements) {
            for(String value: values) {
                if (how.isFound(by.get(element, attribute), value)) {
                    return element;
                }
            }
        }

        String error = getError();
        throw new NoSuchElementException(error);
    }

    @Override
    public boolean contains() {
        try {
            get();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
