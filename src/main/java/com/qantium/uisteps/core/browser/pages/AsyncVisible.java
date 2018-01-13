package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.wait.WithTimeout;

import static com.qantium.uisteps.core.browser.wait.Waiting.waitUntil;

public interface AsyncVisible extends Visible, WithTimeout {

    default boolean waitUntilIsDisplayed() {
        return waitUntil(this, obj -> isDisplayed());
    }

    default boolean waitUntilIsNotDisplayed() {
        return waitUntil(this, obj -> isNotDisplayed());
    }
}
