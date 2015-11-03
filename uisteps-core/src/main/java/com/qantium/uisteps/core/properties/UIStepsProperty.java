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
package com.qantium.uisteps.core.properties;

/**
 *
 * @author A.Solyankin
 */
public enum UIStepsProperty {

    UISTEPS_PROPERTIES_PATH {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    WEBDRIVER_DRIVER {

                @Override
                public String getDefault() {
                    return "firefox";
                }
            },
    WEBDRIVER_BASE_URL {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT {
                @Override
                public String getDefault() {
                    return "3000";
                }
            },
    UISTEPS_VARIABLE_HOST {

                @Override
                public String getDefault() {
                    return "#HOST";
                }
            },
    UISTEPS_VARIABLE_PARAM {

                @Override
                public String getDefault() {
                    return "#PARAM";
                }
            };

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }

    public abstract String getDefault();

}
