package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.MethodNotSupportedException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Anton Solyankin
 */
public class Finder<T, E extends UIElement> {

    protected final UIElements<E> elements;
    protected Find by;
    protected How how;
    protected String[] values;
    protected String attribute;

    public Finder(UIElements<E> elements) {
        this.elements = elements;
    }

    public E get() {
        for (E element : elements) {
            for (String value : values) {
                if (how.isFound(by.get(element, attribute), value)) {
                    return element;
                }
            }
        }
        String error = getError();
        throw new NoSuchElementException(error);
    }

    public boolean contains() {
        try {
            get();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public UIElements<E> getAll() {
        List<E> found = new ArrayList();

        for (E element : elements) {
            for (String value : values) {
                if (how.isFound(by.get(element, attribute), value)) {
                    found.add(element);
                }
            }
        }
        return elements.getUIElements(found);
    }

    public boolean containsAll() {
        try {
            get();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public Finder<T, E> by(Find by) {
        this.by = by;
        return this;
    }

    public Finder<T, E> how(How how) {
        this.how = how;
        return this;
    }

    public Finder<T, E> attribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public Finder<T, E> values(String... values) {
        this.values = values;
        return this;
    }

    protected String getError() {
        StringBuilder error = new StringBuilder();

        error.append("Cannot find element by ").append(by);

        if (!StringUtils.isEmpty(attribute)) {
            error.append(" ").append(attribute);
        }

        error.append(" ").append(how)
                .append(" ").append(values);

        return error.toString();
    }

    protected T find()  {
        throw new UnsupportedOperationException("This method must be overridden!");
    }

}
