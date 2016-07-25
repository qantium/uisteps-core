package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.Page;

/**
 * Created Anton Solyankin
 */
public class PageDisplayWaiting extends  Waiting {

    private final Page page;

    public PageDisplayWaiting(Page page) {
        this.page = page;
    }

    @Override
    protected boolean until() {
        return page.executeScript("return document.readyState").equals("complete");
    }
}
