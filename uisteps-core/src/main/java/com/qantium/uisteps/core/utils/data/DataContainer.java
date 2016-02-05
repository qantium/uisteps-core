package com.qantium.uisteps.core.utils.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Anton Solyankin
 */
public class DataContainer extends Data {

    private Data defaultData;

    public DataContainer() {
        defaultData = new Data();
    }

    public DataContainer(String data) {
        this(data, data);
    }

    public DataContainer(File file) {
        this(file, file);
    }

    public DataContainer(File defaultData, File data) {
        super(data);
        this.defaultData = new Data(defaultData);
    }

    public DataContainer(String defaultData, String data) {
        super(data);
        this.defaultData = new Data(defaultData);
    }

    public Data getDefault() {
        return defaultData;
    }

    @Override
    public String getString(String key) {
        return toJSON().optString(key, defaultData.getString(key));
    }

    @Override
    public long getLong(String key) {
        return toJSON().optLong(key, defaultData.getLong(key));
    }

    @Override
    public JSONObject getJSONObject(String key) {
        try {
            return toJSON().getJSONObject(key);
        } catch (JSONException ex) {
            return defaultData.getJSONObject(key);
        }
    }

    @Override
    public JSONArray getJSONArray(String key) {
        try {
            return toJSON().getJSONArray(key);
        } catch (JSONException ex) {
            return defaultData.getJSONArray(key);
        }
    }

    @Override
    public int getInt(String key) {
        return toJSON().optInt(key, defaultData.getInt(key));
    }

    @Override
    public double getDouble(String key) {
        return toJSON().optDouble(key, defaultData.getDouble(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return toJSON().optBoolean(key, defaultData.getBoolean(key));
    }
}
