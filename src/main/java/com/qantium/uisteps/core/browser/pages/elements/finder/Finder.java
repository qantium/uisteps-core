package com.qantium.uisteps.core.browser.pages.elements.finder;

import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.UnhandledAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.qantium.uisteps.core.browser.wait.DisplayWaiting.startTime;
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
    private boolean initFinder;
    private boolean not;

    public Finder(UIElements<E> elements) {
        this.elements = elements;
    }

    public void not() {
        not(true);
    }

    public void not(boolean not) {
        this.not = not;
    }

    public E get() {

        sleep(elements.getDelay());

        if (startTime.get() < 0) {
            initFinder = true;
            startTime.set(System.currentTimeMillis());
        }

        if (System.currentTimeMillis() - startTime.get() > elements.getTimeout()) {
            throw new IllegalStateException("Timeout " + elements.getTimeout() + " is exceeded");
        }

        while (System.currentTimeMillis() - startTime.get() <= elements.getTimeout()) {
            try {
                for (E element : elements) {
                    for (String value : values) {
                        boolean isFound = how.isFound(by.get(element, attribute), value);

                        if ((!not && isFound) || (not && !isFound)) {
                            initFinder();
                            return element;
                        }
                    }
                }
            } catch (NoBrowserException | UnhandledAlertException ex) {
                initFinder();
                throw ex;
            } catch (Exception ex) {
                sleep(elements.getPollingTime());
                elements.refresh();
            }
        }
        initFinder();
        throw new NoSuchElementException(getError());
    }

    private void initFinder() {
        if (initFinder) {
            initFinder = false;
            startTime.set(-1L);
        }
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
            return not;
        }
    }

    protected boolean waitUntilIsDisplayed() {
        try {
            if (not) {
                return get().isNotCurrentlyDisplayed();
            } else {
                return get().isCurrentlyDisplayed();
            }
        } catch (NoSuchElementException ex) {
            return not;
        }
    }

    public UIElements<E> getAll() {
        List<E> found = new ArrayList();

        for (E element : elements) {
            for (String value : values) {

                boolean isFound = how.isFound(by.get(element, attribute), value);
                if ((!not && isFound) || (not && !isFound)) {
                    found.add(element);
                }
            }
        }
        return elements.getUIElements(found);
    }

    public boolean allContains() {
        if (not) {
            return getAll().size() != elements.size();
        } else {
            return getAll().size() == elements.size();
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
