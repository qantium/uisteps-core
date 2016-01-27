package com.qantium.uisteps.core.utils.testrail;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anton Solyankin
 */
public class TestRailConfig {

    private final String config;
    private final JSONObject jsonConfig;
    private String host;
    private String user;
    private String login;
    private TestRailAction action;
    private TestRailContainer container;
    private String id;


    public TestRailConfig(String config) {
        this.config = config;
        jsonConfig = getConfigJSON(config);

        host = getString("host");
        user = getString("user");
        login = getString("login");
        action = TestRailAction.valueOf(getString("action").toUpperCase());
        container = TestRailContainer.valueOf(getString("container").toUpperCase());
        id = getString("id");
    }

    private JSONObject getConfigJSON(String config) {
        try {
            return new JSONObject(config);
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

    public String getLogin() {
        return login;
    }

    public TestRailAction getAction() {
        return action;
    }


    public TestRailContainer getContainer() {
        return container;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return config;
    }
}
