package com.qantium.uisteps.core.browser.pages.elements.finder.facades;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.browser.pages.elements.finder.Find;
import com.qantium.uisteps.core.browser.pages.elements.finder.Finder;
import com.qantium.uisteps.core.browser.pages.elements.finder.FinderFactory;

import static com.qantium.uisteps.core.browser.pages.elements.finder.Find.CSS;

/**
 * Created by Anton Solyankin
 */
public abstract class FinderFacade<T, E extends UIElement> {

    private final Finder<E> finder;

    public FinderFacade(UIElements<E> elements) {
        finder = new FinderFactory(elements).getSingleFinder();
    }

    public Finder<E> by(Find by) {
        return finder.by(by);
    }

    public abstract T byText(String... values);

    public abstract T byCSS(String attribute, String... values);

    public abstract T byHTML(String... values);

    public abstract T byAttribute(String attribute, String... values);

}
