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

    PROPERTIES_PATH {

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
    WEBDRIVER_REMOTE_URL {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    WEBDRIVER_BASE_URL {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    WEBDRIVER_PROXY{

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
    HOME_DIR {

                @Override
                public String getDefault() {
                    return "target/site";
                }
            },
    SCREENSHOTS_SCALE_WIDTH {

                @Override
                public String getDefault() {
                    return "48";
                }
            },
    SCREENSHOTS_SCALE_HEIGHT {

                @Override
                public String getDefault() {
                    return "48";
                }
            },
    HOST {

                @Override
                public String getDefault() {
                    return "#HOST";
                }
            },
    NULL {

                @Override
                public String getDefault() {
                    return "#NULL";
                }
            },
    PROPERTY_REGEXP {

                @Override
                public String getDefault() {
                    return "(\\[(.*?)\\])";
                }
            },
    BROWSER_WIDTH {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    BROWSER_HEIGHT {

                @Override
                public String getDefault() {
                    return "";
                }
            };

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }

    public abstract String getDefault();

}
