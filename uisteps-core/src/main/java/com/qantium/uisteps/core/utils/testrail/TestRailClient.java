package com.qantium.uisteps.core.utils.testrail;

import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiRequest;
import org.json.JSONArray;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class TestRailClient {

    private final RestApi api;


    public TestRailClient(String host, String user, String password) {;
        api = new RestApi(host + "/api/v2")
                .setBase64BasicAuthorization(user, password)
                .setHeader("Content-Type", "application/json");
    }

    public String getPassword() {
        return api.getPassword();
    }

    public String getUser() {
        return api.getLogin();
    }

    public String getHost() {
        return api.getPassword();
    }

    public String getBasicAuthorization() {
        return api.getBasicAuthorization();
    }

    public Set<String> getTestsFromRun(String id) {
        try {
            Set<String> tests = new HashSet();
            RestApiRequest request = api.createRequest("/get_tests/" + id);
            JSONArray testsJSONArray = request.get().toJSONArray();

            for (int i = 0; i < testsJSONArray.length(); i++) {
                tests.add(testsJSONArray.getJSONObject(i).getString("id"));
            }
            return tests;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addTestResult(String testId, TestRailStatus status) {
        RestApiRequest request = api.createRequest("/add_result/" + testId);
        try {
            request.post("{\"status_id\":" + status + "}");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
