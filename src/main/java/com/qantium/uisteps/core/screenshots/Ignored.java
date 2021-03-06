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
package com.qantium.uisteps.core.screenshots;

import com.google.common.collect.Lists;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Anton Solyankin
 */
public class Ignored {

    public final Ignore elements;
    private List<By> locators;
    private List<UIElement> uiElements;
    private List<Coords> areas;

    public Ignored(Ignore ignored, By... locators) {
        this.elements = ignored;
        this.locators = Lists.newArrayList(locators);
    }

    public Ignored(Ignore ignored, UIElement... elements) {
        this.elements = ignored;
        uiElements = new ArrayList();

        for (UIElement element : elements) {
            if (element instanceof UIElements) {
                Iterator iterator = ((UIElements) element).iterator();

                while (iterator.hasNext()) {
                    UIElement uiElement = (UIElement) iterator.next();
                    uiElements.add(uiElement);
                }
            } else {
                uiElements.add(element);
            }
        }
    }

    public Ignored(Ignore ignored, Coords... areas) {
        this.elements = ignored;
        this.areas = Lists.newArrayList(areas);
    }

    public List<By> getLocators() {
        return locators;
    }

    public List<UIElement> getElements() {
        return uiElements;
    }

    public List<Coords> getAreas() {
        return areas;
    }

    public List getList() {

        switch (elements) {
            case LOCATORS:
                return getLocators();
            case ELEMENTS:
                return getElements();
            case AREAS:
                return getAreas();
            default:
                return null;
        }
    }

    //ignore
    public static Ignored ignore(By... locators) {
        return new Ignored(Ignore.LOCATORS, locators);
    }

    public static Ignored ignore(UIElement... elements) {
        return new Ignored(Ignore.ELEMENTS, elements);
    }

    public static Ignored ignore(Coords... areas) {
        return new Ignored(Ignore.AREAS, areas);
    }

    public static Ignored ignore(int width, int height) {
        return ignore(new Coords(width, height));
    }

    public static Ignored ignore(int x, int y, int width, int height) {
        return ignore(new Coords(x, y, width, height));
    }

    @Override
    public String toString() {
        return "ignoring " + elements.toString().toLowerCase() + ":" + getList();
    }

}
