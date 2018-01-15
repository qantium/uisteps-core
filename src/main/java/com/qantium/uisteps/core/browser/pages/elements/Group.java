package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.browser.pages.HtmlObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.wait.WithTimeout;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    public Iterator<E> descendingIterator() {
        return getElements().descendingIterator();
    }

    public UIElements<E> getUIElements(LinkedList<E> elements) {
        return getElements().getUIElements(elements);
    }

    @Override
    public HtmlObject getChildContext() {
        return this;
    }

    public E getFirst() {
        return getElements().getFirst();
    }

    public E getLast() {
        return get(size() - 1);
    }

    public E findFirst(Predicate<E> predicate) {
        return getElements().findFirst(predicate);
    }

    public boolean anyMatch(Predicate<E> predicate) {
        return getElements().anyMatch(predicate);
    }

    public boolean allMatch(Predicate<E> predicate) {
        return getElements().allMatch(predicate);
    }

    @Override
    public long getTimeout() {
        return getElements().getTimeout();
    }

    @Override
    public long getPollingTime() {
        return getElements().getPollingTime();
    }

    @Override
    public long getDelay() {
        return getElements().getDelay();
    }

    @Override
    public <T extends AbstractUIObject> T withDelay(long delay) {
        getElements().withDelay(delay);
        return (T) this;
    }

    @Override
    public <T extends AbstractUIObject> T withTimeout(long timeout) {
        getElements().withTimeout(timeout);
        return (T) this;
    }

    @Override
    public <T extends AbstractUIObject> T pollingEvery(long pollingTime) {
        getElements().pollingEvery(pollingTime);
        return (T) this;
    }

    @Override
    public <T extends AbstractUIObject> T flushTimeouts() {
        getElements().flushTimeouts();
        return (T) this;
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
