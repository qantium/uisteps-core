package com.qantium.uisteps.core.utils.grid.servlets.robot;


import com.qantium.uisteps.core.utils.grid.servlets.GridServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.internal.Registry;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class RobotServlet extends GridServlet {

    public RobotServlet() {
        this(null);
    }

    public RobotServlet(Registry registry) {
        super(registry);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.print("<h1>If you want to execute RobotServlet actions you need to use POST request</h1>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RobotGridResponse robotResponse = new RobotGridResponse();
        try {
            RobotActions actions;
            if (isSerialized(request)) {
                actions = getActions(request);
            } else {
                String json = getRequestString(request);
                actions = getActions(json);
            }
            run(actions);

            robotResponse.setResults(actions.getResults());
        } catch (JSONException | IllegalArgumentException | ClassNotFoundException | AWTException ex) {
            robotResponse.failed(ex);
            ex.printStackTrace();
        } finally {
            PrintWriter out = response.getWriter();
            out.print(robotResponse);
        }

    }

    protected void run(RobotActions actions) throws AWTException {
        Robot robot = new Robot();

        for (RobotAction action : actions) {
            action.setRobot(robot);
        }

        actions.run();
    }

    protected boolean isSerialized(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType == null || !contentType.contains("application/json");
    }

    protected RobotActions getActions(HttpServletRequest request) throws IOException, ClassNotFoundException {
        ServletInputStream inputStream = request.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (RobotActions) objectInputStream.readObject();
    }

    protected RobotActions getActions(String json) throws JSONException, IOException, ClassNotFoundException {
        List<RobotAction> actions = getActionsList(json);
        return new RobotActions(actions);
    }

    protected List<RobotAction> getActionsList(String request) throws JSONException, IllegalArgumentException {
        JSONObject json = new JSONObject(request);
        JSONArray actionsJSONArray = json.getJSONArray("actions");

        List<RobotAction> actions = new ArrayList();

        for (int i = 0; i < actionsJSONArray.length(); i++) {
            JSONObject jsonAction = actionsJSONArray.getJSONObject(i);
            String actionName = jsonAction.getString("name");

            JSONArray actionParamJSONArray = jsonAction.getJSONArray("params");

            int paramsLength = actionParamJSONArray.length();
            Class<?>[] paramTypes = new Class<?>[paramsLength];
            Object[] paramsValues = new Object[paramsLength];

            for (int j = 0; j < paramsLength; j++) {
                JSONObject jsonParam = actionParamJSONArray.getJSONObject(j);
                Class<?> paramType = getParamType(jsonParam.getString("type"));
                Object paramValue = getParamValue(paramType, jsonParam.getString("value"));
                paramTypes[j] = paramType;
                paramsValues[j] = paramValue;
            }
            RobotAction action = new RobotAction(actionName, paramTypes, paramsValues);
            actions.add(action);
        }
        return actions;
    }

    private Class<?> getParamType(String name) throws IllegalArgumentException {
        switch (name) {
            case "int":
                return Integer.TYPE;
            case "Integer":
                return Integer.TYPE;
            case "String":
                return String.class;
            case "boolean":
                return Boolean.TYPE;
            case "Boolean":
                return Boolean.TYPE;
            default:
                throw new IllegalArgumentException("Cannot get type by name " + name + "! Type name must be one of boolean, Boolean, int, Integer or String");
        }
    }

    private Object getParamValue(Class<?> type, String value) throws IllegalArgumentException {

        if (type.equals(String.class)) {
            return value;
        }

        if (type.equals(Integer.TYPE)) {
            return Integer.parseInt(value);
        }

        if (type.equals(Boolean.TYPE)) {
            return Boolean.valueOf(value);
        }

        throw new IllegalArgumentException("Cannot cast " + value + " to any type of Boolean, Integer or String");
    }
}