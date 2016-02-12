package com.qantium.uisteps.core.browser.zk;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Solyankin
 */
public class ByZkId extends By {

    private WebDriver driver;
    private String id;

    public ByZkId(String id) {

        if (ZK.isZkId(id)) {
            id.replace(ZK.ID_MARK, "");
        }
        this.id = id;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        if (context instanceof FindsById) {
            return ((FindsById) context).findElementsById(getId());
        } else {
            return ((FindsByXPath) context).findElementsByXPath(getXpath());
        }
    }

    @Override
    public WebElement findElement(SearchContext context) {
        if (context instanceof FindsById) {
            return ((FindsById) context).findElementById(getId());
        } else {
            return ((FindsByXPath) context).findElementByXPath(getXpath());
        }
    }

    private String getId() {
        return getHash() + id;
    }

    private String getXpath() {
        return ".//*[@id = '" + getHash() + id + "']";
    }

    private String getHash() {
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

    @Override
    public String toString() {
        return "By.ZK.id: " + id;
    }
}
