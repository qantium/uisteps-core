package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;

import static com.qantium.uisteps.core.browser.pages.elements.finder.How.*;

/**
 * Created by Anton Solyankin
 */
public class HowCondition<T, E extends UIElement> {

    private final Finder<T, E> finder;
    private boolean ignoreCase;

    public HowCondition(Finder<T, E> finder) {
        this.finder = finder;
    }

    public FindCondition<T, E> containing() {
        return how(CONTAINS);
    }

    public FindCondition<T, E> with() {
        return how(EQUALS);
    }

    public FindCondition<T, E> matching() {
        return how(MATCHES);
    }

    public FindCondition<T, E> startingWith() {
        return how(STARTS_WITH);
    }

    public FindCondition<T, E> endingWith() {
        return how(ENDS_WITH);
    }

    public HowCondition<T, E> ignoringCase() {
        return ignoringCase(true);
    }

    public HowCondition<T, E> ignoringCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    private FindCondition how(How how) {
        getFinder().how(how.ignoreCase(ignoreCase));
        return new FindCondition(this);
    }

    protected Finder<T, E> getFinder() {
        return finder;
    }

    public HowCondition<T, E> not() {
        return not(true);
    }

    public HowCondition<T, E> not(boolean not) {
        finder.not(not);
        return this;
    }
}
