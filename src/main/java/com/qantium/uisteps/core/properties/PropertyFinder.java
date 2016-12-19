package com.qantium.uisteps.core.properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Solan on 09.12.2016.
 */
public class PropertyFinder {

    public static final String PATTERN = "%(.+?)%";

    public static String findAll(String value) {

        Pattern pattern = Pattern.compile(PATTERN);

        do {
            value = find(value);
        } while (pattern.matcher(value).find());
        return value;
    }

    public static String find(String value) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {

            String aliasKey = matcher.group(1);
            String aliasValue = System.getProperty(aliasKey);

            value = value.replace(matcher.group(0), aliasValue);
        }

        return value;
    }

}
