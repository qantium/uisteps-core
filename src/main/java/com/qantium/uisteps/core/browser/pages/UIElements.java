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

import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_TIMEOUTS_POLLING;
import static com.qantium.uisteps.core.properties.UIStepsProperty.LIST_POLLING_ATTEMPS;

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
    private int listPollingAttempts;


    public UIElements(Class<E> elementType) throws IllegalArgumentException {

        if (UIElements.class.isAssignableFrom(elementType)) {
            throw new IllegalArgumentException("UIElements cannot contain other elements with type " + elementType);
        }
        this.elementType = elementType;

        listPollingAttempts = getListPollingAttempts();

        if(listPollingAttempts < 1) {
            listPollingAttempts = 1;
        }
    }

    protected UIElements(Class<E> elementType, List<E> elements) throws IllegalArgumentException {
        this(elementType);
        this.elements = new ArrayList();
        this.elements.addAll(elements);
    }

    @Override
    public void setBrowser(IBrowser browser) {
        super.setBrowser(browser);
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

    public UIElements<E> subList(int fromIndex, int toIndex) {
        List<E> subList = clone().getElements().subList(fromIndex, toIndex);
        return getUIElements(subList);
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    @Override
    public boolean isCurrentlyDisplayed() {
        return !isEmpty();
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

    public List<E> getElements() {
        if (elements != null) {
            return elements;
        } else {
            ArrayList<E> list = new ArrayList();
            By locator = getLocator();
            Class<E> elementType = getElementType();

            for(WebElement wrappedElement: getSearchContext().findElements(locator)) {
                E uiElement = super.get(elementType, locator);
                uiElement.setWrappedElement(wrappedElement);
                list.add(uiElement);
            }
            return list;
        }
    }

    public E get(int index) {

        long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
        long counter = 0;
        long breakCounter = 0;
        int elementsSize = 0;

        while (counter <= timeout) {
            List<E> list = getElements();

            if (elementsSize == list.size() && breakCounter >= listPollingAttempts * pollingTime) {
                break;
            }

            try {
                return list.get(index);
            } catch (Exception ex) {
                sleep(pollingTime);
                counter += pollingTime;
                breakCounter += pollingTime;
            }
        }
        throw new NoSuchElementException("Cannot find element by index " + index);
    }

    public boolean containsByAttributeContains(String attribute, String value) {
        try {
            getByAttributeContains(attribute, value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public E getByAttributeContains(String attribute, String value) {
        return get(Find.ATTRIBUTE, How.CONTAINS, attribute, value);
    }

    public boolean containsByAttrite(String attribute, String value) {
        try {
            getByAttribute(attribute, value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public E getByAttribute(String attribute, String value) {
        return get(Find.ATTRIBUTE, How.EQUAL, attribute, value);
    }

    public boolean containsByCSSPropertyContains(String attribute, String value) {
        try {
            getByCSSPropertyContains(attribute, value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public E getByCSSPropertyContains(String attribute, String value) {
        return get(Find.CSS, How.CONTAINS, attribute, value);
    }

    public boolean containsByCSSProperty(String attribute, String value) {
        try {
            getByCSSProperty(attribute, value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public E getByCSSProperty(String attribute, String value) {
        return get(Find.CSS, How.EQUAL, attribute, value);
    }

    protected E get(Find find, How how, String attribute, String value) {

        long timeout = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT));
        long pollingTime = Integer.parseInt(getProperty(WEBDRIVER_TIMEOUTS_POLLING));
        long counter = 0;
        long breakCounter = 0;
        int elementsSize = 0;

        while (counter <= timeout) {
            boolean isFound = false;
            List<E> elements = getElements();

            if (elementsSize == elements.size() && breakCounter >= 2 * pollingTime) {
                break;
            }
            elementsSize = elements.size();
            for (E element : elements) {

                WebElement wrappedElement = element.getWrappedElement();
                String attr;

                switch (find) {
                    case TEXT:
                        attr = wrappedElement.getText();
                        break;
                    case ATTRIBUTE:
                        attr = wrappedElement.getAttribute(value);
                        break;
                    default:
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
            sleep(pollingTime);
            counter += pollingTime;
            breakCounter += pollingTime;
        }
        throw new NoSuchElementException("Cannot find element by " + find + " " + attribute + " " + how + " " + value);
    }

    private void sleep(long pollingTime) {
        try {
            Thread.sleep(pollingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean containsByText(String value) {
        try {
            getByText(value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public E getByText(String value) {
        return get(Find.TEXT, How.EQUAL, "", value);
    }

    public boolean containsByTextContains(String value) {
        try {
            getByTextContains(value);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
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
        ArrayList<E> cloned = (ArrayList<E>) ((ArrayList<E>) getElements()).clone();
        return new UIElements(elementType, cloned);
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

    public int getListPollingAttempts() {
        return Integer.parseInt(getProperty(LIST_POLLING_ATTEMPS));
    }

    public void setListPollingAttempts(int listPollingAttempts) {
        this.listPollingAttempts = listPollingAttempts;
    }
}
