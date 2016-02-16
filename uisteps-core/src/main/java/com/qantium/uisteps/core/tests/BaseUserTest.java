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
package com.qantium.uisteps.core.tests;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.BrowserManager;
import com.qantium.uisteps.core.browser.Driver;
import com.qantium.uisteps.core.browser.NoBrowserException;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.user.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.conditions.DisplayCondition;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

/**
 * @param <U> specifies the type of user
 * @author A.Solyankin
 */
public class BaseUserTest<U extends User> extends BaseTest {

    public final U user;

    public BaseUserTest(Class<U> user) {
        this.user = instatiate(user);
    }

    protected <U extends User> U instatiate(Class<U> user) {
        try {
            return ConstructorUtils.invokeConstructor(user, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + user + ".\nCause: " + ex);
        }
    }
}
