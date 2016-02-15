package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class ByZkId extends By {

    private WebDriver driver;
    private final String id;

    public ByZkId(String id) {
        this.id = id;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        if (context instanceof FindsById) {
            return ((FindsById) context).findElementsById(getHashWithId());
        } else {
            return ((FindsByXPath) context).findElementsByXPath(getXpath());
        }
    }

    @Override
    public WebElement findElement(SearchContext context) {
        if (context instanceof FindsById) {
            return ((FindsById) context).findElementById(getHashWithId());
        } else {
            return ((FindsByXPath) context).findElementByXPath(getXpath());
        }
    }

    private String getXpath() {
        return ".//*[@id = '" + getHashWithId() + "']";
    }

    private String getHashWithId() {
        return getHash() + id.replace(ZK.ID_MARK, "");
    }

    private String getHash() {
        return ZK.getHash(driver);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "By.ZK.id: " + id;
    }
}
