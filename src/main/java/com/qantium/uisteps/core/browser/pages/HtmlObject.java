/*
 * Copyright 2015 A.Solyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qantium.uisteps.core.browser.pages;

import com.google.gson.Gson;
import com.qantium.uisteps.core.browser.*;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.browser.pages.elements.actions.BrowserActions;
import com.qantium.uisteps.core.browser.pages.elements.actions.UIElementActions;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.screenshots.Screenshot;
import org.apache.commons.io.Charsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Anton Solyankin
 */

public abstract class HtmlObject extends AbstractUIObject implements ScriptExecutor, IUIObjectFactory, ISearchContext, SearchContext, WithSearchContext {

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
                    HtmlObject htmlObject = getHtmlObjectFrom(member);
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

                    HtmlObject htmlObject = getHtmlObjectFrom(member);
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

    private HtmlObject getHtmlObjectFrom(AccessibleObject member) {
        try {
            member.setAccessible(true);
            if (member instanceof Field) {
                Field field = (Field) member;
                return (HtmlObject) field.get(this);

            } else {
                Method method = (Method) member;
                return (HtmlObject) method.invoke(this);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return inOpenedBrowser().executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return inOpenedBrowser().executeAsyncScript(script, args);
    }

    @Override
    public <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    @Override
    public UIElement onDisplayed(By... locators) {
        return onDisplayed(get(locators));
    }

    @Override
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(get(uiObject));
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By... locators) {
        return onDisplayed(get(uiObject, locators));
    }

    @Override
    public <T extends UIElement> UIElements<T> onAllDisplayed(Class<T> uiObject, By... locators) {
        return onDisplayed(getAll(uiObject, locators));
    }

    @Override
    public <T extends UIElement> T onDisplayed(Class<T> uiObject, Supplier<By[]> supplier) {
        return onDisplayed(uiObject, supplier.get());
    }

    @Override
    public <E extends UIElement> UIElements<E> onAllDisplayed(Class<E> uiObject, Supplier<By[]> supplier) {
        return onAllDisplayed(uiObject, supplier.get());
    }

    @Override
    public UIElement get(By... locator) {
        return getUIObjectFactory().get(UIElement.class, getChildContext(), locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By... locators) {
        return getUIObjectFactory().get(uiObject, context, locators);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        if (UIElement.class.isAssignableFrom(uiObject)) {
            return (T) getUIObjectFactory().get((Class<UIElement>) uiObject, getChildContext());
        } else if (Page.class.isAssignableFrom(uiObject) || Alert.class.isAssignableFrom(uiObject)) {
            return inOpenedBrowser().get(uiObject);
        } else {
            throw new IllegalArgumentException("Cannot get " + uiObject + " from HtmlObject! Only Alerts and HtmlObjects are allowed!");
        }
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By... locator) {
        return getUIObjectFactory().get(uiObject, getChildContext(), locator);
    }


    private UIObjectFactory getUIObjectFactory() {
        return new UIObjectFactory(inOpenedBrowser());
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By... locators) {
        return getUIObjectFactory().getAll(uiObject, getChildContext(), locators);
    }

    public abstract Screenshot takeScreenshot();


    protected <T extends UIObject> HtmlObject getChildContext() {
        return this;
    }
}
