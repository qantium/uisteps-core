package com.qantium.uisteps.core.utils.testrail;

import com.google.common.io.Files;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Anton Solyankin
 */
public class TestRailAdapter {

    public static final String TESTRAIL_TESTS_PATH = UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_TESTS_PATH);
    private TestRailConfig config;
    private TestRailClient client;
    private boolean init;

    public TestRailAdapter() {
        this(UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_CONFIG));
    }

    public TestRailAdapter(String config) {

        init = !StringUtils.isEmpty(config);

        if (init) {
            this.config = new TestRailConfig(config);
            String host = this.config.getHost();
            String user = this.config.getUser();
            String password = this.config.getPassword();
            client = new TestRailClient(host, user, password);
        }
    }

    public TestRailConfig getConfig() {
        return config;
    }

    public Set<String> getTests() {

        Set<TestRailEntity> entities = config.getContainers();
        Set<String> tests = new HashSet();

        for (TestRailEntity entity : entities) {

            switch (entity.getType()) {
                case RUN:
                    tests.addAll(client.getTestsFromRun(entity.getId()));
                    break;
                default:
                    throw new IllegalArgumentException("Functionality for " + entity.getType() + " has not realized yet!");
            }
        }

        return tests;
    }

    public Set<TestRailEntity> getTestEntities(Set<String> tests) {

        Set<TestRailEntity> entities = new HashSet();

        for (String testId : tests) {
            entities.add(new TestRailEntity(TestRailContainerType.TEST, testId));
        }
        return entities;
    }

    public void addTestResult(String testId, TestRailStatus status) {
        client.addTestResult(testId, status);
    }

    public void init() {
        init(TESTRAIL_TESTS_PATH);
    }

    public void init(String path) {
        if (init) {
            StringBuilder sb = new StringBuilder();

            Set<String> tests = getTests();
            Set<TestRailEntity> testEntities = getTestEntities(tests);

            for (TestRailEntity test : testEntities) {
                sb.append(test).append(",");
            }

            Map<UIStepsProperty, Object> properties = new HashMap<>();

            properties.put(UIStepsProperty.TESTRAIL_TESTS, sb);
            properties.put(UIStepsProperty.TESTRAIL_CONFIG, config);

            write(properties, path);
        }
    }


    private void write(Map<UIStepsProperty, Object> properties, String path) {
        try {
            Charset UTF_8 = Charset.forName("UTF-8");
            File file = new File(path);

            if (!file.isAbsolute()) {
                file = new File(System.getProperty("user.dir") + path);
            }

            if (file.exists()) {
                Files.write(new byte[0], file);
            } else {
                Files.createParentDirs(file);
            }

            for (UIStepsProperty key : properties.keySet()) {
                Files.append(key + "=" + properties.get(key) + "\n", file, UTF_8);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws JSONException {
        new TestRailAdapter().init();

    }
}
