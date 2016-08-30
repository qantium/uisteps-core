package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.context.Context;
import com.qantium.uisteps.core.browser.context.UseContext;
import com.qantium.uisteps.core.browser.pages.HtmlObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
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
public class UIObjectFactory implements IUIObjectFactory {

    private final IBrowser browser;
    private final LocatorFactory locatorFactory;

    public UIObjectFactory(IBrowser browser) {
        this.browser = browser;
        locatorFactory = new LocatorFactory();
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject) {
        return getAll(uiObject, locatorFactory.getLocator(uiObject));
    }

    @Override
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By locator) {
        return getAll(uiObject, null, locator);
    }

    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, HtmlObject context) {
        UIElements<T> uiElements = new UIElements(uiObject);
        return get(uiElements, context, null);
    }

    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, HtmlObject context, By locator) {
        UIElements<T> uiElements = new UIElements(uiObject);
        return get(uiElements, context, locator);
    }

    @Override
    public UIElement get(By locator) {
        return get(UIElement.class, locator);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return get(uiObject, null, null);
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By locator) {
        return get(uiObject, null, locator);
    }

    public <T extends HtmlObject> T get(Class<T> uiObject, HtmlObject context) {
        return get(uiObject, context, null);
    }

    public <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By locator) {
        T uiObjectInstance = getInstanceOf(uiObject);
        return get(uiObjectInstance, context, locator);
    }

    private <T extends UIObject> T get(T uiObject, HtmlObject context, By locator) {

        uiObject.setBrowser(browser);

        if (uiObject instanceof UIElement) {
            initAsUIElement((UIElement) uiObject, context, locator);
        }

        if (!uiObject.getClass().isAnnotationPresent(NotInit.class)) {

            try {
                for (Field field : getUIObjectFields(uiObject)) {

                    if (field.get(uiObject) == null) {
                        UIElement uiElement = getInstanceOf((Class<UIElement>) field.getType());

                        uiElement.setName(NameConverter.humanize(field));
                        field.set(uiObject, uiElement);
                        HtmlObject fieldContext = (HtmlObject) uiObject;

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

    private <T extends UIElement> void initAsUIElement(T uiObject, HtmlObject context, By locator) {

        if (doNotUseContextFor(uiObject.getClass())) {
            context = null;
        } else if (context == null && contextPresentsIn(uiObject.getClass())) {
            context = getContext(uiObject.getClass().getAnnotation(Context.class));
        }

        if (locator == null) {
            locator = getLocator(uiObject);
        }

        uiObject.setContext(context);
        uiObject.setLocator(locator);
    }

    private <T extends UIElement> boolean doNotUseContextFor(Class<T> uiObject) {
        return uiObject.isAnnotationPresent(UseContext.class) && uiObject.getAnnotation(UseContext.class).value() == false;
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

    private HtmlObject getContextOf(AnnotatedElement uiObject) {
        Context context = uiObject.getAnnotation(Context.class);
        return getContext(context);
    }

    private HtmlObject getContext(Context context) {
        Class<? extends HtmlObject> uiObject = context.value();
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
