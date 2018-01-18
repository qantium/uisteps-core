/*
 * Copyright 2015 A.Solyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.HtmlObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static com.qantium.uisteps.core.browser.wait.Waiting.*;

/**
 * Contains elements of one type
 *
 * @param <E> specifies the type of elements
 * @author Anton Solyankin
 */
@NotInit
public class UIElements<E extends UIElement> extends UIElement implements Iterable<E> {

    private final Class<E> elementType;
    private LinkedList<E> elements;

    public UIElements(Class<E> elementType) throws IllegalArgumentException {

        if (UIElements.class.isAssignableFrom(elementType)) {
            throw new IllegalArgumentException("UIElements cannot contain other elements with type " + elementType);
        }
        this.elementType = elementType;
    }

    private UIElements<E> withElements(LinkedList<E> elements) {
        this.elements = elements;
        return this;
    }

    public E get(int index) {
        return waitFor(this, () -> getElements().get(index));
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getSearchContext().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getSearchContext().findElement(by);
    }


    protected Class<E> getElementType() {
        return elementType;
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    @Override
    public boolean isDisplayed() {
        return !isEmpty();
    }

    private E[] toArray() {
        return (E[]) getElements().<E>toArray();
    }

    private LinkedList<E> getElements() {
        if (elements == null || elements.isEmpty()) {
            refresh();
        }

        return elements;
    }

    public Stream<E> stream() {
        waitUntilNot(this, () -> {
            refresh();
            return isEmpty();
        });
        return new Stream(getElements().stream());
    }

    public <T extends UIElements<E>> T refresh() {
        elements = new LinkedList<>();
        Iterator<By> iterator = Arrays.asList(getLocators()).iterator();
        while (iterator.hasNext()) {
            try {
                By locator = iterator.next();
                for (WebElement wrappedElement : getContext().findElements(locator)) {
                    if (wrappedElement.isDisplayed()) {
                        E uiElement = get(getElementType(), locator);
                        uiElement.setWrappedElement(wrappedElement);
                        elements.add(uiElement);
                    }
                }
            } catch (Exception ex) {
                if (!iterator.hasNext() && elements.isEmpty()) {
                    throw ex;
                }
            }
        }

        return (T) this;
    }


    @Override
    public Iterator<E> iterator() {
        return getElements().iterator();
    }

    public Iterator<E> descendingIterator() {
        return getElements().descendingIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        getElements().forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return getElements().spliterator();
    }

    public int size() {
        return getElements().size();
    }


    public UIElements<E> getUIElements(LinkedList<E> elements) {
        if (getContext() == null) {
            return inOpenedBrowser().getAll(elementType, getLocators()).withElements(elements);
        } else {
            return getContext().getAll(elementType, getLocators()).withElements(elements);
        }
    }

    //Screenshots
    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot(toArray());
    }

    @Override
    protected HtmlObject getChildContext() {
        return getContext();
    }

    @Override
    public SearchContext getSearchContext() {
        if (getContext() == null) {
            return inOpenedBrowser();
        } else {
            return getContext().getSearchContext();
        }
    }

    @Override
    public void setWrappedElement(WebElement wrappedElement) {
        throw new UnsupportedOperationException("This operation is not supported for UIElements");
    }

    @Override
    public WebElement getWrappedElement() {
        throw new UnsupportedOperationException("This operation is not supported for UIElements");
    }

    @Override
    public String getText() {
        return getText("");
    }

    public String getText(String separator) {

        StringBuffer text = new StringBuffer();
        boolean first = true;

        for (E value : getElements()) {
            if (first) {
                first = false;
            } else {
                text.append(separator);
            }
            text.append(value.getText());
        }
        return text.toString();
    }

    @Override
    public <T extends UIElement> T as(Class<T> type) {
        T as = super.as(type);
        if (as instanceof UIElements) {
            UIElements uiElements = (UIElements) as;
            uiElements.elements = elements;
        }
        return as;
    }

    public E getFirst() {
        return get(0);
    }

    public E getLast() {
        return get(size() - 1);
    }

    public E findFirst(Predicate<E> predicate) {
        return waitFor(this, () -> stream().filter(predicate).findFirst().get());
    }

    public boolean anyMatch(Predicate<E> predicate) {
        Stream<E> stream = stream();
        return waitUntil(this, () -> stream().anyMatch(predicate));
    }

    public boolean allMatch(Predicate<E> predicate) {
        Stream<E> stream = stream();
        return waitUntil(this, () -> stream().allMatch(predicate));
    }

    public static class Stream<E extends UIElement> {

        private final java.util.stream.Stream<E> stream;

        public Stream(java.util.stream.Stream<E> stream) {
            this.stream = stream;
        }

        private Stream<E> stream(java.util.stream.Stream<E> stream) {
            return new Stream<>(stream);
        }

        public Stream<E> filter(Predicate<? super E> predicate) {
            return stream(stream.filter(predicate));
        }

        public Stream<E> sorted(Comparator<? super E> comparator) {
            return stream(stream.sorted(comparator));
        }

        public Stream<E> peek(Consumer<? super E> action) {
            return stream(stream.peek(action));
        }

        public Stream<E> limit(long maxSize) {
            return stream(stream.limit(maxSize));
        }

        public Stream<E> skip(long n) {
            return stream(stream.skip(n));
        }

        public void forEach(Consumer<? super E> action) {
            stream.forEach(action);
        }

        public Optional<E> min(Comparator<? super E> comparator) {
            return stream.min(comparator);
        }

        public Optional<E> max(Comparator<? super E> comparator) {
            return stream.max(comparator);
        }

        public long count() {
            return stream.count();
        }

        public boolean anyMatch(Predicate<? super E> predicate) {
            return stream.anyMatch(predicate);
        }

        public boolean allMatch(Predicate<? super E> predicate) {
            return stream.allMatch(predicate);
        }

        public boolean noneMatch(Predicate<? super E> predicate) {
            return stream.noneMatch(predicate);
        }

        public Optional<E> findFirst() {
            return stream.findFirst();
        }

        public Optional<E> findAny() {
            return stream.findAny();
        }

        public Iterator<E> iterator() {
            return stream.iterator();
        }

        public Stream<E> onClose(Runnable closeHandler) {
            return stream(stream.onClose(closeHandler));
        }

        public void close() {
            stream.close();
        }

        public <R, A> R collect(Collector<? super E, A, R> collector) {
            return stream.collect(collector);
        }
    }

}
