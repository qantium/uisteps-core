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

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASolyankin
 */
public abstract class UrlFactory {

    protected final String HOST;
    protected final String PARAM;
    protected final Class<? extends Annotation> urlAnnotation;

    public UrlFactory(String HOST, String PARAM, Class<? extends Annotation> urlAnnotation) {
        this.HOST = HOST;
        this.PARAM = PARAM;
        this.urlAnnotation = urlAnnotation;
    }

    public UrlFactory(Class<? extends Annotation> urlAnnotation) {
        this("#HOST", "#PARAM", urlAnnotation);
    }

    public Url getUrlOf(Class<? extends Page> page, Url url, String... params) {

        if (url.getHost().isEmpty()) {
            url.setHost(getBaseUrl());
        }
        
        Class<?> pageClass = getPageClass(page);
        
        getUrlOf(url, pageClass);

        String urlString = url.toString();
        int paramIndex = 0;

        while (urlString.contains(PARAM)) {
            
            try {
                urlString = urlString.replaceFirst(PARAM, params[paramIndex]);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException("Url " + urlString + "  in " + pageClass + " needs more params! Params: " + Arrays.toString(params) + " \nCause:" + ex);
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

    public Url getUrlOf(Class<? extends Page> page, String... params) {
        return getUrlOf(page, new Url(), params);
    }

    protected void getUrlOf(Url url, Class<?> page) {

        if (isRoot(page)) {
            String rootUrl = page.getAnnotation(Root.class).value();
            if (!rootUrl.isEmpty()) {
                url.setHost(rootUrl);
            }
        }

        if (!isRoot(page) && page != Object.class) {
            getUrlOf(url, page.getSuperclass());
        }

        if (page.isAnnotationPresent(urlAnnotation)) {
            String defaultUrl = getPageUrlFrom(page.getAnnotation(urlAnnotation));

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
    }

    protected boolean isRoot(Class<?> page) {
        return page.isAnnotationPresent(Root.class);
    }

    protected Class<?> getPageClass(Class<?> page) {
        
        if (page.getName().contains("$$")) {
            return getPageClass(page.getSuperclass());
        } else {
            return page;
        }
    }

    protected abstract String getBaseUrl();

    protected abstract String getPageUrlFrom(Annotation urlAnnotation);
}
