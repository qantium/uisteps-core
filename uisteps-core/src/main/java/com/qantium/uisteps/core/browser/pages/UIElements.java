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

import com.qantium.uisteps.core.name.Named;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

/**
 *
 * @author A.Solyankin
 * @param <E>
 */
public class UIElements<E extends UIObject> implements Named {

    private String name;
    private final LinkedList<E> elements = new LinkedList();

    public UIElements(List<E> elements) {
        this.elements.addAll(elements);
    }

    public boolean isDisplayed() {
        Iterator<E> iterator = elements.iterator();

        while (iterator.hasNext()) {

            if (!iterator.next().isDisplayed()) {
                return false;
            }
        }
        return true;

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {

        if (StringUtils.isEmpty(name)) {

            try {
                return "list of " + elements.getFirst().getName();
            } catch (NoSuchElementException ex) {
                return "empty list";
            }
        } else {
            return name;
        }
    }

    public E getFirst() {
        E element = elements.getFirst();
        return element.withName("first "  + element.getName());
    }

    public E getLast() {
        E element = elements.getLast();
        return element.withName("last "  + element.getName());
    }

    public int size() {
        return elements.size();
    }

    public E[] toArray() {
        return (E[]) elements.toArray();
    }

    public E get(int index) {
        E element = elements.get(index);
        return element.withName(element.getName() + " by index " + index);
    }

    public E get(String attribute, String value) {

        Iterator<E> iterator = elements.iterator();

        while (iterator.hasNext()) {

            E element = iterator.next();

            if (element instanceof WrapsElement) {

                WebElement wrappedElement = ((WrapsElement) element).getWrappedElement();
                String attr;

                if (attribute.equals("text")) {
                    attr = wrappedElement.getText();
                } else {
                    attr = wrappedElement.getAttribute(attribute);
                }

                if (attr.equals(value)) {
                    return element.withName(element.getName() + " with " + attribute +  " " + attr);
                }
            }
        }

        throw new NoSuchElementException("Cannot find element by attribute " + value);
    }

    public E get(String value) {
        return get("text", value);
    }

    public UIElements<E> exceptFirst() {
        return except(0);
    }

    public UIElements<E> exceptLast() {
        return except(size() - 1);
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
        LinkedList<E> proxyList = getProxyList();
        proxyList.subList(fromIndex, toIndex).clear();
        return uiElements(proxyList);
    }

    public UIElements<E> exceptOuter(int fromIndex, int toIndex) {
        return uiElements(getProxyList().subList(fromIndex, toIndex));
    }

    public UIElements<E> except(Integer... indexes) {

        LinkedList<E> proxyList = getProxyList();

        for (int index : indexes) {
            elements.remove(index);
        }

        return uiElements(proxyList);
    }

    private UIElements<E> uiElements(List<E> proxyElements) {
        return new UIElements(proxyElements);
    }

    private LinkedList<E> getProxyList() {
        LinkedList<E> proxyElements = new LinkedList();
        proxyElements.addAll(elements);
        return proxyElements;
    }
}
