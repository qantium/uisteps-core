package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Anton Solyankin
 */
public class MultiFinder<E extends UIElement> extends Finder<E> {

    public MultiFinder(UIElements<E> elements) {
        super(elements);
    }

    @Override
    public UIElements<E> get() {
        List<E> found = new ArrayList();

        for (E element : elements) {
            for(String value: values) {
                if (how.isFound(by.get(element, attribute), value)) {
                    found.add(element);
                }
            }
        }
        return elements.getUIElements(found);
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
