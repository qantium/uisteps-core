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
    private final String PARAM;
    private final String BASE_URL;

    public UrlFactory() {
        HOST = UIStepsProperties.getProperty(UIStepsProperty.UISTEPS_VARIABLE_HOST);
        PARAM = UIStepsProperties.getProperty(UIStepsProperty.UISTEPS_VARIABLE_PARAM);
        BASE_URL = UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_BASE_URL);
    }

    public static Url getUrlOf(Class<?> page) {
        return new UrlFactory().process(new Url(), page);
    }

    public Url getUrlOf(Class<? extends Page> page, String... params) {
        Class<?> pageClass = getPageClass(page);
        Url url = process(new Url(), pageClass);
        return processParams(process(url, pageClass), params);
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

        if (!isRoot(page) && page != Object.class) {
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

        while (urlString.contains(PARAM)) {

            try {
                urlString = urlString.replaceFirst(PARAM, params[paramIndex]);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException("Url " + urlString + " needs more params! Params: " + Arrays.toString(params) + " \nCause:" + ex);
            }
            paramIndex++;
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

    protected Class<?> getPageClass(Class<?> page) {

        if (page.getName().contains("$$")) {
            return getPageClass(page.getSuperclass());
        } else {
            return page;
        }
    }

}
