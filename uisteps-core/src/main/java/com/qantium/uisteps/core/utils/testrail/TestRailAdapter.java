package com.qantium.uisteps.core.utils.testrail;

import com.google.common.io.Files;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import com.qantium.uisteps.core.utils.data.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.qantium.uisteps.core.properties.UIStepsProperties.*;
import static com.qantium.uisteps.core.properties.UIStepsProperty.TESTRAIL_OUTCOME_FILE;

/**
 * Created by Anton Solyankin
 */
public class TestRailAdapter {

    public static TestRailAdapter instance;
    private TestRailConfig config;
    private File outcomeFile;
    private TestRailClient client;
    private Data data;
    private HashMap<String, HashSet<JSONObject>> cases = new HashMap();

    public static TestRailAdapter getInstance(TestRailConfig config) {
        if (instance == null) {
            synchronized (TestRailAdapter.class) {
                if (instance == null) {
                    instance = new TestRailAdapter(config);
                }
            }
        }
        return instance;
    }

    public static TestRailAdapter getInstance() {
        if (instance == null) {
            synchronized (TestRailAdapter.class) {
                if (instance == null) {
                    TestRailConfig config = new TestRailConfig();
                    instance = new TestRailAdapter(config);
                }
            }
        }
        return instance;
    }


    private TestRailAdapter(TestRailConfig config) {
        this.config = config;
        outcomeFile = new File(getProperty(TESTRAIL_OUTCOME_FILE));
        data = new Data(config.toJSON());
        client = new TestRailClient(config.getHost(), config.getUser(), config.getPassword());

        if(isDefined() && !outcomeFileExists()) {
            initOutcomeFile();
        }
    }

    public boolean isDefined() {
        return config.isValid() && config.actionIsDefined();
    }

    private boolean outcomeFileExists() {
        return new File(getProperty(TESTRAIL_OUTCOME_FILE)).exists();
    }

    private void initOutcomeFile() {
        JSONArray tests = getTests();
        data.append("tests", tests);
        setCasesFrom(tests);
        writeOutcomeFile();
    }

    public void addTestResult(int caseID, int status) {
        client.addTestResult(caseID, status);
    }

    public Integer getStatusCode(String status) {
        return config.getStatusCode(status);
    }

    public JSONArray getStatuses() {
        return client.getStatuses();
    }

    public JSONArray getTestsFromRun(int runId) {
        return client.getTestsFromRun(runId);
    }

    private JSONArray getTests() {
        JSONArray tests = new JSONArray();

        for (TestRailEntity entity : config.getRuns()) {

            switch (entity.getType()) {
                case RUN:
                    tests = getTestsFromRun(entity.getId());
                    break;
                default:
                    throw new IllegalArgumentException("Functionality for " + entity.getType() + " has not realized yet!");
            }
        }

        return tests;
    }

    private void setCasesFrom(JSONArray tests) {
        try {
            for (int i = 0; i < tests.length(); i++) {
                JSONObject test = tests.getJSONObject(i);
                String caseID = TestRailType.CASE.mark + test.getString("case_id");
                HashSet<JSONObject> caseTests;

                if (cases.containsKey(caseID)) {
                    caseTests = cases.get(caseID);
                } else {
                    caseTests = new HashSet();
                    cases.put(caseID, caseTests);
                }
                caseTests.add(test);
            }
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeOutcomeFile() {

        try {
            Charset UTF_8 = Charset.forName("UTF-8");

            if (!outcomeFile.isAbsolute()) {
                outcomeFile = new File(outcomeFile.getPath());
            }

            if (outcomeFile.exists()) {
                Files.write(new byte[0], outcomeFile);
            } else {
                Files.createParentDirs(outcomeFile);
            }

            Files.append(getOutcomeData(), outcomeFile, UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getOutcomeData() {
        StringBuilder outcomeData = new StringBuilder();
        outcomeData.append(UIStepsProperty.RUN_GROUPS).append(" = ").append(getCasesString()).append("\n");
        outcomeData.append("testrail.config").append(" = ").append(data).append("\n");
        return outcomeData.toString();
    }

    private String getCasesString() {
        String casesString = Arrays.toString(cases.keySet().toArray());
        return casesString.substring(1, casesString.length() - 1).replace(" ", "");

    }

    public Data getData() {
        return data;
    }
}
