package com.qantium.uisteps.core.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.qantium.uisteps.core.data.DataParser.parse;
import static com.qantium.uisteps.core.data.TypeConverter.convert;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class DataProcessor {

    public static Object getData(MemberCollector members, Predicate<String> predicate) {
        List<String> keys = members.getMembers().keySet().stream().filter(predicate)
                .collect(Collectors.toList());

        return getData(members.getObject(), keys);
    }

    public static Object getData(Object obj, Object key) {
        try {
            DataParser data = parse(key);

            if (data.isJson()) {
                if (data.isJsonObject()) {
                    JSONObject jsonObject = data.toJsonObject();
                    JSONObject finalData = new JSONObject();


                    for (int i = 0; i < jsonArray.length(); i++) {
                        finalData.put(i, getData(obj, jsonArray.get(i)));
                    }
                } else {
                    JSONArray jsonArray = data.toJsonArray();
                    JSONArray finalData = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        finalData.put(i, getData(obj, jsonArray.get(i)));
                    }
                }
            } else {
                return getData(obj, Arrays.asList(key.toString()));
            }
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static Object getData(Object obj, List<String> keys) {
        if (obj instanceof DataContainer && new MemberCollector(obj).getMembers().isEmpty()) {
            return ((DataContainer) obj).getData();
        } else if (obj instanceof Iterable) {
            if (keys != null && !keys.isEmpty()) {
                throw new IllegalArgumentException("For Iterable \"" + obj + "\" param keys must be empty: " + Arrays.asList(keys));
            }
            return getData((Iterable) obj);
        }
        return getData(new MemberCollector(obj), keys);
    }

    private static Object getData(MemberCollector members, List<String> keys) {
        Object obj = members.getObject();
        JSONObject data = new JSONObject();
        Map<String, AccessibleObject> membersMap = members.getMembers();

        if (keys.isEmpty()) {
            membersMap.forEach((String key, AccessibleObject member) -> processData(data, key, member, obj));
        } else {
            for (String key : keys) {
                processData(data, key, membersMap.get(key), obj);
            }
        }
        return data;
    }

    private static Object processData(JSONObject data, String key, AccessibleObject member, Object obj) {
        try {
            if (member instanceof Field) {
                return processData(data, key, (Field) member, obj);
            } else {
                return processData(data, key, (Method) member, obj);
            }
        } catch (JSONException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static Object processData(JSONObject data, String key, Field field, Object obj) throws InvocationTargetException, IllegalAccessException, JSONException {
        Object fieldObject = field.get(obj);
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldObject == null) {
            value = JSONObject.NULL;
        } else if (DataContainer.class.isAssignableFrom(fieldType)) {
            value = ((DataContainer) fieldObject).getData();
        } else if (fieldObject instanceof Iterable) {
            value = getData((Iterable) fieldObject);
        } else if (isPrimitive(fieldType) || fieldType.equals(String.class)) {
            value = fieldObject;
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            value = ((Enum) fieldObject).name();
        } else {
            value = getData(fieldObject, new ArrayList<>());
        }
        data.put(key, value);
        return value;
    }

    private static Object processData(JSONObject data, String key, Method method, Object obj) throws InvocationTargetException, IllegalAccessException, JSONException {
        Class<?> returnedType = method.getReturnType();
        Object result = method.invoke(obj);
        if (result == null) {
            data.put(key, JSONObject.NULL);
        } else if (isPrimitive(returnedType) || returnedType.equals(String.class)) {
            data.put(key, convert(returnedType, result));
        } else if (Enum.class.isAssignableFrom(returnedType)) {
            data.put(key, ((Enum) result).name());
        } else {
            data.put(key, getData(result, new ArrayList<>()));
        }
        return result;
    }

    public static void setData(Object obj, Object rawData) {
        try {
            DataParser data = parse(rawData);

            if (data.isJson()) {
                setData(obj, data);
            } else if (obj instanceof DataFillable) {
                ((DataFillable) obj).setData(rawData);
            } else if (obj instanceof Iterable) {
                setData((Iterable) obj, rawData);
            } else {
                throw new IllegalArgumentException("\"" + obj + "\" must implement DataFillable or Iterable<? extends DataFillable> interfaces or data must be json");
            }
        } catch (JSONException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static void setData(Object obj, DataParser data) throws IllegalAccessException, JSONException, InvocationTargetException {
        Map<String, AccessibleObject> members = new MemberCollector(obj).getMembers();
        JSONObject json = data.toJsonObject();
        Iterator keys = json.keys();

        while (keys.hasNext()) {
            String key = keys.next().toString();
            Object value = json.get(key);
            if (members.containsKey(key)) {
                setData(obj, members.get(key), value, data);
            } else {
                throw new IllegalArgumentException("\"" + obj + "\" dose not contain member \"" + key + "\"");
            }
        }
    }

    private static void setData(Object obj, AccessibleObject member, Object value, DataParser data) throws IllegalAccessException, JSONException, InvocationTargetException {
        if (member instanceof Field) {
            setData(obj, (Field) member, value, data);
        } else {
            setData(obj, (Method) member, value);
        }
    }

    private static void setData(Object obj, Field field, Object value, DataParser data) throws IllegalAccessException, JSONException, InvocationTargetException {
        Class<?> fieldType = field.getType();
        Object fieldObject = field.get(obj);
        DataParser valueData = parse(value);

        if (valueData.isJson()) {
            setData(fieldObject, valueData);
        } else if (isPrimitive(fieldType) || fieldType.equals(String.class) || Enum.class.isAssignableFrom(fieldType)) {
            field.set(obj, convert(fieldType, value));
        } else if (fieldObject != null && DataFillable.class.isAssignableFrom(fieldType)) {
            ((DataFillable) fieldObject).setData(value);
        } else if (fieldObject != null && Iterable.class.isAssignableFrom(fieldType)) {
            setData((Iterable) fieldObject, data);
        }
    }

    private static void setData(Object obj, Method method, Object value) throws IllegalAccessException, JSONException, InvocationTargetException {
        Class<?> returnedType = method.getReturnType();
        if (DataFillable.class.isAssignableFrom(returnedType) || Iterable.class.isAssignableFrom(returnedType)) {
            setData(method.invoke(obj), value);
        } else {
            Class<?>[] paramTypes = method.getParameterTypes();
            JSONArray params;

            DataParser valueData = parse(value);
            if (valueData.isJson()) {
                params = valueData.toJsonObject().optJSONArray("!params");
            } else {
                params = valueData.toJsonArray();
            }
            Object result;

            if (isEmpty(paramTypes)) {
                result = method.invoke(obj);
            } else {
                if (params.length() != paramTypes.length) {
                    throw new IllegalArgumentException("Count of values \"" + params + "\" not equal for method  \"" + method.getName() + "\" params " + Arrays.asList(paramTypes));
                }
                result = method.invoke(obj, convert(paramTypes, params));
            }

            if (valueData.isJson()) {
                JSONObject jsonValue = valueData.toJsonObject();
                jsonValue.remove("!params");
                setData(result, valueData);
            }
        }
    }

    private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || Boolean.class.equals(type)
                || Integer.class.equals(type)
                || Character.class.equals(type)
                || Byte.class.equals(type)
                || Short.class.equals(type)
                || Long.class.equals(type)
                || Float.class.equals(type)
                || Double.class.equals(type)
                || Void.class.equals(type);
    }

    public static void setData(Iterable iterable, Object rawData) {
        setData(iterable, DataParser.parse(rawData));
    }

    private static void setData(Iterable iterable, DataParser data) {
        try {
            Iterator iterator = iterable.iterator();
            int index = 0;
            JSONArray jsonArray = data.toJsonArray();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof DataFillable) {
                    ((DataFillable) next).setData(jsonArray.get(index));
                    index++;
                } else {
                    throw new IllegalArgumentException("Iterable object \"" + iterable + "\" must contain DataFillable elements");
                }
            }
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static String getData(Iterable iterable) {

        Iterator iterator = iterable.iterator();
        JSONArray values = new JSONArray();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof DataContainer) {
                DataContainer element = (DataContainer) next;
                String value = element.getData();
                values.put(value);
            } else {
                throw new IllegalArgumentException("Iterable object \"" + iterable + "\" must contain DataFillable elements");
            }
        }
        return values.toString();
    }
}