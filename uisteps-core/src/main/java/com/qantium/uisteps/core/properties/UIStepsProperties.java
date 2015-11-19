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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import org.codehaus.plexus.util.StringUtils;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author A.Solyankin
 */
public class UIStepsProperties {

    private static Properties properties;
    private static final String WORKIND_DIR = System.getProperty("user.dir");
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String PROPERTIES_FILE_NAME = "uisteps.properties";
    private static final String AS = "AS#";

    private static Properties getProperties() {

        if (properties == null) {
            properties = new Properties(getDefaults());
            load(PROPERTIES_FILE_NAME, properties);
            String path = getProperty(PROPERTIES_PATH);

            if (!StringUtils.isEmpty(path)) {
                load(path, properties);
            }

            for (String propertyName : properties.stringPropertyNames()) {
                String property = properties.getProperty(propertyName);

                if (property.startsWith(AS)) {
                    properties.setProperty(propertyName, properties.getProperty(property.replace(AS, "")));
                }

            }
        }
        return properties;
    }

    private static void load(String propertiesFileName, Properties properties) {
        if (!loadForm(new File(propertiesFileName), properties)) {

            if (!loadForm(new File(WORKIND_DIR, propertiesFileName), properties)) {
                loadForm(new File(HOME_DIR, propertiesFileName), properties);
            }
        }
    }

    private static boolean loadForm(File file, Properties properties) {

        try {
            properties.load(Files.newInputStream(file.toPath()));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static String getProperty(String key) {
        key = key.toLowerCase();
        String property = getProperties().getProperty(key);
        if (property != null) {
            return property;
        } else {
            return System.getProperty(key);
        }
    }

    public static String getProperty(UIStepsProperty property) {
        return getProperty(property.toString());
    }

    private static Properties getDefaults() {
        Properties defaults = new Properties();

        for (UIStepsProperty property : UIStepsProperty.values()) {
            defaults.setProperty(property.toString(), property.getDefault());
        }
        return defaults;
    }
}
