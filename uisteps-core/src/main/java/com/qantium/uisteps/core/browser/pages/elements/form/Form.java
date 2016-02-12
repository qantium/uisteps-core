package com.qantium.uisteps.core.browser.pages.elements.form;

import com.qantium.uisteps.core.browser.pages.UIElement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class Form extends UIElement {

    private final Map<Object, Fillable> fields = new HashMap();

    protected Form add(Object key, Fillable field) {
        fields.put(key, field);
        return this;
    }

    public <T extends Fillable> T get(Object key) {
        return (T) fields.get(key);
    }

    public Set<Object> getFieldKeys() {
        return fields.keySet();
    }

    public Collection<Fillable> getFields() {
        return fields.values();
    }

    public Object fill(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("Cannot find field by null key on the form " + this);
        }
        fields.get(key).setValue(value);
        return this;
    }

    public Object fill(Map<Object, Object> values) {

        for (Object key : values.keySet()) {
            fill(key, values.get(key));
        }
        return this;
    }

}
