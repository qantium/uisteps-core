package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.context.Context;
import com.qantium.uisteps.core.browser.context.UseContext;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.browser.pages.elements.Group;
import com.qantium.uisteps.core.browser.pages.elements.Table;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
import com.qantium.uisteps.core.name.NameConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.qantium.uisteps.core.browser.wait.Waiting.waitUntil;

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
    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, By... locators) {
        return getAll(uiObject, null, locators);
    }

    public <T extends UIElement> UIElements<T> getAll(Class<T> uiObject, HtmlObject context, By... locators) {
        UIElements<T> uiElements = new UIElements(uiObject);
        if (ArrayUtils.isEmpty(locators)) {
            locators = locatorFactory.getLocators(uiObject);
        }
        return get(uiElements, context, locators);
    }

    @Override
    public UIElement get(By... locators) {
        if (ArrayUtils.isEmpty(locators)) {
            throw new IllegalArgumentException("Locator is not set!");
        }
        return get(UIElement.class, locators);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject) {
        return get(uiObject, null);
    }

    @Override
    public <T extends UIElement> T get(Class<T> uiObject, By... locators) {
        return get(uiObject, null, locators);
    }

    @Override
    public <T extends UIObject> T get(Class<T> uiObject, HtmlObject context, By... locators) {
        T uiObjectInstance = getInstanceOf(uiObject);
        return get(uiObjectInstance, context, locators);
    }

    private <T extends UIObject> T get(T uiObject, HtmlObject context, By... locators) {

        uiObject.setBrowser(browser);

        if (uiObject instanceof UIElement) {
            initAsUIElement((UIElement) uiObject, context, locators);
        }

        if (!uiObject.getClass().isAnnotationPresent(NotInit.class)) {

            try {
                for (Field field : getUIObjectFields(uiObject)) {

                    if (field.get(uiObject) == null) {
                        Class<?> fieldType = field.getType();

                        AbstractUIObject uiElement;


                        if (UIElement.class.isAssignableFrom(fieldType)) {
                            uiElement = getInstanceOf((Class<UIElement>) field.getType());
                        } else if (Alert.class.isAssignableFrom(fieldType)) {
                            uiElement = getInstanceOf((Class<Alert>) field.getType());
                        } else if (Page.class.isAssignableFrom(fieldType)) {
                            uiElement = getInstanceOf((Class<Page>) field.getType());
                        } else {
                            throw new IllegalStateException("Unknown type \"" + fieldType + "\" to init");
                        }

                        uiElement.setName(NameConverter.humanize(field));
                        field.set(uiObject, uiElement);
                        if (uiObject instanceof HtmlObject) {

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

                            get(uiElement, fieldContext, locatorFactory.getLocators(field));
                        }

                        if (uiElement instanceof Group) {
                            Group group = (Group) uiElement;
                            initGroupElementsLocator(group, field);
                        }

                        if (uiElement instanceof Table) {
                            Table table = (Table) uiElement;
                            initHeaderOf(table, field);
                        }
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(!waitUntil(uiObject, () -> uiObject.isDisplayed())) {
            throw new IsNotDisplayedException(uiObject);
        }
        return uiObject;
    }

    private <T extends UIElement> void initAsUIElement(T uiObject, HtmlObject context, By... locators) {

        if (doNotUseContextFor(uiObject.getClass())) {
            context = null;
        } else if (context == null && contextPresentsIn(uiObject.getClass())) {
            context = getContext(uiObject.getClass().getAnnotation(Context.class));
        }

        if (ArrayUtils.isEmpty(locators)) {
            locators = getLocator(uiObject);
        }

        uiObject.setContext(context);
        uiObject.setLocators(locators);

        if (uiObject instanceof Group) {
            Group group = (Group) uiObject;
            initGroupElementsLocator(group, group.getClass());
        }

        if (uiObject instanceof Table) {
            Table table = (Table) uiObject;
            initHeaderOf(table, table.getClass());
        }
    }

    private void initGroupElementsLocator(Group group, AnnotatedElement annotatedElement) {
        By[] elementsLocator = group.getElementsLocator();

        if (elementsLocator == null && annotatedElement.isAnnotationPresent(Group.Elements.class)) {
            FindBy[] groupElementsBy = annotatedElement.getAnnotation(Group.Elements.class).value();
            elementsLocator = locatorFactory.getLocators(groupElementsBy);
            group.withElementsLocator(elementsLocator);
        }
    }


    private void initHeaderOf(Table table, AnnotatedElement annotatedElement) {

        if (table.headerIsNotInit()) {
            if (annotatedElement.isAnnotationPresent(Table.HeaderIsFirstRow.class)) {
                boolean headerIsFirstRow = annotatedElement.getAnnotation(Table.HeaderIsFirstRow.class).value();
                table.headerIsFirstRow(headerIsFirstRow);
            }
        }

        if (table.headerIsNotInit()) {
            By[] headerLocator = table.getHeaderLocator();

            if (headerLocator == null && annotatedElement.isAnnotationPresent(Table.Header.class)) {
                Table.Header header = annotatedElement.getAnnotation(Table.Header.class);
                FindBy[] headerBy = header.value();

                headerLocator = locatorFactory.getLocators(headerBy);
                table.withHeaderLocator(headerLocator);
            }
        }
    }

    private <T extends UIElement> boolean doNotUseContextFor(Class<T> uiObject) {
        return uiObject.isAnnotationPresent(UseContext.class)
                && uiObject.getAnnotation(UseContext.class).value() == false;
    }

    protected  <T extends UIObject> T getInstanceOf(Class<T> uiObject) {
        try {
            return ConstructorUtils.invokeConstructor(uiObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject, ex);
        }
    }

    private By[] getLocator(UIElement uiElement) {
        try {
            return locatorFactory.getLocators(uiElement);
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
