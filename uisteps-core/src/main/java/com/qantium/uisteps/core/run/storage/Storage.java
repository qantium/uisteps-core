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
package com.qantium.uisteps.core.run.storage;

import com.qantium.uisteps.core.name.Named;
import java.util.Map;

/**
 *
 * @author ASolyankin
 */
public class Storage implements WithStorage {

    private final Map map;

    public Storage(Map map) {
        this.map = map;
    }

    @Override
    public <T> T remember(String key, T value) {
        map.put(key, value);
        return value;
    }

    @Override
    public <T> T remember(T value) {
        return remember(value.getClass().getName(), value);
    }

    @Override
    public <T> T remembered(String key) {
        return (T) map.get(key);
    }

    @Override
    public <T> T remembered(Class<T> key) {
        return remembered(key.getName());
    }

}
