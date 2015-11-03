/*
 * Copyright 2015 ASolyankin.
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
package com.qantium.uisteps.core.run.tests;

import com.qantium.uisteps.core.user.User;
import com.qantium.uisteps.core.user.UserFactory;

/**
 *
 * @author ASolyankin
 */
public class MultiUserTest extends JUnitTest {

    public final UserFactory users;

    public MultiUserTest() {
        this(new JUnitListener());
    }

    public MultiUserTest(JUnitListener listener) {
        this.users = new UserFactory();
    }

    public User by(String user) {
        return users.by(user);
    }

    public void add(String user) {
        users.add(user);
    }

    public void add(String name, Class<? extends User> user) {
        users.add(name, user);
    }

    public <T extends User> T by(Class<T> user) {
        return users.by(user);
    }

    public <T extends User> T by(String name, Class<T> user) {
        return users.by(name, user);
    }

}
