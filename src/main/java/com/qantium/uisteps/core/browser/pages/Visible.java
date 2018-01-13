package com.qantium.uisteps.core.browser.pages;

/**
 * Created by Anton Solyankin
 */
public interface Visible {

    boolean isDisplayed();

    default boolean isNotDisplayed() {
        return !isDisplayed();
    }
}
