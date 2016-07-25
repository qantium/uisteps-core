package com.qantium.uisteps.core.browser.pages;

/**
 * Created by Anton Solyankin
 */
public interface Visible {

    boolean isDisplayed();

    boolean isNotDisplayed();

    boolean isDisplayed(long timeout);

    boolean isNotDisplayed(long timeout);

    boolean isDisplayed(long timeout, long pollingTime);

    boolean isNotDisplayed(long timeout, long pollingTime);
}
