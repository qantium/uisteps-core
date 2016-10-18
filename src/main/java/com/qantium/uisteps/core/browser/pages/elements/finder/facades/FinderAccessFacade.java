package com.qantium.uisteps.core.browser.pages.elements.finder.facades;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

import static com.qantium.uisteps.core.browser.pages.elements.finder.Find.*;

/**
 * Created by Anton Solyankin
 */
public class FinderAccessFacade<E extends UIElement> extends FinderFacade<E, E> {

    public FinderAccessFacade(UIElements<E> elements) {
        super(elements);
    }

    public E byText(String... values) {
        return by(TEXT).values(values).get();
    }

    public E byCSS(String attribute, String... values) {
        return by(CSS).attribute(attribute).values(values).get();
    }

    public E byHTML(String... values) {
        return by(HTML).values(values).values(values).get();
    }

    public E byAttribute(String attribute, String... values) {
        return by(ATTRIBUTE).attribute(attribute).values(values).get();
    }
}
