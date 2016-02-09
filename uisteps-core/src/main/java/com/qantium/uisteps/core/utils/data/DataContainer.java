package com.qantium.uisteps.core.utils.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Anton Solyankin
 */
public class DataContainer {

    private Data defaultData;
    private Data data;


    public Data getDefault() {
        return defaultData;
    }

    public Data get() {
        return data;
    }

    public DataContainer setDefault(Data defaultData) {
        this.defaultData = defaultData;
        return this;
    }

    public DataContainer set(Data data) {
        this.data = data;

        if (defaultData == null) {
            setDefault(data);
        }
        return this;
    }

    public String getString(String key) {
        return data.toJSON().optString(key, defaultData.getString(key));
    }

    public long getLong(String key) {
        return data.toJSON().optLong(key, defaultData.getLong(key));
    }

    public JSONObject getJSONObject(String key) {
        JSONObject json = data.getJSONObject(key);

        if (json != null) {
            json = defaultData.getJSONObject(key);
        }

        return json;
    }

    public JSONArray getJSONArray(String key) {
        try {
            return data.toJSON().getJSONArray(key);
        } catch (JSONException ex) {
            return defaultData.getJSONArray(key);
        }
    }

    public int getInt(String key) {
        return data.toJSON().optInt(key, defaultData.getInt(key));
    }

    public double getDouble(String key) {
        return data.toJSON().optDouble(key, defaultData.getDouble(key));
    }

    public boolean getBoolean(String key) {
        return data.toJSON().optBoolean(key, defaultData.getBoolean(key));
    }


    public JSONObject toJSON() {
        Data toData = copy(defaultData, new Data());
        return copy(data, toData).toJSON();
    }

    private Data copy(Data data, Data toData) {

        if (data != null) {
            Iterator iterator = data.keys();

            while (iterator.hasNext()) {
                Object key = iterator.next();
                toData.set(key, data.get(key));
            }
        }

        return toData;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public Iterator sortedKeys() {
        return toJSON().sortedKeys();
    }

    public Data set(Object key, Object value) {
        return data.set(key, value);
    }

    public int length() {
        return toJSON().length();
    }

    public Iterator keys() {
        return toJSON().keys();
    }

    public boolean isNull(String key) {
        return toJSON().isNull(key);
    }

    public boolean has(String key) {
        return toJSON().has(key);
    }

    public Object get(Object key) {
        return data.get(key);
    }
}
