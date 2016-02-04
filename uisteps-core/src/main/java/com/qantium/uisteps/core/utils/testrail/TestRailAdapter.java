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
    private TestRailClient client;

    public TestRailAdapter() {
        this(UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_CONFIG));
    }

    public TestRailAdapter(String config) {

        if (!StringUtils.isEmpty(config)) {
            this.config = new TestRailConfig(config);
            String host = this.config.getHost();
            String user = this.config.getUser();
            String password = this.config.getPassword();
            client = new TestRailClient(host, user, password);
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
                return client.getTestsFromRun(config.getId());
            default:
                throw new IllegalArgumentException("Functionality for " + container + " has not realized yet!");
        }
    }

    private void setTestGroups(Set<String> tests) {

        StringBuilder testsStr = new StringBuilder();
        testsStr.append(UIStepsProperty.TESTRAIL_TESTS).append("=");

        for (String test : tests) {
            testsStr.append("#").append(test).append(",");
        }

        try {
            String path = UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_TESTS_PATH);
            Files.write(Paths.get(path), testsStr.toString().getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addTestResult(String testId, TestRailStatus status) {
        client.addTestResult(testId, status);
    }

}
