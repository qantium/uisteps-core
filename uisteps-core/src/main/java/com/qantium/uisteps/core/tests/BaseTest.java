/*
 * Copyright 2016 ASolyankin.
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

import com.qantium.uisteps.core.storage.Storage;
import com.qantium.uisteps.core.verify.Assume;
import com.qantium.uisteps.core.verify.Verify;

/**
 *
 * @author ASolyankin
 */
public class BaseTest {

    public final Verify verify = getAssertions();
    public final Assume assume = getAssumtions();
    public final Storage storage = getStorage();

    protected Verify getAssertions() {
        return new Verify();
    }

    protected Assume getAssumtions() {
        return new Assume();
    }

    protected Storage getStorage() {
        return new Storage();
    }

}
