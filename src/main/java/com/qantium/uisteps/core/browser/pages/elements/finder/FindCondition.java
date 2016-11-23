package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.pages.UIElement;

import static com.qantium.uisteps.core.browser.pages.elements.finder.Find.*;

/**
 * Created by Anton Solyankin
 */
public class FindCondition<T, E extends UIElement> {

    private final HowCondition<T, E> how;

    public FindCondition(HowCondition<T, E> how) {
        this.how = how;
    }

    public T text(String... values) {
        return getFinder().by(TEXT).values(values).find();
    }

    public T css(String attribute, String... values) {
        return getFinder().by(CSS).attribute(attribute).values(values).find();
    }

    public T html(String... values) {
        return getFinder().by(HTML).values(values).find();
    }

    public T attribute(String attribute, String... values) {
        return getFinder().by(ATTRIBUTE).attribute(attribute).values(values).find();
    }

    public Finder<T, E> getFinder() {
        return how.getFinder();
    }
}