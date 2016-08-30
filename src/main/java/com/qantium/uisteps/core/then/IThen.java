package com.qantium.uisteps.core.then;

import com.qantium.uisteps.core.browser.pages.UIObject;

/**
 * Created by Solan on 26.07.2016.
 */
public interface IThen {

    <T extends UIObject> Then<T> then(Class<T> uiObject);
}
