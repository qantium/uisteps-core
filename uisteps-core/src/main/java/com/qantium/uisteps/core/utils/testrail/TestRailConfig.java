package com.qantium.uisteps.core.utils.testrail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class TestRailConfig {

    private final String config;
    private final JSONObject jsonConfig;
    private final String host;
    private final String user;
    private final String password;
    private final TestRailAction  action;
    private final Set<TestRailEntity> containers = new HashSet();


    public TestRailConfig(String config) {
        this.config = config;
        jsonConfig = getConfigJSON(config);

        host = getString("host");
        user = getString("user");
        password = getString("password");
        action = TestRailAction.valueOf(getString("action").toUpperCase());
        initContainers();
    }

    private JSONObject getConfigJSON(String config) {
        try {
            return new JSONObject(config);
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private void initContainers() {
        try {
            JSONArray runArray = jsonConfig.getJSONArray("run");

            for(int i =0; i < runArray.length(); i++) {
                TestRailEntity container = new TestRailEntity(runArray.getString(i));
                containers.add(container);
            }
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }


    protected String getString(String key) {
        try {
            return jsonConfig.getString(key);
        } catch (JSONException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public TestRailAction getAction() {
        return action;
    }

    public Set<TestRailEntity> getContainers() {
        return containers;
    }

    @Override
    public String toString() {
        return config;
    }
}
