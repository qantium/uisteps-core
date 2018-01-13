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
import com.qantium.uisteps.core.browser.pages.Url;
import com.qantium.uisteps.core.lifecycle.Execute;

import java.net.MalformedURLException;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
 * <li>setup.props.file</li>
 * <li>webdriver.driver</li>
 * <li>webdriver.remote.url</li>
 * <li>webdriver.base.url.host</li>
 * <li>webdriver.base.url.port</li>
 * <li>webdriver.base.url.protocol</li>
 * <li>webdriver.base.url.user</li>
 * <li>webdriver.base.url.password</li>
 * <li>webdriver.timeouts.implicitlywait</li>
 * <li>webdriver.timeouts.polling</li>
 * <li>webdriver.timeouts.withDelay</li>
 * <li>meta.info.regexp</li>
 * <li>meta.param.regexp</li>
 * <li>webdriver.proxy = localhost<li>
 * <li>webdriver.proxy = localhost:7777</li>
 * <li>webdriver.proxy = 127.0.0.1:7777</li>
 * <li>webdriver.proxy = :7777</li>
 * <li>webdriver.unexpected.alert.behaviour</li>
 * <li>user.dir</li>
 * <li>home.dir</li>
 * <li>har.take</li>
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
 * <li>browser.download.path</li>
 * <li>testrail.host</li>
 * <li>testrail.login</li>
 * <li>testrail.password</li>
 * <li>testrail.run</li>
 * <li>testrail.statuses</li>
 * <li>testrail.outcome.file</li>
 * <li>run.tables</li>
 * <li>storage.dir</li>
 * <li>retry.attempts</li>
 * <li>retry.withDelay</li>
 * <li>element.decorator.use</li>
 * <li>element.decorator.style</li>
 * </ul>
 *
 * @author Anton Solyankin
 */
public enum UIStepsProperty implements IUIStepsProperty {

    SETUP_PROPS_FILE(PROPS_FILE_PATH),

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
    WEBDRIVER_BASE_URL_PORT,
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
     * Set "webdriver.proxy" to specify ip and port for proxy server.
     *
     * Examples:
     * <ul>
     * <li>localhost</li>
     * <li>localhost:7777</li>
     * <li>127.0.0.1:7777</li>
     * </ul>
     *
     * @see BrowserFactory
     */
    WEBDRIVER_PROXY(() -> {

        String ip = "localhost";
        String port = "";

        String webdriverRemoteUrl = WEBDRIVER_REMOTE_URL.getValue();

        if (isNotEmpty(webdriverRemoteUrl)) {

            try {
                Url url =  new Url(webdriverRemoteUrl);
                ip = url.getHost();
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex.getCause());
            }
        }

        return ip + ":" + port;
    }),
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
    WEBDRIVER_TIMEOUTS_DELAY("0"),
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

    HAR_TAKE(Execute.NONE.name()),
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

    LIST_POLLING_ATTEMPTS("1"),


    TESTRAIL_HOST,
    TESTRAIL_LOGIN,
    TESTRAIL_PASSWORD,
    TESTRAIL_RUN(""),
    TESTRAIL_STATUSES("Passed=1;Blocked=2;Untested=3;Retest=4;Failed=5"),
    /**
     * Set "testrail.outcome.file" to specify path to *.properties file with list of runed testrail cases
     */
    TESTRAIL_OUTCOME_FILE("/target/testrail/tests.properties"),
    RUN_GROUPS,


    /**
     * Set "retry.attempts" to specify number of retries after failed test
     */
    RETRY_ATTEMPTS("0"),
    /**
     * Set "retry.withDelay" to specify withDelay time before retry test in milliseconds
     */
    RETRY_DELAY("0"),

    ELEMENT_DECORATOR_USE(Boolean.FALSE.toString()),
    ELEMENT_DECORATOR_STYLE("border: 2px solid red");

    private final String defaultValue;

    UIStepsProperty() {
        this("");
    }

    UIStepsProperty(Supplier<String> supplier) {
        this(supplier.get());
    }

    UIStepsProperty(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
