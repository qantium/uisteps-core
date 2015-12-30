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

import com.qantium.uisteps.core.user.User;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 *
 * @author A.Solyankin
 * @param <U> specifies the type of user
 */
public class BaseUserTest<U extends User> extends BaseTest {

    public final U user;

    public BaseUserTest(Class<U> user) {

        try {
            this.user = (U) ConstructorUtils.invokeConstructor(user, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + user + ".\nCause: " + ex);
        }
    }
}
