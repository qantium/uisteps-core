package com.qantium.uisteps.core.browser.pages;

import com.google.gson.Gson;
import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
import com.qantium.uisteps.core.browser.wait.TimeoutBuilder;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.then.Then;
import org.apache.commons.io.Charsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;

import static org.codehaus.plexus.util.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
@NotInit
public abstract class AbstractUIObject implements UIObject {

    private String name;
    private IBrowser browser;
    private final TimeoutBuilder timeoutBuilder = new TimeoutBuilder();

    public <T extends AbstractUIObject> T immediately() {
        return withTimeout(0);
    }


    public IBrowser inOpenedBrowser() {
        return browser;
    }

    @Override
    public void setBrowser(IBrowser browser) {
        this.browser = browser;
    }

    public <T extends AbstractUIObject> T withName(String name) {
        setName(name);
        return (T) this;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        if (isEmpty(name)) {
            setName(NameConverter.humanize(getClass()));
        }
        return name;
    }

    @Override
    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    @Override
    public TimeoutBuilder getTimeoutBuilder() {
        return timeoutBuilder;
    }

    public abstract String getText();

    @Override
    public boolean isNotCurrentlyDisplayed() {
        return !isCurrentlyDisplayed();
    }

    @Override
    public String toString() {
        return getName();
    }


    private MemberCollector fieldCollector;

    public Object setContent(String key, Object... values) {
        LinkedHashMap<String, Object> valueMap = new LinkedHashMap<>();
        if (values == null || values.length == 0) {
            valueMap.put(key, null);
        } else if (values.length == 1) {
            valueMap.put(key, values[0]);
        } else {
            valueMap.put(key, Arrays.asList(values));
        }
        return setValue(valueMap);
    }

    public Object setContent(File file) {
        JSONObject json = getJsonFromFile(file);
        return setContent(json);
    }

    public boolean hasContent(File file) {
        JSONObject json = getJsonFromFile(file);
        return hasContent(json);
    }

    private JSONObject getJsonFromFile(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()), Charsets.UTF_8);
            return new JSONObject(content);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot read file " + file);
        } catch (JSONException ex) {
            throw new IllegalArgumentException("Content of file " + file + " is not json");
        }
    }

    public Object setContent(Object value) {
        if (value instanceof JSONObject) {
            JSONObject json = (JSONObject) value;
            return setContent(json);
        } else {
            try {
                JSONObject json = new JSONObject(value.toString());
                return setContent(json);
            } catch (Exception ex) {
                return setStructContent(value);
            }
        }
    }

    public boolean hasContent(Object content) {
        if (content instanceof JSONObject) {
            JSONObject json = (JSONObject) content;
            return hasContent(json);
        } else {
            try {
                JSONObject json = new JSONObject(content.toString());
                return hasContent(json);
            } catch (Exception ex) {
                return Objects.deepEquals(content, getContent());
            }
        }
    }

    public Object setContent(JSONObject json) {
        Object content = geGson(json);
        return setStructContent(content);
    }

    private Object geGson(JSONObject json) {
        return new Gson().fromJson(json.toString(), getContentType(json));
    }

    public boolean hasContent(JSONObject json) {
        Object gson = geGson(json);
        Object content = geGson(getJsonContent());
        return Objects.deepEquals(gson, content);
    }

    private Object setStructContent(Object value) {
        if (value instanceof LinkedHashMap) {
            return setValue((LinkedHashMap<String, Object>) value);
        } else {
            return setValue(value);
        }
    }

    private Class<?> getContentType(JSONObject json) {
        try {
            new JSONArray(json);
            return List.class;
        } catch (JSONException ex) {
            return LinkedHashMap.class;
        }
    }

    protected Object setValue(LinkedHashMap<String, Object> values) {

        HashMap<String, Queue<AccessibleObject>> members = new HashMap<>(getFieldCollector().getMembers());
        values.entrySet().stream().forEach(entry -> {

                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (!members.containsKey(key)) {
                        throw new NoSuchElementException(this + " does not contain element with name " + key);
                    }

                    AccessibleObject member = members.get(key).peek();
                    AbstractUIObject htmlObject = getHtmlObjectFrom(member);
                    htmlObject.setContent(value);
                }
        );
        return null;
    }

    protected abstract Object setValue(Object value);

    public JSONObject getJsonContent() {
        String gson = new Gson().toJson(getContent());
        try {
            return new JSONObject(gson);
        } catch (JSONException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public JSONObject getJsonContent(String... keys) {
        String gson = new Gson().toJson(getContent(keys));
        try {
            return new JSONObject(gson);
        } catch (JSONException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public Object getContent() {
        Map<String, Queue<AccessibleObject>> members = getFieldCollector().getMembers();
        if (members.size() == 0) {
            return getText();
        } else {
            return getContent(members.keySet().toArray(new String[members.size()]));
        }
    }

    public LinkedHashMap<String, Object> getContent(String... keys) {
        Map<String, Queue<AccessibleObject>> members = getFieldCollector().getMembers();
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();

        Arrays.asList(keys).stream().forEach(key -> {
            if (members.containsKey(key)) {

                members.get(key).forEach((member) -> {

                    AbstractUIObject htmlObject = getHtmlObjectFrom(member);
                    Object value = htmlObject.getContent();
                    if (htmlObject instanceof UIElements) {
                        values.put(key, value);

                    } else {
                        Object valueList;
                        if (values.containsKey(key)) {
                            valueList = values.get(key);

                            if (valueList instanceof List) {
                                ((List) valueList).add(value);
                            } else {
                                Object oldValue = valueList;
                                valueList = new ArrayList<>();
                                values.put(key, valueList);
                                ((List) valueList).add(oldValue);
                                ((List) valueList).add(value);
                            }
                        } else {
                            values.put(key, value);
                        }
                    }
                });

            } else {
                throw new NoSuchElementException(this + " does not contain element with name " + key);
            }
        });

        return values;
    }

    private MemberCollector getFieldCollector() {
        if (fieldCollector == null) {
            fieldCollector = new MemberCollector(this);
        }
        return fieldCollector;
    }

    private AbstractUIObject getHtmlObjectFrom(AccessibleObject member) {
        try {
            member.setAccessible(true);
            if (member instanceof Field) {
                Field field = (Field) member;
                return (AbstractUIObject) field.get(this);

            } else {
                Method method = (Method) member;
                return (AbstractUIObject) method.invoke(this);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
