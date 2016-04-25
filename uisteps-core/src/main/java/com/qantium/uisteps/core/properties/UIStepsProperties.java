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

import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qantium.uisteps.core.properties.UIStepsProperty.PROPERTIES_PATH;

/**
 * Contains settings that can be set before test running
 * <p/>
 * Property can be overrided in "uisteps.properties" file or in another file
 * which path is set in "properties.path" property. Both files must be in
 * working or/and home directory. Properties in working directory override
 * properties in home directory. Working directory is taken from
 * System.getProperty("user.dir"). Home directory is taken from
 * System.getProperty("user.home") At last all properties can be oveerided in
 * "uisteps.local.properties". The rules for it are same with
 * "uisteps.properties" file
 * <p/>
 * Property can be overrided by another using "AS#" construction, e.g.
 * webdriver.driver = AS#driver
 *
 * @author A.Solyankin
 * @see com.qantium.uisteps.core.properties.UIStepsProperty
 */
public class UIStepsProperties {

    private static Properties properties;
    private static final String WORKING_DIR = System.getProperty("user.dir");
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String PROPERTIES_FILE_NAME = "uisteps.properties";
    private static final String PROPERTIES_LOCAL_FILE_NAME = "uisteps.local.properties";

    /**
     * Load default properties
     *
     * @return Properties
     */
    public static Properties getProperties() {

        if (properties == null) {
            synchronized (Properties.class) {
                if (properties == null) {
                    properties = new Properties(getDefaults());
                    load(PROPERTIES_FILE_NAME, properties);
                    String path = getProperty(PROPERTIES_PATH);

                    if (!isEmpty(path)) {
                        load(path, properties);
                    }
                    load(PROPERTIES_LOCAL_FILE_NAME, properties);

                    for (String propertyName : properties.stringPropertyNames()) {
                        String property = properties.getProperty(propertyName);

                        Pattern pattern = Pattern.compile("%(.+?)%");
                        Matcher matcher = pattern.matcher(property);
                        String propertyValue = property;
                        while (matcher.find()) {

                            String aliasProperty = matcher.group(1);
                            String fromProperty = properties.getProperty(aliasProperty);

                            if (isEmpty(fromProperty)) {

                                fromProperty = System.getProperty(aliasProperty);
                                if (isEmpty(fromProperty)) {
                                    throw new RuntimeException("Cannot find property " + property + " for setting to " + propertyName + "!");
                                }
                            }
                            propertyValue = propertyValue.replace(matcher.group(0), fromProperty);
                            properties.setProperty(propertyName, propertyValue);
                        }
                    }

                    for (String property : properties.stringPropertyNames()) {
                        String propertyValue = properties.getProperty(property);

                        if(!isEmpty(propertyValue)) {
                            System.setProperty(property, properties.getProperty(property));
                        }
                    }
                }
            }
        }
        return properties;
    }

    private static void load(String propertiesFileName, Properties properties) {

        if (!load(new File(propertiesFileName), properties)) {

            File fileFromHomeDir = new File(HOME_DIR, propertiesFileName);

            if (fileFromHomeDir.exists()) {
                load(fileFromHomeDir, properties);
            }

            File fileFromWorkingDir = new File(WORKING_DIR, propertiesFileName);

            if (fileFromWorkingDir.exists()) {
                load(fileFromWorkingDir, properties);
            }
        }
    }

    private static boolean load(File file, Properties properties) {

        try {
            properties.load(Files.newInputStream(file.toPath()));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Get property by key
     *
     * @param key specifies the key of property
     * @return the string value if there is property with that key and <code>null</code> if the property is not found
     */
    public static String getProperty(String key) {
        return System.getProperty(key);
    }

    /**
     * Get the property
     *
     * @param property specifies property
     * @return the string value of the property, or null if there is no property
     * with that key.
     */
    public static String getProperty(IUIStepsProperty property) {
        return getProperty(property.toString());
    }

    /**
     * Get default properties
     *
     * @return system properties merged with default properties specified in
     * uisteps property files
     */
    private static Properties getDefaults() {
        Properties defaults = new Properties();
        for (UIStepsProperty property : UIStepsProperty.values()) {

            if(isEmpty(property.getDefaultValue())) {
                defaults.setProperty(property.toString(), "");
            } else {
                defaults.setProperty(property.toString(), property.getDefaultValue());
            }
        }
        return defaults;
    }
}
