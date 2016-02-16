package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
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
    private final WebDriver driver;

    public ZK(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isZkId(By.ById id) {
        return getIdFrom(id).contains(ID_MARK);
    }

    public boolean isZkShiftId(By.ById id) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(getIdFrom(id));
        return matcher.find();
    }

    private String getIdFrom(By.ById id) {
        return id.toString().replace("By.id: ", "");
    }

    public By getLocator(By.ById id) {
        return By.id(getHash() + getIdFrom(id));
    }

    public By getLocator(By.ById id, UIObject context) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(getIdFrom(id));

        if (matcher.find()) {
            int shift = Integer.parseInt(matcher.group(1));

            if(context != null && context instanceof UIElement) {
                Pattern pattern2 = Pattern.compile(getHash() + "(.*?)($|\\W.*?)");
                Matcher matcher2 = pattern2.matcher(((UIElement) context).getAttribute("id"));
                matcher2.matches();
                return By.id(getHash() + ZK.sum(ZK.number(matcher2.group(1)), ZK.number(shift)));
            } else {
                return By.id(getHash() + ZK.number(getIdFrom(id)));
            }
        } else {
            throw new ZKException("Cannot find shift fo " + id);
        }
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

    public String getHash() {
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

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("AAAA(.*?)($|\\W.*?)");
        Matcher matcher = pattern.matcher("AAAAe1q-real");

        if (matcher.find()) {
            System.out.println("================33=" + matcher.group(1));
        }
    }

}
