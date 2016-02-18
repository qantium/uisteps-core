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
package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.screenshots.Screenshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Contains elements of one type
 *
 * @param <E> specifies the type of elements
 * @author A.Solyankin
 */
@NotInit
public class UIElements<E extends UIElement> extends UIElement implements Cloneable {

    private final Class<E> elementType;
    private ArrayList<E> elements;

    public UIElements(Class<E> elementType) throws IllegalArgumentException {

        if (UIElements.class.isAssignableFrom(elementType)) {
            throw new IllegalArgumentException("UIElements cannot contain other elements with type " + elementType);
        }
        this.elementType = elementType;
    }

    protected UIElements(Class<E> elementType, List<E> elements) throws IllegalArgumentException {
        this(elementType);
        this.elements = new ArrayList();
        this.elements.addAll(elements);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getSearchContext().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getSearchContext().findElement(by);
    }

    public Class<E> getElementType() {
        return elementType;
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    public Iterator<E> iterator() {
        return getElements().iterator();
    }

    public UIElements<E> subList(int fromIndex, int toIndex) {
        List<E> subList = clone().getElements().subList(fromIndex, toIndex);
        return getUIElements(subList);
    }

    @Override
    public boolean isDisplayed() {
        if (isEmpty()) {
            return false;
        }

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {

            if (!iterator.next().isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    public E getFirst() {
        return get(0);
    }

    public E getLast() {
        return get(size() - 1);
    }

    public E[] toArray() {
        return (E[]) getElements().<E>toArray();
    }

    public UIElements<E> reset() {
        elements = new ArrayList();

        for (WebElement element : findElements(getLocator())) {
            E uiElement = inOpenedBrowser().init(getElementType(), getContext(), getLocator());
            uiElement.setWrappedElement(element);
            elements.add(uiElement);
        }
        return this;
    }

    public List<E> getElements() {
        if (elements == null) {
            reset();
        }
        return elements;
    }

    public E get(int index) {
        return getElements().get(index);
    }

    public E getByAttributeContains(String attribute, String value) {
        return get(Find.ATTRIBUTE, How.CONTAINS, attribute, value);
    }

    public E getByAttribute(String attribute, String value) {
        return get(Find.ATTRIBUTE, How.EQUAL, attribute, value);
    }

    public E getByCSSPropertyContains(String attribute, String value) {
        return get(Find.CSS, How.CONTAINS, attribute, value);
    }

    public E getByCSSProperty(String attribute, String value) {
        return get(Find.CSS, How.EQUAL, attribute, value);
    }

    protected E get(Find find, How how, String attribute, String value) {

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {

            E element = iterator.next();
            boolean isFound = false;

            WebElement wrappedElement = element.getWrappedElement();
            String attr;

            if (find == Find.TEXT) {
                attr = wrappedElement.getText();
            } else if (find == Find.ATTRIBUTE) {
                attr = wrappedElement.getAttribute(value);
            } else {
                attr = wrappedElement.getCssValue(value);
            }


            switch (how) {
                case CONTAINS:
                    isFound = attr.contains(value);
                    break;
                case EQUAL:
                    isFound = attr.equals(value);
                    break;
            }

            if (isFound) {
                return element;
            }
        }

        throw new NoSuchElementException("Cannot find element by attribute " + attribute + " " + how + " " + value);
    }

    public E getByText(String value) {
        return get(Find.TEXT, How.EQUAL, "", value);
    }

    public E getByTextContains(String value) {
        return get(Find.TEXT, How.CONTAINS, "", value);
    }

    public UIElements<E> exceptFirst() {
        return except(0);
    }

    protected enum Find {

        ATTRIBUTE {
            private String attr;

            public String get() {
                return attr;
            }

            public void set(String attr) {
                this.attr = attr;
            }
        }, CSS {
            private String attr;

            public String get() {
                return attr;
            }

            public void set(String attr) {
                this.attr = attr;
            }
        }, TEXT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

    }

    protected enum How {

        CONTAINS, EQUAL;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

    }

    public UIElements<E> exceptLast() {
        return except(size() - 1);
    }

    public int size() {
        return getElements().size();
    }

    public UIElements<E> except(Integer... indexes) {

        UIElements<E> clonedUIElements = clone();
        List<E> proxyList = clonedUIElements.getElements();

        for (int index : indexes) {
            proxyList.remove(index);
        }
        return clonedUIElements;
    }

    protected UIElements<E> getUIElements(List<E> elements) {
        return new UIElements(elementType, elements);
    }

    @Override
    public UIElements<E> clone() {
        ArrayList<E> cloned = (ArrayList<E>) elements.clone();
        return new UIElements(elementType, cloned);
    }

    //Screenshots
    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot(toArray());
    }
}
