package com.qantium.uisteps.core.name;

import org.apache.commons.lang.WordUtils;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.lang.reflect.Field;

/**
 * @author ASolyankin
 */
public class NameConverter {

    public static String humanize(Class<?> klass) {
        String name;
        if (klass.isAnnotationPresent(Name.class)) {
            name = splitCamelCase(klass.getAnnotation(Name.class).value());
        } else {
            name = splitCamelCase(klass.getSimpleName());
        }

        if (name.contains("$")) {
            name = name.split("\\$")[0].trim();
        }
        return name.toLowerCase();
    }

    public static String humanize(Field field) {
        if (field.isAnnotationPresent(Name.class)) {
            return splitCamelCase(field.getAnnotation(Name.class).value());
        } else {
            return splitCamelCase(field.getName()).toLowerCase();
        }
    }

    private static String splitCamelCase(String camel) {
        return WordUtils.capitalizeFully(camel.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        ));
    }

}
