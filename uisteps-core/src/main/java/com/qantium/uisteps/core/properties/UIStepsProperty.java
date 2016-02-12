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

import com.qantium.uisteps.core.tests.listeners.Execute;
import com.qantium.uisteps.core.utils.testrail.TestRail;

/**
 * Contains settings that can be set before test running
 * <p/>
 * Property can be overrided in "uisteps.properties" file or in another file
 * whitch path is set in "properties.path" property. Both files must be in
 * working or/and home directory. Properties in working directory override
 * properties in home directory. Working directory is taken from
 * System.getProperty("user.dir"). Home directory is taken from
 * System.getProperty("user.home") At last all properties can be oveerided in
 * "uisteps.local.properties". The rules for it are same with
 * "uisteps.properties" file
 * <p/>
 * Property can be overrided by another using "AS#" construction, e.g.
 * webdriver.driver = AS#driver
 * <p/>
 * List of properties:
 * <ul>
 * <li>properties.path</li>
 * <li>webdriver.driver</li>
 * <li>webdriver.remote.url</li>
 * <li>webdriver.base.url.host</li>
 * <li>webdriver.base.url.protocol</li>
 * <li>webdriver.base.url.user</li>
 * <li>webdriver.base.url.password</li>
 * <li>webdriver.timeouts.implicitlywait</li>
 * <li>webdriver.timeouts.polling</li>
 * <li>steps.meta.regexp</li>
 * <li>steps.meta.param.regexp</li>
 * <li>webdriver.proxy = localhost<li>
 * <li>webdriver.proxy = localhost:7777</li>
 * <li>webdriver.proxy = 127.0.0.1:7777</li>
 * <li>webdriver.proxy = :7777</li>
 * <li>home.dir</li>
 * <li>source.take</li>
 * <li>screenshots.take</li>
 * <li>screenshots.scale.width</li>
 * <li>screenshots.scale.height</li>
 * <li>base.url.host</li>
 * <li>null.value</li>
 * <li>base.url.param.regexp</li>
 * <li>base.url.param.value.regexp</li>
 * <li>browser.width</li>
 * <li>browser.height</li>
 * <li>testrail.action</li>
 * <li>testrail.host</li>
 * <li>testrail.user</li>
 * <li>testrail.password</li>
 * <li>testrail.runs</li>
 * <li>testrail.outcome.file</li>
 * <li>run.groups</li>
 * </ul>
 *
 * @author Anton Solyankin
 * @see com.qantium.uisteps.core.properties.UIStepsProperties
 */
public enum UIStepsProperty {

    /**
     * Set "properties.path" to specify in what file alternative properties are
     * set. Path can be relative or absolute
     */
    /**
     * Set "properties.path" to specify in what file alternative properties are
     * set. Path can be relative or absolute
     */
    PROPERTIES_PATH,
    WEBDRIVER_DRIVER("firefox"),
    /**
     * Set "webdriver.remote.url" to specify url for remote driver
     */
    WEBDRIVER_REMOTE_URL,
    /**
     * Set "webdriver.base.url.host" to specify base host for pages
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_HOST,
    /**
     * Set "webdriver.base.url.protocol" to specify base protocol for page url
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_PROTOCOL("http"),
    /**
     * Set "webdriver.base.url.user" to specify user for basic authorization to
     * page
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_USER,
    /**
     * Set "webdriver.base.url.password" to specify password for basic
     * authorization to page
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    WEBDRIVER_BASE_URL_PASSWORD,
    /**
     * Set "webdriver.proxy" to specify ip and port for proxy server
     * <p/>
     * Examples:
     * <ul>
     * <li>localhost<li>
     * <li>localhost:7777</li>
     * <li>127.0.0.1:7777</li>
     * <li>:7777</li>
     * </ul>
     *
     * @see com.qantium.uisteps.core.browser.BrowserFactory
     */
    WEBDRIVER_PROXY,
    /**
     * Set "webdriver.timeouts.implicitlywait" to specify implicitly wait in milliseconds
     * timeouts
     */
    WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT("3000"),
    /**
     * Set "webdriver.timeouts.polling" to specify polling time in milliseconds for implicitly wait
     * timeouts
     */
    WEBDRIVER_TIMEOUTS_POLLING("250"),
    /**
     * Set "steps.meta.regexp" to specify regexp for step meta
     */
    STEPS_META_REGEXP("\\s?META(.*)"),
    /**
     * Set "steps.meta.param.regexp" to specify regexp for parameters in step meta
     */
    STEPS_META_PARAM_REGEXP("\\[(.+?)=(.*?)\\]"),
    /**
     * Set "home.dir" to specify directory for saved file
     *
     * @see com.qantium.uisteps.core.screenshots.Screenshot#save(java.lang.String)
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    HOME_DIR("target/site"),
    /**
     * Set "source.take" to specify when to take screenshots
     *
     * @see com.qantium.uisteps.core.tests.listeners.Execute
     */
    SOURCE_TAKE(Execute.FOR_FAILURES.name()),
    /**
     * Set "screenshots.take" to specify when to take screenshots
     */
    SCREENSHOTS_TAKE(Execute.FOR_FAILURES.name()),
    /**
     * Set "screenshots.scale.width" to specify scaled width of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    SCREENSHOTS_SCALE_WIDTH("48"),
    /**
     * Set "screenshots.scale.height" to specify scaled height of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
     * @see com.qantium.uisteps.core.storage.Saved
     */
    SCREENSHOTS_SCALE_HEIGHT("48"),
    /**
     * Set "base.url.host" to specify string for host part in BaseUrl
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    BASE_URL_HOST("#HOST"),
    /**
     * Set "null.value" to specify string for null values
     */
    NULL_VALUE("#NULL"),
    /**
     * Set "base.url.param.regexp" to specify regexp for parameters in url
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    BASE_URL_PARAM_REGEXP("(\\[(.*?)\\])"),
    /**
     * Set "base.url.param.value.regexp" to specify regexp for key-value
     * parameters in url
     *
     * @see com.qantium.uisteps.core.browser.pages.BaseUrl
     * @see com.qantium.uisteps.core.browser.pages.UrlFactory
     */
    BASE_URL_PARAM_VALUE_REGEXP("(.+?)=(.*)"),
    BROWSER_WIDTH,
    BROWSER_HEIGHT,

    TESTRAIL_ACTION(TestRail.Action.UNDEFINED.name()),
    TESTRAIL_HOST,
    TESTRAIL_USER,
    TESTRAIL_PASSWORD,
    TESTRAIL_RUNS,
    /**
     * Set "testrail.outcome.file" to specify path to *.properties file with list of runed testrail cases
     */
    TESTRAIL_OUTCOME_FILE("/target/testrail/tests.properties"),
    RUN_GROUPS,

    ZK_ID_MARK("ZK#"),
    ZK_HASH_XPATH("//body/div[1]"),
    ZK_HASH_ATTRIBUTE("id"),
    ZK_HASH_REGEXP("(.*)_");

    public final String defaultValue;

    UIStepsProperty(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    UIStepsProperty() {
        this("");
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }
}
