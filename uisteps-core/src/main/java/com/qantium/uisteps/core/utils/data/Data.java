package com.qantium.uisteps.core.utils.data;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Anton Solyankin
 */
public class Data {

    private final JSONObject data;

    public Data() {
        this.data = new JSONObject();
    }

    public Data(String data) {
        try {
            this.data = new JSONObject(data);
        } catch (JSONException ex) {
            throw new RuntimeException("Cannot set data!\nCause:" + ex);
        }
    }

    public Data(File file) {
        this.data = getFrom(file);
    }


    protected JSONObject getFrom(File file) {

        if(!file.isAbsolute()) {
            file = new File(System.getProperty("user.dir") + file.getPath());
        }

        StringBuilder sb = new StringBuilder();

        try {
            for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read data  from file " + file + "\nCause:" + ex);
        } catch (JSONException ex) {
            throw new RuntimeException("Cannot create json from: " + sb + "\nCause:" + ex);
        }

    }

    public JSONObject toJSON() {
        return data;
    }

    public Object get(Object key) {
        try {
            return data.get(key.toString());
        } catch (JSONException ex) {
            throw new RuntimeException("Cannot get " + key + " from: " + data + "\nCause:" + ex);
        }
    }

    public String getString(String key) {
        return data.optString(key);
    }

    public boolean getBoolean(String key) {
        return data.optBoolean(key);
    }

    public double getDouble(String key) {
        return data.optDouble(key);
    }

    public int getInt(String key) {
        return data.optInt(key);
    }

    public JSONArray getJSONArray(String key) {
        return data.optJSONArray(key);
    }

    public JSONObject getJSONObject(String key) {
        return data.optJSONObject(key);
    }

    public long getLong(String key) {
        return data.optLong(key);
    }

    public boolean has(String key) {
        return data.has(key);
    }

    public boolean isNull(String key) {
        return data.isNull(key);
    }

    public Iterator keys() {
        return data.keys();
    }

    public int length() {
        return data.length();
    }

    public Data set(Object key, Object value) {
        try {
            data.putOpt(key.toString(), value);
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public Iterator sortedKeys() {
        return data.sortedKeys();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
