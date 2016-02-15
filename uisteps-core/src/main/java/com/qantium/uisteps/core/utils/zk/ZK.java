package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Solyankin
 */
public class ZK {

    public static final String ID_MARK = UIStepsProperties.getProperty(UIStepsProperty.ZK_ID_MARK);

    public static boolean isZkId(String id) {
        return id.contains(ID_MARK);
    }

    public static ByZkId byId(String id) {
        return new ByZkId(id);
    }

    public static  ZKNumber number(String value) {
        return new ZKNumber(value);
    }

    public static  ZKNumber number(int value) {
        return new ZKNumber(value);
    }

    public static ZKNumber sum(ZKNumber a, ZKNumber b) {
        return number(a.toInt() + b.toInt());
    }

    public static ZKNumber diff(ZKNumber a, ZKNumber b) {
        return number(a.toInt() - b.toInt());
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("(\\[(.*?)\\])");
        Matcher matcher = pattern.matcher("[222][1111]");

        if(matcher.find()) {
            System.out.println("========" + matcher.group(1));
            System.out.println("========" + matcher.group(2));
        }

    }

}
