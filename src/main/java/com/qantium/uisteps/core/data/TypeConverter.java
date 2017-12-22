package com.qantium.uisteps.core.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TypeConverter {


    public static Object[] convert(Class<?>[] types, JSONArray jsonArray) throws JSONException {
        List castedValues = new ArrayList();

        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            Object value = jsonArray.get(i);
            castedValues.add(convert(type, value));
        }
        return castedValues.toArray();
    }

    public static Object convert(Class<?> type, Object value) throws JSONException {

        if (value == null) {
            return value;
        } else {
            if (type.isEnum()) {
                return Enum.valueOf((Class<Enum>) type, value.toString());
            } else if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.valueOf(value.toString());
            } else if (type.equals(float.class) || type.equals(Float.class)) {
                return Float.valueOf(value.toString());
            } else if (type.equals(double.class) || type.equals(Double.class)) {
                return Double.valueOf(value.toString());
            } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                return Boolean.valueOf(value.toString());
            } else if (type.equals(String.class)) {
                return value.toString();
            } else {
                throw new IllegalArgumentException("Cannot cast value \"" + value + "\" to type \"" + type + "\"");
            }
        }
    }
}
