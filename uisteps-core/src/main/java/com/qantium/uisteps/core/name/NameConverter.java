package com.qantium.uisteps.core.name;

import org.apache.commons.lang.WordUtils;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;

import java.lang.reflect.Field;
/**
 *
 * @author ASolyankin
 */
public class NameConverter {

    public static String humanize(Class<?> klass) {
        String name = HtmlElementUtils.getElementName(klass);
        
        if(name.contains("$")) {
            name = name.split("\\$")[0].trim();
        }
        return name.toLowerCase();
    }
    
    public static String humanize(Field field) {
        return HtmlElementUtils.getElementName(field).toLowerCase();
    }
}
