package com.qantium.uisteps.core.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    private String data;
    private boolean isJsonObject;
    private boolean isJsonArray;

    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private DataParser(Object data) {
        if (data == null) {
            this.data = null;
        } else {
            String formattedData = formatJson(data.toString());

            isJsonObject = isJsonObject(formattedData);
            isJsonArray = isJsonArray(formattedData);
            if (isJson()) {
                this.data = formattedData;
            } else {
                this.data = data.toString();
            }
        }
    }


    public static DataParser parse(Object data) {
        return new DataParser(data);
    }

    public String getData() {
        return data;
    }

    public boolean isJsonObject() {
        return isJsonObject;
    }

    public boolean isJsonArray() {
        return isJsonArray;
    }

    public boolean isJson() {
        return isJsonObject || isJsonArray;
    }

    public JSONObject toJsonObject() {
        if (jsonObject == null) {
            throw new IllegalArgumentException("Data is not json object. Data: " + data);
        } else {
            return jsonObject;
        }
    }

    public JSONArray toJsonArray() {
        if (jsonArray == null) {
            throw new IllegalArgumentException("Data is not json array. Data: " + data);
        } else {
            return jsonArray;
        }
    }

    private boolean isJsonObject(String data) {
        try {
            if (jsonObject == null) {
                jsonObject = new Json(data);
            }
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }

    private boolean isJsonArray(String data) {
        try {
            if (jsonArray == null) {
                jsonArray = new JSONArray(data);
            }
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }

    private String formatJson(String json) {
        json = json
                .replace("=,", "=null,")
                .replace("[,", "[null,")
                .replace(",]", ",null]")
                .replace("=}", "=null}")
                .replace(",,", ",null,")
                .replace(", ,", ",null,")
                .replace("=", ":");

        Pattern p = Pattern.compile(":(\\d+)");
        Matcher m = p.matcher(json);
        while (m.find()) {
            json = json.replaceAll(m.group(), ":\"" + m.group(1) + "\"");
        }
        return json;
    }


    @Override
    public String toString() {
        return data;
    }

    private class Json extends JSONObject {

        private Json(String source) throws JSONException {
            super(source);
        }

        private Map newMap;

        public JSONObject put(String key, Object value) throws JSONException {
            if (newMap == null) {
                newMap = new LinkedHashMap();
                try {
                    Field field = getClass().getSuperclass().getDeclaredField("map");
                    field.setAccessible(true);
                    field.set(this, newMap);
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return super.put(key, value);
        }
    }
}
