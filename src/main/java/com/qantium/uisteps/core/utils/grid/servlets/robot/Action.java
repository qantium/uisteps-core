package com.qantium.uisteps.core.utils.grid.servlets.robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Solan on 29.09.2016.
 */
public class Action implements Runnable {

    private Object object;
    private final String name;
    private final Class<?>[] paramTypes;
    private final Object[] params;

    public Action(String name, Class<?>[] paramTypes, Object[] params) {
        this.name = name;
        this.paramTypes = paramTypes;
        this.params = params;
        check();
    }

    private void check() {

        if (paramTypes.length != params.length) {
            throw new IllegalArgumentException("Length of param types array != length of params array!");
        }
    }

    public Action setObject(Object object) {
        this.object = object;
        return this;
    }

    @Override
    public void run() {
        if (object == null) {
            throw new IllegalArgumentException("Object to run method cannot be null!");
        }

        try {
            Method method = object.getClass().getMethod(name, paramTypes);
            method.invoke(object, params);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
