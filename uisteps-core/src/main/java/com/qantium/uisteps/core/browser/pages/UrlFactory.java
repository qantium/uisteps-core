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
import static com.qantium.uisteps.core.properties.UIStepsProperty.WEBDRIVER_BASE_URL_PROTOCOL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ASolyankin
 */
public class UrlFactory {

    private final String HOST;
    private final String PARAM_REGEXP;
    private final String BASE_HOST;
    private final String BASE_PROTOCOL;

    public UrlFactory() {
        HOST = UIStepsProperties.getProperty(UIStepsProperty.BASE_URL_HOST);
        PARAM_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.PROPERTY_REGEXP);
        BASE_HOST = UIStepsProperties.getProperty(UIStepsProperty.WEBDRIVER_BASE_URL_HOST);
        BASE_PROTOCOL = UIStepsProperties.getProperty(WEBDRIVER_BASE_URL_PROTOCOL);
    }

    public Url getUrlOf(Page page, String... params) {

        if (page.getUrl() == null) {
            page.setUrl(getUrlOf(page.getClass(), params));
        }
        return page.getUrl();
    }

    public Url getUrlOf(Class<? extends Page> page, String... params) {
        Url url = new Url();
        
        if (!ArrayUtils.isEmpty(params)) {
            processParams(url, params);
        }
        process(url, page);

        if (!ArrayUtils.isEmpty(params)) {
            processParams(url, UIStepsProperties.getProperties());
        } 
        return url;
    }

    public boolean isRoot(Class<?> page) {
        return page.isAnnotationPresent(Root.class);
    }

    protected void process(Url url, Class<?> page) {

        if (!isRoot(page)) {
            process(url, page.getSuperclass());
        }
        
        if (page.isAnnotationPresent(BaseUrl.class)) {
            BaseUrl baseUrlAnnotation = page.getAnnotation(BaseUrl.class);

            String protocol = baseUrlAnnotation.protocol();
            String host = baseUrlAnnotation.value();
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
                url.setPassword(user);
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

            if (!ArrayUtils.isEmpty(urlParams)) {
                processParams(url, urlParams);
            }
        }

        if (StringUtils.isEmpty(url.getHost())) {
            url.setHost(BASE_HOST);
        }

        if (StringUtils.isEmpty(url.getProtocol())) {
            url.setProtocol(BASE_PROTOCOL);
        }
    }

    protected void processParams(Url url, String... params) {
        List<String> urlParams = new ArrayList();
        Map<String, String> paramsMap = new HashMap();

        for (String param : params) {
            Pattern pattern = Pattern.compile("(.+?)=(.*)");
            Matcher matcher = pattern.matcher(param);

            if (matcher.matches()) {
                paramsMap.put(matcher.group(1), matcher.group(2));
            } else {
                urlParams.add(param);
            }
        }
        processParams(url, paramsMap, urlParams.toArray(new String[urlParams.size()]));
    }

    protected void processParams(Url url, Map propeties, String... params) {
        int paramIndex = 0;

        Pattern pattern = Pattern.compile(PARAM_REGEXP);
        Matcher matcher = pattern.matcher(url.toString());

        while (matcher.find()) {

            String key = matcher.group(1);
            String name = matcher.group(2);
            String value = propeties.get(name).toString();

            if (value == null) {
                
                if(paramIndex < params.length) {
                    value = params[paramIndex];
                    paramIndex++;
                } else {
                    continue;
                }
            }

            String prefix = url.getPrefix();

            if (prefix.contains(key)) {
                url.setPrefix(prefix.replace(key, value));
                continue;
            }

            String postfix = url.getPrefix();

            if (postfix.contains(key)) {
                url.setPostfix(prefix.replace(key, value));
            }

        }
    }

}
