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

    private final String hash;
    private final String id;

    public ByZkId(String id,  String hash) {
        this.id = id;
        this.hash = hash;
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

    public String getHashWithId() {
        return getHash() + id.replace(ZK.ID_MARK, "");
    }

    private String getHash() {
        return hash;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "By.ZK.id: " + id;
    }
}
