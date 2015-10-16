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

import java.lang.reflect.Field;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author A.Solyankin
 */
public class LocatorFactory {

    public By getLocator(Class<?> uiObject) {

        if (uiObject == Object.class) {
            throw new RuntimeException("Cannot find locator!");
        }

        if (uiObject.isAnnotationPresent(FindBy.class)) {
            return getLocator(uiObject.getAnnotation(FindBy.class));
        }

        return getLocator(uiObject.getSuperclass());
    }

    public By getLocator(Object uiObject) {
        return getLocator(uiObject.getClass());
    }

    public By getLocator(Field field) {

        if (field.isAnnotationPresent(FindBy.class)) {
            return getLocator(field.getAnnotation(FindBy.class));
        } else {
            return getLocator(field.getType());
        }
    }

    public By getLocator(FindBy findBy) {

        if (findBy == null) {
            return null;
        }

        How how;
        String using;

        if (StringUtils.isEmpty(using = findBy.className())) {
            how = How.CLASS_NAME;
        } else if (StringUtils.isEmpty(using = findBy.css())) {
            how = How.CSS;
        } else if (StringUtils.isEmpty(using = findBy.id())) {
            how = How.ID;
        } else if (StringUtils.isEmpty(using = findBy.linkText())) {
            how = How.LINK_TEXT;
        } else if (StringUtils.isEmpty(using = findBy.name())) {
            how = How.NAME;
        } else if (StringUtils.isEmpty(using = findBy.partialLinkText())) {
            how = How.PARTIAL_LINK_TEXT;
        } else if (StringUtils.isEmpty(using = findBy.tagName())) {
            how = How.TAG_NAME;
        } else if (StringUtils.isEmpty(using = findBy.xpath())) {
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
                throw new RuntimeException("Cannot get " + how + " " + using + " locator!");

        }
    }

}
