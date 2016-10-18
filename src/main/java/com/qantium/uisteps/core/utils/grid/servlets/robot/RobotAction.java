package com.qantium.uisteps.core.utils.grid.servlets.robot;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class RobotAction implements Runnable, Serializable {

    private Robot robot;
    private final String name;
    private final Class<?>[] paramTypes;
    private final Object[] params;
    private List<Object> results;

    public RobotAction(String name) {
        this(name, new Class<?>[]{}, new Object[]{});
    }

    protected List<Object> getResults() {
        return results;
    }

    protected void setResults(List<Object> results) {
        this.results = results;
    }

    public RobotAction(String name, Class<?>[] paramTypes, Object[] params) {
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

    public RobotAction setRobot(Robot robot) {
        this.robot = robot;
        return this;
    }

    @Override
    public void run() {
        if (robot == null) {
            throw new IllegalArgumentException("Object to run method cannot be null!");
        }

        try {
            Method method = robot.getClass().getMethod(name, paramTypes);
            Object result = method.invoke(robot, params);
            if(results != null) {
                results.add(result);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public Object[] getParams() {
        return params;
    }
}
