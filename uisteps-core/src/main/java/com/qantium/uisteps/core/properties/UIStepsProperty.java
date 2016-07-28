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

import com.qantium.uisteps.core.browser.factory.BrowserFactory;
import com.qantium.uisteps.core.lifecycle.Execute;
import com.qantium.uisteps.core.utils.testrail.Action;

import static org.apache.commons.lang3.StringUtils.isEmpty;

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
 * </p>
 * <p>
 * Property can be overrided by another using "AS#" construction, e.g.
 * webdriver.driver = AS#driver
 * </p>
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
 * <li>meta.info.regexp</li>
 * <li>meta.param.regexp</li>
 * <li>webdriver.proxy = localhost<li>
 * <li>webdriver.proxy = localhost:7777</li>
 * <li>webdriver.proxy = 127.0.0.1:7777</li>
 * <li>webdriver.proxy = :7777</li>
 * <li>webdriver.unexpected.alert.behaviour/li>
 * <li>user.dir</li>
 * <li>home.dir</li>
 * <li>source.take</li>
 * <li>source.take.fake</li>
 * <li>source.take.dir</li>
 * <li>screenshots.take</li>
 * <li>screenshots.take.fake</li>
 * <li>screenshots.dir</li>
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
 * <li>testrail.status.codes</li>
 * <li>testrail.outcome.file</li>
 * <li>run.groups</li>
 * <li>com.qantium.uisteps.core.storage.dir</li>
 * <li>retry.attempts</li>
 * <li>retry.delay</li>
 * </ul>
 *
 * @author Anton Solyankin
 * @see com.qantium.uisteps.core.properties.UIStepsProperties
 */
public enum UIStepsProperty implements IUIStepsProperty {

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
     * @see BrowserFactory
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
     * Examples:
     * <ul>
     * <li><li>
     * <li>dismiss</li>
     * <li>ignore</li>
     * </ul>
     */
    WEBDRIVER_UNEXPECTED_ALERT_BEHAVIOUR("accept"),
    /**
     * Set "meta.info.regexp" to specify regexp for step meta
     */
    META_INFO_REGEXP("\\s?META(.*)"),
    /**
     * Set "meta.param.regexp" to specify regexp for parameters in step meta
     */
    META_PARAM_REGEXP("\\[(.+?)=(.*?)\\]"),
    /**
     * Set "home.dir" to specify directory for saved file
     *
     * @see com.qantium.uisteps.core.screenshots.Screenshot#save(java.lang.String)
     * @see com.qantium.uisteps.core.storage.Storage
     */
    HOME_DIR("/target/site"),
    USER_DIR,
    /**
     * Set "source.take" to specify when to take screenshots
     *
     * @see Execute
     */
    SOURCE_TAKE(Execute.FOR_FAILURES.name()),
    SOURCE_TAKE_FAKE("true"),
    SOURCE_DIR(HOME_DIR.getDefaultValue()),
    /**
     * Set "screenshots.take" to specify when to take screenshots
     */
    SCREENSHOTS_TAKE(Execute.FOR_FAILURES.name()),

    /**
     * Set "screenshots.fake" to true, to get fake screenshot if driver is died
     */

    SCREENSHOTS_TAKE_FAKE("true"),
    SCREENSHOTS_DIR (HOME_DIR.getDefaultValue()),
    /**
     * Set "screenshots.scale.width" to specify scaled width of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
     */
    SCREENSHOTS_SCALE_WIDTH("48"),
    /**
     * Set "screenshots.scale.height" to specify scaled height of saved images
     *
     * @see com.qantium.uisteps.core.storage.Storage
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
    BROWSER_WIDTH("max"),
    BROWSER_HEIGHT("max"),

    STORAGE_DIR(HOME_DIR.getDefaultValue()),

    /**
     * @see com.qantium.uisteps.core.utils.testrail.Action
     */
    TESTRAIL_ACTION(Action.UNDEFINED.name().toLowerCase()),
    TESTRAIL_HOST,
    TESTRAIL_USER,
    TESTRAIL_PASSWORD,
    TESTRAIL_RUNS,
    TESTRAIL_STATUS_CODES,
    /**
     * Set "testrail.outcome.file" to specify path to *.properties file with list of runed testrail cases
     */
    TESTRAIL_OUTCOME_FILE("/target/testrail/tests.properties"),
    RUN_GROUPS,

    ZK_ID_MARK("ZK#"),
    ZK_HASH_XPATH("//body/div[1]"),
    ZK_HASH_ATTRIBUTE("id"),
    ZK_HASH_REGEXP("(.*)_"),
    ZK_SHIFT_REGEXP("(\\[(.*?)\\])"),

    /**
     * Set "retry.attempts" to specify number of retries after failed test
     */
    RETRY_ATTEMPTS("0"),
    /**
     * Set "retry.delay" to specify delay time before retry test in milliseconds
     */
    RETRY_DELAY("0");

    private final String defaultValue;

    UIStepsProperty(String defaultValue) {
        String key = this.toString();

        if(isEmpty(System.getProperty(key)) && !isEmpty(defaultValue)) {
            System.setProperty(key, defaultValue);
        }

        this.defaultValue = defaultValue;
    }

    UIStepsProperty() {
        this("");
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }
}
