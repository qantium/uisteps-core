package com.qantium.uisteps.core.factory;

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.browser.LocatorFactory;
import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.context.Context;
import com.qantium.uisteps.core.browser.context.UseContext;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.name.NameConverter;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.By;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class UIObjectFactory {

    private final Browser browser;
    private final LocatorFactory locatorFactory ;

    public UIObjectFactory(Browser browser) {
        this.browser = browser;
        locatorFactory = new LocatorFactory();
    }

    public <T extends UIObject> T get(Class<T> uiObject) {
        return get(uiObject, null, null);
    }

    public <T extends UIObject> T get(Class<T> uiObject, By locator) {
        return get(uiObject, null, locator);
    }

    public <T extends UIObject> T get(Class<T> uiObject, UIObject context) {
        return get(uiObject, context, null);
    }

    public <T extends UIObject> T get(Class<T> uiObject, UIObject context, By locator) {
        T uiObjectInstance = getInstanceOf(uiObject);
        return get(uiObjectInstance, context, locator);
    }

    private  <T extends UIObject> T get(T uiObject, UIObject context, By locator) {

        uiObject.setBrowser(browser);

        if (uiObject instanceof UIElement) {
            initAsUIElement(uiObject, context, locator);
        }

        if (!uiObject.getClass().isAnnotationPresent(NotInit.class)) {

            try {
                for (Field field : getUIObjectFields(uiObject)) {

                    if (field.get(uiObject) == null) {
                        UIElement uiElement = getInstanceOf((Class<UIElement>) field.getType());

                        uiElement.setName(NameConverter.humanize(field));
                        field.set(uiObject, uiElement);
                        UIObject fieldContext = uiObject;

                        if (contextPresentsIn(field)) {
                            fieldContext = getContextOf(field);
                        } else if (useContextOf(field)) {

                            if (contextPresentsIn(field.getClass())) {
                                fieldContext = getContextOf(field.getClass());
                            } else {
                                throw new RuntimeException("Context is not set for " + field);
                            }
                        }

                        get(uiElement, fieldContext, locatorFactory.getLocator(field));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return uiObject;
    }

    private <T extends UIObject> void initAsUIElement(T uiObject, UIObject context, By locator) {
        UIElement uiElement = (UIElement) uiObject;

        if (context == null && contextPresentsIn(uiObject.getClass())) {
            context = getContext(uiObject.getClass().getAnnotation(Context.class));
        }

        if (locator == null) {
            locator = getLocator(uiElement);
        }

        uiElement.setContext(context);
        uiElement.setLocator(locator);
    }

    private <T extends UIObject> T getInstanceOf(Class<T> uiObject) {
        try {
            return ConstructorUtils.invokeConstructor(uiObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject, ex);
        }
    }

    private By getLocator(UIElement uiElement) {
        try {
            return locatorFactory.getLocator(uiElement);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
    private boolean useContextOf(AnnotatedElement uiObject) {
        return uiObject.isAnnotationPresent(UseContext.class);
    }

    private boolean contextPresentsIn(AnnotatedElement uiObject) {
        return uiObject.isAnnotationPresent(Context.class);
    }

    private UIObject getContextOf(AnnotatedElement uiObject) {
        Context context = uiObject.getAnnotation(Context.class);
        return getContext(context);
    }

    private UIObject getContext(Context context) {
        Class<? extends UIObject> uiObject = context.value();
        By contextLocator = null;

        if (UIElement.class.isAssignableFrom(uiObject)) {
            contextLocator = getContextLocator(context);
        }

        return get(uiObject, null, contextLocator);
    }

    private By getContextLocator(Context context) {
        try {
            return locatorFactory.getLocator(context.findBy());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private List<Field> getUIObjectFields(UIObject uiObject) throws IllegalArgumentException, IllegalAccessException {
        return getUIObjectFields(uiObject.getClass(), new ArrayList());
    }

    private List<Field> getUIObjectFields(Class<?> uiObjectType, List<Field> fields) throws IllegalArgumentException, IllegalAccessException {

        if (uiObjectType.isAnnotationPresent(NotInit.class)) {
            return fields;
        }

        for (Field field : uiObjectType.getDeclaredFields()) {

            if (!field.isAnnotationPresent(NotInit.class) && UIObject.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }

        return getUIObjectFields(uiObjectType.getSuperclass(), fields);
    }
}
