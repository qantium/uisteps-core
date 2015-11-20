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

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ASolyankin
 */
public class Storage {

    private final Map map;
    private final static ThreadLocal<Map> threadLocalMap = new ThreadLocal();

    public Storage(Map map) {
        this.map = map;
    }

    public Storage() {
        threadLocalMap.set(new ConcurrentHashMap());
        map = null;
    }

    private Map getMap() {

        if (map != null) {
            return map;
        } else {
            return threadLocalMap.get();
        }
    }

    public File remember(File file) {
        return remember(file.getPath(), file);
    }
    
    public <T> T remember(String key, T value) {
        getMap().put(key, value);
        return value;
    }

    public <T> T remember(T value) {
        return remember(value.getClass().getName(), value);
    }

    public <T> T remembered(String key) {
        return (T) getMap().get(key);
    }

    public <T> T remembered(Class<T> key) {
        return remembered(key.getName());
    }

}
