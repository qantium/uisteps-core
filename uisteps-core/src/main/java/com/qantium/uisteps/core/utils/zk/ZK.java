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


    public static final String ZK_ID_MARK = UIStepsProperties.getProperty(UIStepsProperty.ZK_ID_MARK);
    private final WebDriver driver;

    public ZK(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isZkId(By.ById id) {
        return getIdFrom(id).contains(ZK_ID_MARK);
    }

    public boolean isZkShiftId(By.ById id) {
        return getZkShiftMatcher(id).find();
    }

    private String getIdFrom(By.ById id) {
        return id.toString().replace("By.id: ", "");
    }

    public By getLocator(By.ById id) {
        return By.id(getHash() + getIdFrom(id).replace(ZK_ID_MARK, ""));
    }

    public By getLocator(By.ById id, UIObject context) {

        if (context != null && context instanceof UIElement) {
            return getLocator(ZK.sum(getContextZkNumber((UIElement) context), getZkShift(id)));
        } else {
            return getLocator(getZkShift(id));
        }
    }

    private By getLocator(ZKNumber zkNumber) {
        return By.id(getHash() + zkNumber);
    }

    private ZKNumber getZkShift(By.ById id) {
        Matcher zkShiftMatcher = getZkShiftMatcher(id);
        if (zkShiftMatcher.find()) {
            return ZK.number(Integer.parseInt(zkShiftMatcher.group(1)));
        } else {
            throw new ZKException("Cannot find shift for " + id);
        }
    }

    private Matcher getZkShiftMatcher(By.ById id) {
        Pattern pattern = Pattern.compile("ZK_ID_MARK + \\[(.*?)\\]");
        return pattern.matcher(getIdFrom(id));
    }

    private ZKNumber getContextZkNumber(UIElement context) {
        Pattern pattern = Pattern.compile(getHash() + "(.*?)($|\\W.*?)");
        Matcher matcher = pattern.matcher((context.getAttribute("id")));
        if (matcher.matches()) {
            return ZK.number(matcher.group(1));
        } else {
            throw new ZKException("Cannot find zk number for context " + context);
        }
    }

    public static String id(String id) {
        return ZK_ID_MARK + id;
    }

    public static ZKNumber number(String value) {
        return new ZKNumber(value);
    }

    public static ZKNumber number(int value) {
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
}
