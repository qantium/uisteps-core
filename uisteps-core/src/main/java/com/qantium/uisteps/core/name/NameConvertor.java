package com.qantium.uisteps.core.name;

import java.lang.reflect.Field;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;
/**
 *
 * @author ASolyankin
 */
public class NameConvertor {

    public static String humanize(Class<?> klass) {
        
        if(klass.getSimpleName().contains("$$")) {
            return humanize(klass.getSuperclass());
        }
        
        return HtmlElementUtils.getElementName(klass).toLowerCase();
    }

    public static String humanize(Object obj) {
        return humanize(obj.getClass());
    }
    
    public static String humanize(Field field) {
        return HtmlElementUtils.getElementName(field).toLowerCase();
    }
}
