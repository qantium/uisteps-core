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
package com.qantium.uisteps.core.name;

/**
 *
 * @author ASolyankin
 */
public class Name {
    
    private String value;
    private boolean isDefault = true;

    public Name(String value) {
        this.value = value;
    }
    
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return value;
    }

    public void setValue(String value) {
        isDefault = false;
        this.value = value;
    }
    
}
