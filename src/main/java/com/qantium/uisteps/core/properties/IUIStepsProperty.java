package com.qantium.uisteps.core.properties;

import java.io.*;
import java.util.Properties;

import static java.nio.file.Files.newInputStream;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Anton Solyankin
 */
public interface IUIStepsProperty {

    String PROPS_FILE_PATH = System.getProperty("setup.properties.file", "setup.properties");
    Properties PROPERTIES = getPropertiesFromFile();

    static File[] getPropertiesFiles() {
        String[] fileNames = PROPS_FILE_PATH.split(";");
        File[] files = new File[fileNames.length];

        for(int i = 0; i < files.length; i++) {
            files[i] = new File(fileNames[i].trim());
        }

        return files;
    }

    static Properties getPropertiesFromFile() {
        File[] files = getPropertiesFiles();
        return getPropertiesFrom(files);
    }

    default String getFrom(File file) {
        return getPropertiesFrom(file).getProperty(getKey());
    }

    default String getFromSystem() {
        return System.getProperty(getKey());
    }

    default String getFromFile() {
        return getPropertiesFromFile().getProperty(getKey());
    }

    static Properties getPropertiesFrom(File... files) {
        Properties properties = new Properties();

        try {
            for (File file: files) {
                if(file.exists()) {
                    InputStream inputStream = newInputStream(file.toPath());
                    Properties loadedProperties = new Properties();
                    loadedProperties.load(inputStream);
                    for (Object key : loadedProperties.keySet()) {
                        String value = loadedProperties.getProperty(key.toString());
                        properties.setProperty(key.toString(), value);
                    }
                }
            }
        } finally {
            return properties;
        }
    }

    default void setValue(String value) {
        System.setProperty(getKey(), value);
        getPropertiesFromFile();
    }

    default void writeToFile() throws IOException {
        File[] files = getPropertiesFiles();
        writeTo(files);
    }

    default void writeTo(File... files) throws IOException {
        for(File file: files) {
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = getPropertiesFrom(file);
            properties.setProperty(getKey(), getValue());
            OutputStream out = new FileOutputStream(file);
            properties.store(out, "Property " + getKey() + " was changed");
        }
    }

    default Integer getIntValue() {
        return Integer.parseInt(getValue());
    }

    default String getValue() {
        String value = getFromSystem();

        if (isNotEmpty(value)) {
            value = PropertyFinder.findAll(value);
            return value;
        }

        value = PROPERTIES.getProperty(getKey());

        if (isNotEmpty(value)) {
            value = PropertyFinder.findAll(value);
            return value;
        }

        value = getDefaultValue();
        value = PropertyFinder.findAll(value);

        return value;
    }

    default String getKey() {
        return toString().toLowerCase().replace("_", ".");
    }

    default boolean isTrue() {
        return Boolean.valueOf(getValue());
    }

    String getDefaultValue();
}
