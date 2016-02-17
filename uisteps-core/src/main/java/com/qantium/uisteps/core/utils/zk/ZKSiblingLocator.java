package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class ZKSiblingLocator extends By {

    private final UIElement context;
    private final int shift;


    public ZKSiblingLocator(UIElement context, int shift) {
        this.context = context;
        this.shift = shift;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        throw new ZKException("Method findElements is not realized for ZKSiblingLocator class");
    }

    public UIElement getContext() {
        return context;
    }

    public int getShift() {
        return shift;
    }
}
