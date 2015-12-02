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
import com.qantium.uisteps.core.properties.UIStepsProperty;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASolyankin
 */
public class UrlFactory {

    private final String HOST;
    private final String PARAM_REGEXP;
    private final String BASE_URL;

    public UrlFactory() {
        HOST = UIStepsProperties.getProperty(UIStepsProperty.BASE_URL_HOST);
        PARAM_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.PROPERTY_REGEXP);
        BASE_URL = UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_BASE_URL);
    }

    public static Url getUrlOf(Class<?> page) {
        return new UrlFactory().process(new Url(), page);
    }
    
    public Url getUrlOf(Page page, String... params) {
        return processParams(page.getUrl(), params);
    }

    public Url getUrlOf(Class<? extends Page> page, String... params) {
        Url url = process(new Url(), page);
        return processParams(url, params);
    }

    public boolean isRoot(Class<?> page) {
        return page.isAnnotationPresent(Root.class);
    }

    protected Url process(Url url, Class<?> page) {

        if (url.getHost().isEmpty()) {
            url.setHost(BASE_URL);
        }

        if (isRoot(page)) {
            String rootUrl = page.getAnnotation(Root.class).value();
            if (!rootUrl.isEmpty()) {
                url.setHost(rootUrl);
            }
        }

        if (!isRoot(page) && page != Page.class) {
            process(url, page.getSuperclass());
        }

        if (page.isAnnotationPresent(BaseUrl.class)) {
            String defaultUrl = page.getAnnotation(BaseUrl.class).value();

            if (defaultUrl.contains(HOST)) {
                Pattern pattern = Pattern.compile("(.*)" + HOST + "(.*)");
                Matcher matcher = pattern.matcher(defaultUrl);

                if (matcher.find()) {
                    String prefix = matcher.group(1);
                    String postfix = matcher.group(2);

                    if (prefix != null) {
                        url.prependPrefix(prefix);
                    }

                    if (postfix != null) {
                        url.appendPostfix(postfix);
                    }
                }
            } else {
                url.appendPostfix(defaultUrl);
            }
        }
        return url;
    }

    protected Url processParams(Url url, String... params) {
        String urlString = url.toString();
        int paramIndex = 0;

        Pattern pattern = Pattern.compile(PARAM_REGEXP);
        Matcher matcher = pattern.matcher(urlString);

        while (matcher.find()) {

            String key = matcher.group(1);
            String name = matcher.group(2);
            String value = UIStepsProperties.getProperty(name);

            try {
                if (value == null) {
                    value = params[paramIndex];
                    paramIndex++;
                }
                urlString = urlString.replace(key, value);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException("Url " + urlString + " needs more params! Params: " + Arrays.toString(params) + " \nCause:" + ex);
            }
        }

        if (!url.toString().equals(urlString)) {

            try {
                return new Url(urlString);
            } catch (MalformedURLException ex) {
                throw new RuntimeException("Cannot create url from string " + urlString + "\nCause:" + ex);
            }
        } else {
            return url;
        }
    }

}
