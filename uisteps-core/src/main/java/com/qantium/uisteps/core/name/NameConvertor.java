package com.qantium.uisteps.core.name;

import java.lang.reflect.Field;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;
/**
 *
 * @author ASolyankin
 */
public class NameConvertor {

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
