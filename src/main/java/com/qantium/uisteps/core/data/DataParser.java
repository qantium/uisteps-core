package com.qantium.uisteps.core.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    private final String data;
    private boolean isJsonObject;
    private boolean isJsonArray;

    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private DataParser(Object data) {

        if(data == null) {
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
                jsonObject = new JSONObject(data);
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
}
