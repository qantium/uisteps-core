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

/**
 * Contains settings that can be set before test running
 * <p>
 * Property can be overrided in "uisteps.properties" file or in another file
 * whitch path is set in "properties.path" property. Both files must be in 
 * working or/and home directory. Properties in working directory 
 * override properties in home directory. Working directory is 
 * taken from System.getProperty("user.dir"). Home directory is 
 * taken from System.getProperty("user.home")
 * <p>
 * Property can be overrided by another using "AS#" construction, 
 * e.g. webdriver.driver = AS#driver
 * <p>
 * List of properties:
 * <ul>
 * <li>properties.path</li>
 * <li>webdriver.driver</li>
 * <li>webdriver.remote.url</li>
 * <li>webdriver.base.url</li>
 * <li>webdriver.proxy</li>
 * <li>webdriver.timeouts.implicitlywait</li>
 * <li>home.dir</li>
 * <li>screenshots.scale.width</li>
 * <li>screenshots.scale.height</li>
 * <li>base.url.host</li>
 * <li>null.value</li>
 * <li>property.regexp</li>
 * <li>browser.width</li>
 * <li>browser.height</li>
 * </ul>
 * 
 * @see com.qantium.uisteps.core.properties.UIStepsProperty
 * 
 * @author A.Solyankin
 */
public class UIStepsProperties {

    private static Properties properties;
    private static final String WORKIND_DIR = System.getProperty("user.dir");
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String PROPERTIES_FILE_NAME = "uisteps.properties";
    private static final String AS = "AS#";

    /**
     * Load default properties
     * 
     * @return Properties
     */
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
        
        if (!load(new File(propertiesFileName), properties)) {
            
            File fileFromHomeDir = new File(HOME_DIR, propertiesFileName);
            
            if(fileFromHomeDir.exists()) {
                load(fileFromHomeDir, properties);
            }
            
            File fileFromWorkingDir = new File(WORKIND_DIR, propertiesFileName);
            
            if(fileFromWorkingDir.exists()) {
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
     * @param key
     * @return the string value if there is no property with that key.
     */
    public static String getProperty(String key) {
        key = key.toLowerCase();
        String property = getProperties().getProperty(key);
        
        if (property != null) {
            return property;
        } else {
            return System.getProperty(key);
        }
    }

    /**
     * Get property
     * 
     * @param property
     * @return Properties
     */
    public static String getProperty(UIStepsProperty property) {
        return getProperty(property.toString());
    }

    /**
     * Get default properties
     * 
     * @return Properties
     */
    private static Properties getDefaults() {
        Properties defaults = new Properties();

        for (UIStepsProperty property : UIStepsProperty.values()) {
            defaults.setProperty(property.toString(), property.getDefault());
        }
        return defaults;
    }
}
