package com.qantium.uisteps.core.utils.grid.servlets.robot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class Robot extends RegistryBasedServlet {

    private static final long serialVersionUID = 115148461514875L;

    public Robot() {
        this(null);
    }

    public Robot(Registry registry) {
        super(registry);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String json = getRequestJson(request);
            List<Action> actions = getActions(json);
            Robot robot = new Robot();

            for (Action action : actions) {
                action.setObject(robot).run();
            }
        } catch (IOException | JSONException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.print("<h1>If you want to execute Robot actions you need to use POST request</h1>");
    }

    protected String getRequestJson(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonRequest = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            jsonRequest.append(line);
        }
        return jsonRequest.toString();
    }

    protected List<Action> getActions(String request) throws JSONException, IllegalArgumentException {
        JSONObject json = new JSONObject(request);
        JSONArray actionsJSONArray = json.getJSONArray("actions");

        List<Action> actions = new ArrayList();

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
            Action action = new Action(actionName, paramTypes, paramsValues);
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