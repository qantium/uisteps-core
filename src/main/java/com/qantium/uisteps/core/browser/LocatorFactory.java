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
package com.qantium.uisteps.core.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static org.codehaus.plexus.util.StringUtils.isNotEmpty;

/**
 * @author Anton Solyankin
 */
public class LocatorFactory {

    public By[] getLocators(Field field) {
        try {
            if (field.isAnnotationPresent(FindBy.class)) {
                return new By[]{getLocator(field.getAnnotation(FindBy.class))};
            } else if (field.isAnnotationPresent(FindBys.class)) {
                return getLocators(field.getAnnotation(FindBys.class));
            } else {
                return getLocators(field.getType());
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex + " for " + field);
        }
    }


    public By[] getLocators(Supplier<By[]> supplier) {
        return supplier.get();
    }

    public By[] getLocators(Class<?> uiObject) {

        if (uiObject == Object.class) {
            throw new IllegalArgumentException("Cannot find locator for " + uiObject);
        }

        if (uiObject.isAnnotationPresent(FindBy.class)) {
            return new By[]{getLocator(uiObject.getAnnotation(FindBy.class))};
        } else if (uiObject.isAnnotationPresent(FindBys.class)) {
            return getLocators(uiObject.getAnnotation(FindBys.class));
        }

        try {
            return getLocators(uiObject.getSuperclass());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex + " for " + uiObject);
        }
    }

    public By[] getLocators(Object uiObject) {
        try {
            return getLocators(uiObject.getClass());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex + " for " + uiObject);
        }
    }

    public By[] getLocators(FindBys findBys) {
        return getLocators(findBys.value());
    }

    public By[] getLocators(FindBy[] findBy) {
        By[] bys = new By[findBy.length];

        for(int i = 0; i < findBy.length; i++) {
            bys[i] = getLocator(findBy[i]);
        }

        return bys;
    }

    public By getLocator(FindBy findBy) {

        if (findBy == null) {
            return null;
        }

        How how;
        String using;

        if (isNotEmpty(using = findBy.className())) {
            how = How.CLASS_NAME;
        } else if (isNotEmpty(using = findBy.css())) {
            how = How.CSS;
        } else if (isNotEmpty(using = findBy.id())) {
            how = How.ID;
        } else if (isNotEmpty(using = findBy.linkText())) {
            how = How.LINK_TEXT;
        } else if (isNotEmpty(using = findBy.name())) {
            how = How.NAME;
        } else if (isNotEmpty(using = findBy.partialLinkText())) {
            how = How.PARTIAL_LINK_TEXT;
        } else if (isNotEmpty(using = findBy.tagName())) {
            how = How.TAG_NAME;
        } else if (isNotEmpty(using = findBy.xpath())) {
            how = How.XPATH;
        } else {
            how = findBy.how();
            using = findBy.using();
        }
        return getLocator(how, using);
    }

    public By getLocator(How how, String using) {

        switch (how) {
            case CLASS_NAME:
                return By.className(using);
            case CSS:
                return By.cssSelector(using);
            case ID:
                return By.id(using);
            case LINK_TEXT:
                return By.linkText(using);
            case NAME:
                return By.name(using);
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(using);
            case TAG_NAME:
                return By.tagName(using);
            case XPATH:
                return By.xpath(using);
            default:
                throw new IllegalArgumentException("Cannot find " + how + " " + using + " locator");
        }
    }

}
