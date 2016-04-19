/*
 * Copyright 2014 ASolyankin.
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
package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 *
 * @author ASolyankin
 */
public class UrlFactory {

    private final String HOST;
    private final String PARAM_REGEXP;
    private final String PARAM_VALUE_REGEXP;
    private final String BASE_HOST;
    private final String BASE_PROTOCOL;
    private final String BASE_USER;
    private final String BASE_PASSWORD;

    public UrlFactory() {
        HOST = UIStepsProperties.getProperty(BASE_URL_HOST);
        PARAM_REGEXP = UIStepsProperties.getProperty(BASE_URL_PARAM_REGEXP);
        PARAM_VALUE_REGEXP = UIStepsProperties.getProperty(BASE_URL_PARAM_VALUE_REGEXP);
        BASE_HOST = UIStepsProperties.getProperty(WEBDRIVER_BASE_URL_HOST);
        BASE_PROTOCOL = UIStepsProperties.getProperty(WEBDRIVER_BASE_URL_PROTOCOL);
        BASE_USER = UIStepsProperties.getProperty(WEBDRIVER_BASE_URL_USER);
        BASE_PASSWORD = UIStepsProperties.getProperty(WEBDRIVER_BASE_URL_PASSWORD);
    }

    public Url getUrlOf(Page page) {
        
        if(page.getUrl() != null) {
            return getUrlOf(page.getClass(), page.getUrl(), new HashMap());
        } else {
            return getUrlOf(page.getClass(), new HashMap());
        }
    }

    public Url getUrlOf(Page page, Map<String, String> params) {
        
        if(page.getUrl() != null) {
            return getUrlOf(page.getClass(), page.getUrl(), params);
        } else {
            return getUrlOf(page.getClass(), params);
        }
    }

    public Url getUrlOf(Page page, String[] params) {

        if (!ArrayUtils.isEmpty(params)) {
            Map<String, String> paramsMap = new HashMap();
            parseParams(params, paramsMap);
            return getUrlOf(page, paramsMap);
        } else {
            return getUrlOf(page);
        }
    }

    public Url getUrlOf(Class<? extends Page> page) {
        return getUrlOf(page, new HashMap());
    }

    public Url getUrlOf(Class<? extends Page> page, String[] params) {

        if (!ArrayUtils.isEmpty(params)) {
            Map<String, String> paramsMap = new HashMap();
            parseParams(params, paramsMap);
            return getUrlOf(page, paramsMap);
        } else {
            return getUrlOf(page);
        }
    }

    public Url getUrlOf(Class<? extends Page> page, Map<String, String> params) {
        return getUrlOf(page, new Url(), params);
    }

    protected Url getUrlOf(Class<? extends Page> page, Url url, Map<String, String> params) {
        Map<String, String> paramsMap = new HashMap();

        process(url, page, paramsMap);
        
        processParams(url, paramsMap);
        processParams(url, UIStepsProperties.getProperties());
        
        if (!params.isEmpty()) {
            processParams(url, params);
        }
        
        return url;
    }

    public boolean isRoot(Class<?> page) {
        return page.isAnnotationPresent(Root.class);
    }

    protected void parseParams(String[] urlParams, Map<String, String> params) {
        for (String param : urlParams) {
            Pattern pattern = Pattern.compile(PARAM_VALUE_REGEXP);
            Matcher matcher = pattern.matcher(param);

            String paramName;
            String paramValue;

            if (matcher.matches()) {
                paramName = matcher.group(1);
                paramValue = matcher.group(2);
            } else {
                throw new IllegalArgumentException("Parameter " + param + " has illegal format! Must be name=value");
            }
            params.put(paramName, paramValue);
        }
    }

    protected void process(Url url, Class<?> page, Map<String, String> params) {

        if (params == null) {
            params = new HashMap();
        }

        if (!isRoot(page)) {
            process(url, page.getSuperclass(), params);
        }

        if (page.isAnnotationPresent(BaseUrl.class)) {
            BaseUrl baseUrlAnnotation = page.getAnnotation(BaseUrl.class);

            String protocol = baseUrlAnnotation.protocol();
            String host = baseUrlAnnotation.host();
            String user = baseUrlAnnotation.user();
            String password = baseUrlAnnotation.password();
            String urlValue = baseUrlAnnotation.value();
            String[] urlParams = baseUrlAnnotation.params();
            if (!StringUtils.isEmpty(protocol)) {
                url.setProtocol(protocol);
            }

            if (!StringUtils.isEmpty(host)) {
                url.setHost(host);
            }

            if (!StringUtils.isEmpty(user)) {
                url.setUser(user);
            }

            if (!StringUtils.isEmpty(password)) {
                url.setPassword(password);
            }

            if (!ArrayUtils.isEmpty(urlParams)) {
                parseParams(urlParams, params);
            }

            if (!StringUtils.isEmpty(urlValue)) {
                if (urlValue.contains(HOST)) {
                    Pattern pattern = Pattern.compile("(.*)" + HOST + "(.*)");
                    Matcher matcher = pattern.matcher(urlValue);

                    if (matcher.find()) {
                        String prefix = matcher.group(1);
                        
                        String postfix = matcher.group(2);
                        
                        if (!StringUtils.isEmpty(prefix)) {
                            url.prependPrefix(prefix);
                        }

                        if (!StringUtils.isEmpty(postfix)) {
                            url.appendPostfix(postfix);
                        }
                    }
                } else {
                    url.appendPostfix(urlValue);
                }
            }
        }

        if (StringUtils.isEmpty(url.getHost())) {
            url.setHost(BASE_HOST);
        }

        if (StringUtils.isEmpty(url.getProtocol())) {
            url.setProtocol(BASE_PROTOCOL);
        }

        if (StringUtils.isEmpty(url.getUser())) {
            url.setUser(BASE_USER);
        }

        if (StringUtils.isEmpty(url.getPassword())) {
            url.setPassword(BASE_PASSWORD);
        }
    }

    protected void processParams(Url url, Map params) {
        url.setPrefix(processParams(url.getPrefix(), params));
        url.setPostfix(processParams(url.getPostfix(), params));
    }

    protected String processParams(String input, Map params) {

        Pattern pattern = Pattern.compile(PARAM_REGEXP);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {

            String key = matcher.group(1);
            String name = matcher.group(2);
            if (params.containsKey(name)) {
                String value = params.get(name).toString();
                input = input.replace(key, value);
            }
        }
        
        return input;
    }
}
