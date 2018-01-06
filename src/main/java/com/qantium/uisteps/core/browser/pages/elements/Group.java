package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.wait.TimeoutBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@NotInit
public class Group<E extends UIElement> extends UIElement implements Iterable<E> {

    private final Class<E> elementType;
    private By[] elementsLocator;
    @NotInit
    private UIElements<E> elements;

    public Group(Class<E> elementType, By... elementsLocator) throws IllegalArgumentException {
        this.elementType = elementType;
        this.elementsLocator = elementsLocator;
    }


    @Override
    public <T extends UIElement> T as(Class<T> type) {
        T as = super.as(type);
        if (as instanceof Group) {
            Group group = (Group) as;
            group.withElementsLocator(elementsLocator);
            group.elements = elements;
        }
        return as;
    }

    private UIElements<E> getElements() {
        if (elements == null) {
            elements = getAll(elementType, getElementsLocator());
        }
        return elements;
    }

    protected Class<E> getElementType() {
        return elementType;
    }

    public <T extends Group> T withElementsLocator(By... elementLocator) {
        this.elementsLocator = elementLocator;
        return (T) this;
    }

    public By[] getElementsLocator() {
        return elementsLocator;
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    public UIElements.Stream<E> stream() {
        return getElements().stream();
    }

    public Group<E> refresh() {
        getElements().refresh();
        return this;
    }

    public E get(int index) {
        return getElements().get(index);
    }

    public Iterator<E> iterator() {
        return getElements().iterator();
    }

    public void forEach(Consumer<? super E> action) {
        getElements().forEach(action);
    }

    public Spliterator spliterator() {
        return getElements().spliterator();
    }

    public int size() {
        return getElements().size();
    }

    @Override
    public String getText() {
        return getElements().getText();
    }

    public String getText(String separator) {
        return getElements().getText(separator);
    }

    @Override
    public TimeoutBuilder getTimeoutBuilder() {
        return getElements().getTimeoutBuilder();
    }

    public Group<E> getGroup(List<E> elements) {
        Group<E> group = as(getClass());
        group.elements = getElements().getUIElements(new LinkedList<>(elements));
        return group;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Inherited
    public @interface Elements {
        FindBy[] value();
    }
}
