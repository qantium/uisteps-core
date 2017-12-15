package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.finder.Find;
import com.qantium.uisteps.core.browser.pages.elements.finder.Finder;
import com.qantium.uisteps.core.browser.pages.elements.finder.HowCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

@NotInit
public class Group<E extends UIElement> extends UIElement {

    private final Class<E> elementType;
    private By[] elementsLocator;
    private UIElements<E> elements;

    public Group(Class<E> elementType, By... elementsLocator) throws IllegalArgumentException {
        this.elementType = elementType;
        this.elementsLocator = elementsLocator;
    }

    @Override
    public <T extends UIElement> T as(Class<T> type) {
        T as = super.as(type);
        if(as instanceof Group) {
            Group group = (Group) as;
            group.withElementsLocator(elementsLocator);
            group.elements = elements;
        }
        return as;
    }

    private UIElements<E> getElements() {
        if(elements == null) {
            elements = getAll(elementType, getElementsLocator());
        }
        return elements;
    }

    protected Class<E> getElementType() {
        return elementType;
    }

    public int getIndexOf(Object content) {
        return getElements().getIndexOf(content);
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

    public Stream<E> stream() {
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

    public UIElements<E> exceptFirst() {
        return getElements().exceptFirst();
    }

    public UIElements<E> exceptLast() {
        return getElements().exceptLast();
    }

    public UIElements<E> subList(int fromIndex, int toIndex) {
        return getElements().subList(fromIndex, toIndex);
    }

    public E getFirst() {
        return getElements().getFirst();
    }

    public E getLast() {
        return getElements().getLast();
    }

    public UIElements<E> except(Integer... indexes) {
        return getElements().except(indexes);
    }

    public HowCondition<E, E> get() {
        return getElements().get();
    }

    public HowCondition<Boolean, E> isDisplayed() {
        return getElements().isDisplayed();
    }

    public HowCondition<UIElements<E>, E> getAll() {
        return getElements().getAll();
    }

    public HowCondition<Boolean, E> contains() {
        return getElements().contains();
    }

    public HowCondition<Boolean, E> allContains() {
        return getElements().allContains();
    }

    public Finder<?, E> by(Find by) {
        return getElements().by(by);
    }

    @Override
    public String getText() {
        return getElements().getText();
    }

    public String getText(String separator) {
        return getElements().getText(separator);
    }

    @Override
    public <T extends AbstractUIObject> T immediately() {
        return getElements().immediately();
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
    public <T extends AbstractUIObject> T pollingEvery(long pollingTime) {
        return getElements().pollingEvery(pollingTime);
    }

    public Group<E> getGroup(List<E> elements) {
        Group<E> group = as(getClass());
        group.elements = getElements().getUIElements(elements);
        return group;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Inherited
    public @interface Elements {
        FindBy[] value();
    }
}
