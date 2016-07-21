package com.qantium.uisteps.core.factory;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.context.Context;
import com.qantium.uisteps.core.browser.context.UseContext;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIObject;
import com.qantium.uisteps.core.name.NameConverter;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solan on 21.07.2016.
 */
public class UIObjectFactory {

    public <T extends UIObject> T init(Class<T> uiObject, UIObject context, By locator) {
        return init(uiObject, context, locator, true);
    }

    public  <T extends UIObject> T init(T uiObject, UIObject context, By locator) {

        uiObject.setBrowser(this);

        if (uiObject instanceof UIElement) {
            UIElement uiElement = (UIElement) uiObject;

            if (context == null && uiObject.getClass().isAnnotationPresent(Context.class)) {
                context = getContext(uiObject.getClass().getAnnotation(Context.class));
            }

            if (locator == null) {
                locator = getLocator(uiElement);
            }

            uiElement.setContext(context);
            uiElement.setLocator(locator);
        }

        if (!uiObject.getClass().isAnnotationPresent(NotInit.class)) {

            try {

                for (Field field : getUIObjectFields(uiObject)) {

                    if (field.get(uiObject) == null) {
                        UIElement uiElement = getInstanceOf((Class<UIElement>) field.getType());

                        uiElement.setName(NameConverter.humanize(field));
                        field.set(uiObject, uiElement);
                        UIObject fieldContext = uiObject;

                        if (field.isAnnotationPresent(Context.class)) {
                            fieldContext = getContext(field.getAnnotation(Context.class));
                        } else if (field.isAnnotationPresent(UseContext.class)) {

                            if (field.getClass().isAnnotationPresent(Context.class)) {
                                fieldContext = getContext(field.getClass().getAnnotation(Context.class));
                            } else {
                                throw new RuntimeException("Context is not set for " + field);
                            }
                        }

                        initWithoutAfterInitialization(uiElement, fieldContext, getLocatorFactory().getLocator(field));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return uiObject;
    }


    private By getLocator(UIElement uiElement) {
        try {
            return getLocatorFactory().getLocator(uiElement);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private UIObject getContext(Context context) {
        Class<? extends UIObject> uiObject = context.value();
        By contextLocator = null;

        if (UIElement.class.isAssignableFrom(uiObject)) {
            contextLocator = getContextLocator(context);
        }

        return init(uiObject, null, contextLocator);
    }

    private By getContextLocator(Context context) {
        try {
            return getLocatorFactory().getLocator(context.findBy());
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
