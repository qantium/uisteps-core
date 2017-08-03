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
import com.qantium.uisteps.core.browser.pages.elements.finder.*;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.function.Consumer;

/**
 * Contains elements of one type
 *
 * @param <E> specifies the type of elements
 * @author Anton Solyankin
 */
@NotInit
public class UIElements<E extends UIElement> extends UIElement implements Cloneable, Iterable<E> {

    private final Class<E> elementType;
    private ArrayList<E> elements;

    public UIElements(Class<E> elementType) throws IllegalArgumentException {

        if (UIElements.class.isAssignableFrom(elementType)) {
            throw new IllegalArgumentException("UIElements cannot contain other elements with type " + elementType);
        }
        this.elementType = elementType;
    }

    public UIElements(Class<E> elementType, List<E> elements) throws IllegalArgumentException {
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

    protected Class<E> getElementType() {
        return elementType;
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    @Override
    public boolean isCurrentlyDisplayed() {
        return !isEmpty();
    }

    protected E[] toArray() {
        return (E[]) getElements().<E>toArray();
    }

    private List<E> getElements() {

        if (elements != null) {
            return elements;
        } else {
            refresh();
            return elements;
        }
    }

    public <T extends UIElements<E>> T refresh() {
        elements = new ArrayList<>();
        Iterator<By> iterator = Arrays.asList(getLocators()).iterator();

        while (iterator.hasNext()) {

            try {
                By locator = iterator.next();

                for (WebElement wrappedElement : getSearchContext().findElements(locator)) {
                    E uiElement = get(getElementType(), locator);
                    uiElement.setWrappedElement(wrappedElement);
                    elements.add(uiElement);
                }
            } catch (Exception ex) {
                if (!iterator.hasNext() && elements.isEmpty()) {
                    throw ex;
                }
            }
        }

        return (T) this;
    }

    public E get(int index) {
        return getElements().get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return getElements().iterator();
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

    public UIElements<E> exceptFirst() {
        return except(0);
    }

    public UIElements<E> exceptLast() {
        return except(-1);
    }

    public UIElements<E> subList(int fromIndex, int toIndex) {
        List<E> subList = clone().getElements().subList(fromIndex, toIndex);
        return getUIElements(subList);
    }

    public E getFirst() {
        return get(0);
    }

    public E getLast() {
        return get(size() - 1);
    }

    public UIElements<E> except(Integer... indexes) {

        Set<Integer> proxyIndexes = new HashSet();

        for (int index : indexes) {
            if (index < 0) {
                index = size() - 1;
            }
            proxyIndexes.add(index);
        }

        List<E> proxyList = new ArrayList<>();
        for (int i = 0; i < proxyIndexes.size(); i++) {

            if (!proxyIndexes.contains(i)) {
                proxyList.add(getElements().get(i));
            }
        }

        return getUIElements(proxyList);
    }

    public UIElements<E> getUIElements(List<E> elements) {
        return new UIElements(elementType, elements);
    }

    @Override
    public UIElements<E> clone() {
        ArrayList<E> cloned = (ArrayList<E>) ((ArrayList<E>) getElements()).clone();
        return getUIElements(cloned);
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

    public HowCondition<E, E> get() {
        FinderGet finder = new FinderGet(this);
        return new HowCondition(finder);
    }

    public HowCondition<Boolean, E> isDisplayed() {
        FinderIsDisplayed finder = new FinderIsDisplayed(this);
        return new HowCondition(finder);
    }

    public HowCondition<UIElements<E>, E> getAll() {
        FinderGetAll finder = new FinderGetAll(this);
        return new HowCondition(finder);
    }

    public HowCondition<Boolean, E> contains() {
        Finder finder = new FinderContains(this);
        return new HowCondition(finder);
    }

    public HowCondition<Boolean, E> allContains() {
        Finder finder = new FinderContainsAll(this);
        return new HowCondition(finder);
    }

    public Finder<?, E> by(Find by) {
        return new Finder(this).by(by);
    }

    @Override
    public void setWrappedElement(WebElement wrappedElement) {
        throw new UnsupportedOperationException("This operation is not supported for UIElements");
    }

    @Override
    public WebElement getWrappedElement() {
        throw new UnsupportedOperationException("This operation is not supported for UIElements");
    }


}
