package com.qantium.uisteps.core.name;

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
        
        return HtmlElementUtils.getElementName(klass);
    }

    public static String humanize(Object obj) {
        return humanize(obj.getClass());
    }
}
