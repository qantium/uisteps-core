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
 * Contains settings that can be set before test running
 * <p>
 * Property can be overrided in "uisteps.properties" file or in another file
 * whitch path is set in "properties.path" property. Both files must be in
 * working or/and home directory. Properties in working directory override
 * properties in home directory. Working directory is taken from
 * System.getProperty("user.dir"). Home directory is taken from
 * System.getProperty("user.home") At last all properties can be oveerided in
 * "uisteps.local.properties". The rules for it are same with
 * "uisteps.properties" file
 * <p>
 * Property can be overrided by another using "AS#" construction, e.g.
 * webdriver.driver = AS#driver
 * <p>
 * @see UIStepsProperty.PROPERTIES_PATH
 * @see UIStepsProperty.WEBDRIVER_DRIVER
 * @see UIStepsProperty.WEBDRIVER_REMOTE_URL
 * @see UIStepsProperty.WEBDRIVER_BASE_URL_HOST
 * @see UIStepsProperty.WEBDRIVER_BASE_URL_PROTOCOL
 * @see UIStepsProperty.WEBDRIVER_BASE_URL_USER
 * @see UIStepsProperty.WEBDRIVER_BASE_URL_PASSWORD
 * @see UIStepsProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT
 * @see UIStepsProperty.HOME_DIR
 * @see UIStepsProperty.SCREENSHOTS_SCALE_WIDTH
 * @see UIStepsProperty.SCREENSHOTS_SCALE_HEIGHT
 * @see UIStepsProperty.BASE_URL_HOST
 * @see UIStepsProperty.NULL_VALUE
 * @see UIStepsProperty.PROPERTY_REGEXP
 * @see UIStepsProperty.BROWSER_WIDTH
 * @see UIStepsProperty.BROWSER_HEIGHT
 *
 * @see com.qantium.uisteps.core.properties.UIStepsProperties
 *
 * @author A.Solyankin
 */
public enum UIStepsProperty {

    /**
     * Set "properties.path" to specify in what file alternative properties are
     * set. Path can be relative or absolute
     */
    PROPERTIES_PATH {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.driver" to specify with what driver by default browser
     * will be opened
     */
    WEBDRIVER_DRIVER {

                @Override
                public String getDefault() {
                    return "firefox";
                }
            },
    /**
     * Set "webdriver.remote.url" to specify url for remote driver browser will
     * be opened
     */
    WEBDRIVER_REMOTE_URL {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.base.url.host" to specify base host for pages
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_HOST {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.base.url.protocol" to specify base protocol for page url
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_PROTOCOL {

                @Override
                public String getDefault() {
                    return "http";
                }
            },
    /**
     * Set "webdriver.base.url.user" to specify user for basic authorization to
     * page
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_USER {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.base.url.password" to specify password for basic
     * authorization to page
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_PASSWORD {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.proxy" to specify ip and port for proxy server
     * <p>
     * Examples:
     * <ul>
     * <li>webdriver.proxy = localhost<li>
     * <li>webdriver.proxy = localhost:7777</li>
     * <li>webdriver.proxy = 127.0.0.1:7777</li>
     * <li>webdriver.proxy = :7777</li>
     * </ul>
     *
     * @see com.qantium.uisteps.core.browser.BrowserFactory
     */
    WEBDRIVER_PROXY {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "webdriver.timeouts.implicitlywait" to specify implicitly wait
     * timeouts
     */
    WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT {

                @Override
                public String getDefault() {
                    return "3000";
                }
            },
    /**
     * Set "home.dir" to specify directory for saved file
     *
     * @see
     * com.qantium.uisteps.core.screenshots.Screenshot#save(java.lang.String)
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    HOME_DIR {

                @Override
                public String getDefault() {
                    return "target/site";
                }
            },
    /**
     * Set "screenshots.scale.width" to specify scaled width of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    SCREENSHOTS_SCALE_WIDTH {

                @Override
                public String getDefault() {
                    return "48";
                }
            },
    /**
     * Set "screenshots.scale.height" to specify scaled height of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    SCREENSHOTS_SCALE_HEIGHT {

                @Override
                public String getDefault() {
                    return "48";
                }
            },
    /**
     * Set "base.url.host" to specify string for host part in BaseUrl
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    BASE_URL_HOST {

                @Override
                public String getDefault() {
                    return "#HOST";
                }
            },
    /**
     * Set "base.url.host" to specify string for null values
     */
    NULL_VALUE {

                @Override
                public String getDefault() {
                    return "#NULL";
                }
            },
    /**
     * Set "property.regexp" to specify regexp for parameters in url
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    PROPERTY_REGEXP {

                @Override
                public String getDefault() {
                    return "(\\[(.*?)\\])";
                }
            },
    /**
     * Set "browser.width" to specify width of opened browser
     *
     * @see com.qantium.uisteps.core.browser.BrowserFactory
     */
    BROWSER_WIDTH {

                @Override
                public String getDefault() {
                    return "";
                }
            },
    /**
     * Set "browser.height" to specify height of opened browser
     *
     * @see com.qantium.uisteps.core.browser.BrowserFactory
     */
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

    /**
     * @return String default value of propety
     */
    public abstract String getDefault();

}
