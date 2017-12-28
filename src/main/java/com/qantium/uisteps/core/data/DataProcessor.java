package com.qantium.uisteps.core.data;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.qantium.uisteps.core.data.DataParser.parse;
import static com.qantium.uisteps.core.data.TypeConverter.convert;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class DataProcessor {

    public static String getData(Iterable<? extends DataContainer> obj) {
        try {
            JSONArray values = new JSONArray();
            Iterator<? extends DataContainer> iterator = obj.iterator();

            while (iterator.hasNext()) {
                DataContainer element = iterator.next();
                String data = element.getData();
                values.put(new JSONObject(data));
            }
            return values.toString();
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static String getData(MemberCollector members, Predicate<String> predicate) {
        String[] keys = members.getMembers().keySet().stream().filter(predicate)
                .collect(Collectors.toList()).toArray(new String[0]);

        return getData(members.getObject(), keys);
    }

    public static String getData(Object obj, String... keys) {
        if (obj instanceof DataContainer && new MemberCollector(obj).getMembers().isEmpty()) {
            return ((DataContainer) obj).getData();
        } else if (isDataContainerIterator(obj)) {
            if (ArrayUtils.isNotEmpty(keys)) {
                throw new IllegalArgumentException("For Iterable<? extends DataContainer> \"" + obj + "\" param keys must be empty: " + Arrays.asList(keys));
            }
            return getData((Iterable<? extends DataContainer>) obj);
        }
        return getData(new MemberCollector(obj), Arrays.asList(keys));
    }

    private static String getData(MemberCollector members, List<String> keys) {
        Object obj = members.getObject();
        JSONObject data = new JSONObject();

        members.getMembers().entrySet().stream()
                .filter((key) -> keys.isEmpty() || keys.contains(key))
                .forEach(entry -> {
                    try {
                        String key = entry.getKey();
                        AccessibleObject member = entry.getValue();
                        if (member instanceof Field) {
                            Field field = (Field) member;
                            Object fieldObject = field.get(obj);

                            if (fieldObject == null) {
                                data.put(key, parse(fieldObject).getData());
                            } else {
                                Class<?> fieldType = field.getType();
                                if (DataContainer.class.isAssignableFrom(fieldType)) {
                                    String value = ((DataContainer) fieldObject).getData();
                                    data.put(key, value);
                                } else if (isDataContainerIterator(fieldObject)) {
                                    JSONArray jsonArray = new JSONArray();
                                    Iterable<? extends DataContainer> container = (Iterable<? extends DataContainer>) fieldObject;
                                    Iterator<? extends DataContainer> iterator = container.iterator();

                                    while (iterator.hasNext()) {
                                        DataContainer element = iterator.next();
                                        String value = element.getData();
                                        jsonArray.put(value);
                                    }
                                    data.put(key, jsonArray);
                                } else if (fieldType.isPrimitive()) {
                                    data.put(key, fieldObject.toString());
                                } else {
                                    data.put(key, getData(fieldObject));
                                }
                            }
                        } else {
                            Method method = (Method) member;
                            Class<?> returnedType = method.getReturnType();
                            if (DataContainer.class.isAssignableFrom(returnedType) || Iterable.class.isAssignableFrom(returnedType)) {
                                data.put(key, getData(method.invoke(obj)));
                            }
                        }
                    } catch (JSONException | IllegalAccessException | InvocationTargetException ex) {
                        throw new IllegalArgumentException(ex);
                    }
                });
        return data.toString();
    }

    public static void setData(Object obj, Object rawData) {
        try {
            DataParser data = parse(rawData);
            Map<String, AccessibleObject> members = new MemberCollector(obj).getMembers();

            if (data.isJson()) {
                JSONObject json = data.toJsonObject();
                Iterator keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    Object value = json.get(key);

                    if (members.containsKey(key)) {
                        AccessibleObject member = members.get(key);

                        if (member instanceof Field) {
                            Field field = (Field) member;
                            Class<?> fieldType = field.getType();
                            Object fieldObject = field.get(obj);

                            if (DataFillable.class.isAssignableFrom(fieldType)) {
                                ((DataFillable) fieldObject).setData(value);
                            } else if (isDataFillableIterator(fieldObject)) {
                                setData((Iterable<? extends DataFillable>) fieldObject, value);
                            } else {
                                field.set(obj, convert(fieldType, value));
                            }
                        } else {
                            Method method = (Method) member;
                            Class<?> returnedType = method.getReturnType();
                            if (DataFillable.class.isAssignableFrom(returnedType) || Iterable.class.isAssignableFrom(returnedType)) {
                                setData(method.invoke(obj), value);
                            } else {
                                Class<?>[] paramTypes = method.getParameterTypes();
                                JSONArray params = parse(value).toJsonArray();

                                if (isEmpty(paramTypes)) {
                                    method.invoke(obj);
                                } else {
                                    if (params.length() != paramTypes.length) {
                                        throw new IllegalArgumentException("Count of values \"" + params + "\" not equal for method  \"" + method.getName() + "\" params " + Arrays.asList(paramTypes));
                                    }
                                    method.invoke(obj, convert(paramTypes, params));
                                }
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("\"" + obj + "\" dose not contain member \"" + key + "\"");
                    }
                }

            } else if (obj instanceof DataFillable) {
                ((DataFillable) obj).setData(rawData);
            } else if (isDataFillableIterator(obj)) {
                setData((Iterable<? extends DataFillable>) obj, rawData);
            } else {
                throw new IllegalArgumentException("\"" + obj + "\" must implement DataFillable or Iterable<? extends DataFillable> interfaces or data must be json");
            }
        } catch (JSONException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static boolean isDataContainerIterator(Object obj) {
        try {
            Iterable iterable = (Iterable<? extends DataContainer>) obj;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static boolean isDataFillableIterator(Object obj) {
         try {
             Iterable iterable = (Iterable<? extends DataFillable>) obj;
             return true;
         } catch (Exception ex) {
             return false;
         }
    }

    public static void setData(Iterable<? extends DataFillable> iterable, Object rawData) {
        try {
            DataParser data = parse(rawData);

            JSONArray jsonArray = data.toJsonArray();
            Iterator<? extends DataFillable> iterator = iterable.iterator();

            int index = 0;
            while (iterator.hasNext()) {
                DataFillable obj = iterator.next();
                Object value = jsonArray.get(index);
                setData(obj, value);
                index++;
            }
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }


    public static void main(String[] args) {
        setData(new Els(), "");
    }
    public static class El extends UIElement implements DataContainer {

        @Override
        public String getData() {
            return null;
        }
    }

    public static class Els extends UIElements<El>{

        public Els() {
            super(El.class);
        }
    }
}
