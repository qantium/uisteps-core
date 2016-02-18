package com.qantium.uisteps.core.browser.pages.elements.form;

import com.qantium.uisteps.core.name.Named;

/**
 * Created by SolAN on 12.02.2016.
 */
public interface Fillable extends Named  {

    Object setValue(Object value);

    Object getValue();
}
