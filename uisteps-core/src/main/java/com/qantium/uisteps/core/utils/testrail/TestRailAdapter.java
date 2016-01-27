package com.qantium.uisteps.core.utils.testrail;

import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiRequest;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class TestRailAdapter {

    private TestRailConfig config;
    private RestApi api;

    public TestRailAdapter() {
        this(UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_CONFIG));
    }


    public TestRailAdapter(String config) {

        if (!StringUtils.isEmpty(config)) {
            this.config = new TestRailConfig(config);
            String host = this.config.getHost();
            String user = this.config.getUser();
            String login = this.config.getLogin();
            api = new RestApi(host).setBase64BasicAuthorization(user, login);
            Set<String> tests = getTests();
            setTestGroups(tests);
        }
    }

    public TestRailConfig getConfig() {
        return config;
    }

    public Set<String> getTests() {

        Set<String> tests = new HashSet();
        TestRailContainer container = config.getContainer();

        switch (container) {
            case RUN:
                try {
                    RestApiRequest request = api.getRequest("/api/v2/get_tests/" + config.getId());
                    request.setHeader("Content-Type", "application/json");

                    JSONArray testsJSONArray = request.get().toJSONArray();

                    for (int i = 0; i < testsJSONArray.length(); i++) {
                        tests.add(testsJSONArray.getJSONObject(i).getString("id"));
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                return tests;
            default:
                throw new IllegalArgumentException("Functionality for " + container + " has not realized yet!");
        }
    }

    public void setTestGroups(Set<String> tests) {

        StringBuilder testsStr = new StringBuilder("uisteps.testrail.tests=");
        for (String test : tests) {
            testsStr.append("#").append(test).append(",");
        }

        try {
            Files.write(Paths.get("testrail.tests.properties"), testsStr.toString().getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addResult(String testId, TestRailStatus status) {
        RestApiRequest request = api.getRequest("/api/v2/add_result/" + testId);
        request.setHeader("Content-Type", "application/json");
        try {
            request.post("{\"status_id\":" + status + "}");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
