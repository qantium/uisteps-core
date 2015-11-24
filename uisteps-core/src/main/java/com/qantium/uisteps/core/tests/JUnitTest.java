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
package com.qantium.uisteps.core.tests;

import com.qantium.uisteps.core.name.Named;
import com.qantium.uisteps.core.storage.Storage;
import com.qantium.uisteps.core.verify.Assume;
import com.qantium.uisteps.core.verify.Verify;
import org.junit.runner.RunWith;

/**
 *
 * @author ASolyankin
 */
@RunWith(JUnitRunner.class)
public class JUnitTest {

    public final Verify verify;
    public final Assume assume;
    public final Storage storage;

    public JUnitTest(Verify verify, Assume assume, Storage storage) {
        this.verify = verify;
        this.assume = assume;
        this.storage = storage;
    }

    public JUnitTest() {
        this(new Verify(), new Assume(), new Storage());
    }

    public <T> T remember(String key, T value) {
        return storage.put(key, value);
    }

    public <T extends Named> T remember(T value) {
        return storage.put(value);
    }

    public <T> T remember(T value) {
        return storage.put(value);
    }

    public <T> T remembered(String key) {
        return storage.get(key);
    }

    public <T> T remembered(Class<T> key) {
        return storage.get(key);
    }
}
