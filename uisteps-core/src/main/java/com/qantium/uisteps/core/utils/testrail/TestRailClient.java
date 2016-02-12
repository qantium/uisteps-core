package com.qantium.uisteps.core.utils.testrail;

import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiException;
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

    public JSONArray getTestsFromRun(String id) {

        try {
            RestApiRequest request = api.createRequest("/get_tests/" + id);
            return request.get().toJSONArray();
        } catch (RestApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONArray getStatuses() {

        try {
            RestApiRequest request = api.createRequest("/get_statuses");
            return request.get().toJSONArray();
        } catch (RestApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addTestResult(String testId, int status) {
        RestApiRequest request = api.createRequest("/add_result/" + testId);
        try {
            request.post("{\"status_id\":" + status + "}");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
