package com.qantium.uisteps.core.utils.testrail;

import com.google.common.io.Files;
import com.qantium.uisteps.core.properties.UIStepsProperties;
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
/**
 * Created by Anton Solyankin
 */
public class TestRailAdapter implements TestRail.Adapter {

    private TestRailConfig config;
    private File outcomeFile;
    private TestRailClient client;
    private Data data;
    private HashMap<String, HashSet<JSONObject>> cases = new HashMap();

    public TestRailAdapter(TestRailConfig config) {
        this.config = config;
        outcomeFile = new File(UIStepsProperties.getProperty(UIStepsProperty.TESTRAIL_OUTCOME_FILE));
        data = new Data(config.toJSON());
        client = new TestRailClient(config.getHost(), config.getUser(), config.getPassword());
        JSONArray tests = getTests();
        data.append("tests", tests);
        setCasesFrom(tests);
        writeOutcome();
    }

    @Override
    public void addTestResult(String caseID, int status) {

        if (caseID.startsWith(TestRailType.CASE.mark)) {
            caseID = new TestRailEntity(caseID).getId();
        }

        client.addTestResult(caseID, status);
    }

    private JSONArray getStatuses() {
        return client.getStatuses();
    }

    private JSONArray getTests() {
        JSONArray tests = new JSONArray();

        for (TestRailEntity entity : config.getRuns()) {

            switch (entity.getType()) {
                case RUN:
                    tests = client.getTestsFromRun(entity.getId());
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
                String caseID = test.getString("case_id");
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

    private void writeOutcome() {

        try {
            Charset UTF_8 = Charset.forName("UTF-8");

            if (!outcomeFile.isAbsolute()) {
                outcomeFile = new File(System.getProperty("user.dir") + outcomeFile.getPath());
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
}
