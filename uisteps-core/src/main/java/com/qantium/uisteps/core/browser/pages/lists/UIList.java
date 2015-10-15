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
package com.qantium.uisteps.core.browser.pages.lists;

import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.name.Named;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author A.Solyankin
 * @param <E>
 */
public class UIList<E extends UIObject> extends LinkedList<E> implements Named {

    private String name;

    public UIList(List<E> elements) {
        addAll(elements);

    }

    public boolean isDisplayed() {
        Iterator<E> iterator = iterator();

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
                return "list of " + getFirst().getName();
            } catch (NoSuchElementException ex) {
                return "empty list";
            }
        } else {
            return name;
        }
    }

    public E get(String name) {

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {

            E element = iterator.next();

            if (element.getWrappedElement().getText().equals(name)) {
                return element;
            }
        }

        throw new NoSuchElementException("Cannot find element by name " + name);
    }

    public UIList<E> except(Integer... indexes) {

        for (int index : indexes) {
            remove(index);
        }

        return this;
    }

    public UIList<E> except(String... names) {
        
        for (String n : names) {
            try {
                remove(get(n));
            } catch (NoSuchElementException ex) {
            }
        }
        return this;
    }
}
