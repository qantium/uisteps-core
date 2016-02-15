package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    public static String getHash(WebDriver driver) {
        String ZK_HASH_XPATH = UIStepsProperties.getProperty(UIStepsProperty.ZK_HASH_XPATH);
        String ZK_HASH_ATTRIBUTE = UIStepsProperties.getProperty(UIStepsProperty.ZK_HASH_ATTRIBUTE);
        String ZK_HASH_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.ZK_HASH_REGEXP);

        String id = driver.findElement(By.xpath(ZK_HASH_XPATH)).getAttribute(ZK_HASH_ATTRIBUTE);

        Pattern pattern = Pattern.compile(ZK_HASH_REGEXP);
        Matcher matcher = pattern.matcher(id);

        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new ZKException("Cannot find zk hash by xpath " + ZK_HASH_XPATH
                    + ", attribute " + ZK_HASH_ATTRIBUTE
                    + " and regexp " + ZK_HASH_REGEXP);
        }
    }

}
