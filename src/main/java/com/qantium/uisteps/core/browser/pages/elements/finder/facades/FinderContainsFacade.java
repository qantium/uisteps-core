package com.qantium.uisteps.core.browser.pages.elements.finder.facades;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;

import static com.qantium.uisteps.core.browser.pages.elements.finder.Find.*;

/**
 * Created by Anton Solyankin
 */
public class FinderContainsFacade<E extends UIElement> extends FinderFacade<Boolean, E> {

    public FinderContainsFacade(UIElements<E> elements) {
        super(elements);
    }

    public Boolean byText(String... values) {
        return by(TEXT).values(values).contains();
    }

    public Boolean byCSS(String attribute, String... values) {
        return by(CSS).attribute(attribute).values(values).contains();
    }

    public Boolean byHTML(String... values) {
        return by(HTML).values(values).values(values).contains();
    }

    public Boolean byAttribute(String attribute, String... values) {
        return by(ATTRIBUTE).attribute(attribute).values(values).contains();
    }
}
