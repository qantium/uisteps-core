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

import com.qantium.uisteps.core.screenshots.Screenshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 *
 * Contains elemnets of one type
 *
 * @author A.Solyankin
 * @param <E> specifies the type of elements
 */
public class UIElements<E extends UIElement> extends UIElement {

    private final Class<E> elementType;

    public UIElements(Class<E> elementType) throws IllegalArgumentException {

        if (UIElements.class.isAssignableFrom(elementType)) {
            throw new IllegalArgumentException("UIElements cannot contain other elemnts with type " + elementType);
        }
        this.elementType = elementType;
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
        return uiElements(getElements().subList(fromIndex, toIndex));
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
        E element = getElements().get(0);
        return element.withName("first " + element.getName());
    }

    public E getLast() {
        E element = getElements().get(size() - 1);
        return element.withName("last " + element.getName());
    }

    public E[] toArray() {
        return (E[]) getElements().<E>toArray();
    }

    public List<E> getElements() {
        ArrayList<E> elements = new ArrayList();
        List<WebElement> webElements = getSearchContext().findElements(getLocator());

        for (WebElement element : webElements) {
            E uiElement = inOpenedBrowser().displayed(getElementType(), getLocator(), getContext(), element);
            elements.add(uiElement);
        }
        return elements;
    }

    public E get(int index) {
        E element = getElements().get(index);
        return element.withName(element.getName() + " by index " + index);
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

    protected E get(Find find, How how, String... attribute) {

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {

            E element = iterator.next();
            boolean isFound = false;

            WebElement wrappedElement = element.getWrappedElement();
            String attr;

            if (find == Find.TEXT) {
                attr = wrappedElement.getText();
            } else if (find == Find.ATTRIBUTE) {
                attr = wrappedElement.getAttribute(attribute[0]);
            } else {
                attr = wrappedElement.getCssValue(attribute[0]);
            }

            if ((how == How.EQUAL && attr.equals(attribute[1])) || (how == How.CONTAINS && attr.contains(attribute[1]))) {
                isFound = true;
            }

            if (isFound) {
                return element.withName(element.getName() + " with " + how + " " + attr);
            }
        }

        throw new NoSuchElementException("Cannot find element by attribute " + Arrays.toString(attribute));
    }

    public E getByText(String value) {
        return get(Find.TEXT, How.EQUAL, value);
    }

    public E getByTextContains(String value) {
        return get(Find.TEXT, How.CONTAINS, value);
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

    public UIElements<E> exceptFromIncluding(int index) {
        return exceptInnerIncluding(index, size());
    }

    public UIElements<E> exceptFrom(int index) {
        return exceptInnerIncluding(index + 1, size());
    }

    public UIElements<E> exceptToIncluding(int index) {
        return exceptInnerIncluding(0, index);
    }

    public UIElements<E> exceptTo(int index) {
        return exceptInnerIncluding(0, index - 1);
    }

    public UIElements<E> exceptInnerIncluding(int fromIndex, int toIndex) {
        return exceptInner(fromIndex - 1, toIndex + 1);
    }

    public UIElements<E> exceptOuterIncluding(int fromIndex, int toIndex) {
        return exceptOuter(fromIndex + 1, toIndex - 1);
    }

    public UIElements<E> exceptInner(int fromIndex, int toIndex) {
        List<E> proxyList = getProxyElements();
        proxyList.subList(fromIndex, toIndex).clear();
        return uiElements(proxyList);
    }

    public UIElements<E> exceptOuter(int fromIndex, int toIndex) {
        return uiElements(getProxyElements().subList(fromIndex, toIndex));
    }

    public UIElements<E> except(Integer... indexes) {

        List<E> proxyList = getProxyElements();

        for (int index : indexes) {
            proxyList.remove(index);
        }
        return uiElements(proxyList);
    }

    private UIElements<E> uiElements(List<E> proxyElements) {
        UIElements uiElements = new UIElements(elementType);
        uiElements.addAll(proxyElements);
        return uiElements;
    }

    public void addAll(Collection<E> c) {
        getElements().addAll(c);
    }

    public void addAll(UIElements uiElements) {
        addAll(uiElements.getProxyElements());
    }

    @Override
    public SearchContext getSearchContext() {

        if (getContext() != null) {
            return getContext().getSearchContext();
        } else {
            return inOpenedBrowser().getDriver();
        }
    }

    private List<E> getProxyElements() {
        List<E> proxyElements = new ArrayList();
        proxyElements.addAll(getElements());
        return proxyElements;
    }

    //Screenshots
    @Override
    public Screenshot takeScreenshot() {
        return inOpenedBrowser().takeScreenshot(toArray());
    }
}
