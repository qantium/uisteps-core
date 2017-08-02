package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.UnhandledAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
        long startTime = System.currentTimeMillis();
        long timeDelta;
        sleep(elements.getDelay());

        do {
            try {
                for (E element : elements) {
                    for (String value : values) {
                        if (how.isFound(by.get(element, attribute), value)) {
                            return element;
                        }
                    }
                }
            } catch (NoBrowserException | UnhandledAlertException ex) {
                throw ex;
            } catch (Exception ex) {
                sleep(elements.getPollingTime());
            }
            long currentTime = System.currentTimeMillis();
            timeDelta = currentTime - startTime;
        } while (timeDelta <= elements.getTimeout());

        throw new NoSuchElementException(getError());
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean contains() {
        try {
            get();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public boolean waitUntilIsDisplayed() {
        try {
            return get().isCurrentlyDisplayed();
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

    public boolean allContains() {
        return getAll().size() == elements.size();
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

        error.append("Cannot find element by ").append(by.name().toLowerCase());

        if (isNotEmpty(attribute)) {
            error.append(" ").append(attribute);
        }

        error.append(" ").append(how);

        for (String value : values) {
            error.append(" ").append(value);
        }

        return error.toString();
    }

    protected T find() {
        throw new UnsupportedOperationException("This method must be overridden!");
    }

}
