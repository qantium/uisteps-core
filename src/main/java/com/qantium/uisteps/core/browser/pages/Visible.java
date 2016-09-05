package com.qantium.uisteps.core.browser.pages;

/**
 * Created by Anton Solyankin
 */
public interface Visible {

    boolean waitUntilIsDisplayed();

    boolean waitUntilIsNotDisplayed();

    boolean isCurrentlyDisplayed();

    boolean isNotCurrentlyDisplayed();
}
