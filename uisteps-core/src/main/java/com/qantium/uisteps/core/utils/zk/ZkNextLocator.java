package com.qantium.uisteps.core.utils.zk;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by SolAN on 16.02.2016.
 */
public class ZkNextLocator extends By {

    private final UIElement context;
    private final int shift;


    public ZkNextLocator(UIElement context, int shift) {
        this.context = context;
        this.shift = shift;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        throw new ZKException("Method findElements is not realized for ByZkNext class");
    }

    public UIElement getContext() {
        return context;
    }

    public int getShift() {
        return shift;
    }
}
