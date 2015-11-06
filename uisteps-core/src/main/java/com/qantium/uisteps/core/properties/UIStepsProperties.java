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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author A.Solyankin
 */
public class UIStepsProperties {

    private static final Properties properties = new Properties(getDefaults());
    private static final String workingDirectory = System.getProperty("user.dir");
    private static final String homeDirectory = System.getProperty("user.home");
    private static final String propertiesFileName = "uisteps.properties";
    private static final String AS = "AS#";
    private static final Logger logger = LoggerFactory.getLogger(UIStepsProperties.class);

    static {

        logger.info("UISTEPS PROPERTIES ========================");

        load(propertiesFileName);

        String path = getProperty(UISTEPS_PROPERTIES_PATH);

        if (!StringUtils.isEmpty(path)) {
            load(path);
        }

        for (String propertyName : properties.stringPropertyNames()) {
            String property = properties.getProperty(propertyName);

            if (property.startsWith(AS)) {
                properties.setProperty(propertyName, properties.getProperty(property.replace(AS, "")));
                property = properties.getProperty(propertyName);
            }

            logger.info(propertyName + " = " + property);
        }
        logger.info("===========================================");

    }

    private static void load(String propertiesFileName) {

        if (!loadForm(new File(propertiesFileName))) {

            if (!loadForm(new File(workingDirectory, propertiesFileName))) {
                loadForm(new File(homeDirectory, propertiesFileName));
            }

        }
    }

    private static boolean loadForm(File file) {

        try {
            properties.load(Files.newInputStream(file.toPath()));
            logger.info("Loaded from: " + file.getAbsolutePath());
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
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
