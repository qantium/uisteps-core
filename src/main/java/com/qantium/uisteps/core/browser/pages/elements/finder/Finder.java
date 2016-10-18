package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Anton Solyankin
 */
public abstract class Finder<E extends UIElement> {

    protected final UIElements<E> elements;
    protected Find by;
    protected How how;
    protected String[] values;
    protected String attribute;

    public Finder(UIElements<E> elements) {
        this.elements = elements;
    }

    public abstract E get();

    public abstract boolean contains();

    public Finder<E> by(Find by) {
        this.by = by;
        return this;
    }

    public Finder<E> how(How how) {
        this.how = how;
        return this;
    }

    public Finder<E> attribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public Finder<E> values(String... values) {
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
}
