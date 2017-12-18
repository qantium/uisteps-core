package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.wait.TimeoutBuilder;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.lang.annotation.*;
import java.util.*;
import java.util.function.Consumer;

@NotInit
public class Group<E extends UIElement> extends UIElement {

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
    protected Object setValue(LinkedHashMap<String, Object> values) {
        return getElements().setValue(values);
    }

    @Override
    protected Object setValue(Object values) {
        return getElements().setValue(values);
    }

    @Override
    public Object setContent(String key, Object... values) {
        return getElements().setContent(key, values);
    }

    @Override
    public Object setContent(File file) {
        return getElements().setContent(file);
    }

    @Override
    public boolean hasContent(File file) {
        return getElements().hasContent(file);
    }

    @Override
    public Object setContent(Object value) {
        return getElements().setContent(value);
    }

    @Override
    public boolean hasContent(Object content) {
        return getElements().hasContent(content);
    }

    @Override
    public Object setContent(JSONObject json) {
        return getElements().setContent(json);
    }

    @Override
    public boolean hasContent(JSONObject json) {
        return getElements().hasContent(json);
    }

    @Override
    public JSONObject getJsonContent() {
        return getElements().getJsonContent();
    }

    @Override
    public JSONObject getJsonContent(String... keys) {
        return getElements().getJsonContent(keys);
    }

    @Override
    public LinkedHashMap<String, Object> getContent(String... keys) {
        return getElements().getContent(keys);
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
