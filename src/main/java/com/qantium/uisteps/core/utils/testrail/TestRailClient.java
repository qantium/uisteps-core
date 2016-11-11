package com.qantium.uisteps.core.utils.testrail;

import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiException;
import com.qantium.net.rest.RestApiRequest;
import com.qantium.uisteps.core.utils.testrail.entity.TestRailCase;
import com.qantium.uisteps.core.utils.testrail.entity.TestRailProject;
import com.qantium.uisteps.core.utils.testrail.entity.TestRailRun;
import com.qantium.uisteps.core.utils.testrail.entity.TestRailTest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;


/**
 * Created by Anton Solyankin
 */
public class TestRailClient {

    private final RestApi api;
    public static TestRailClient instance;

    public static TestRailClient getInstance() {
        String host = getProperty(TESTRAIL_HOST);
        String login = getProperty(TESTRAIL_LOGIN);
        String password = getProperty(TESTRAIL_PASSWORD);

        return getInstance(host, login, password);
    }

    public static TestRailClient getInstance(String host, String login, String password) {
        if (instance == null) {
            synchronized (TestRailClient.class) {
                if (instance == null) {
                    instance = new TestRailClient(host, login, password);
                }
            }
        }
        return instance;
    }

    private TestRailClient(String host, String login, String password) {
        api = new RestApi(host + "/index.php?/api/v2")
                .setBase64BasicAuthorization(login, password)
                .setHeader("Content-Type", "application/json");
    }


    public Manage manage() {
        return new Manage();
    }

    public class Manage {

        public Set<TestRailStatus> getTestRailStatuses() {
            try {
                JSONArray json = getStatuses();

                Set<TestRailStatus> statuses = new HashSet();

                for (int i = 0; i < json.length(); i++) {
                    JSONObject status = json.getJSONObject(i);
                    statuses.add(new TestRailStatus(status.getString("name"), status.getInt("id")));
                }
                return statuses;
            } catch (JSONException ex) {
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

        public JSONObject getUserByEmail(String email) {
            try {
                RestApiRequest request = api.createRequest("/get_user_by_email&email=" + email);
                return request.get().toJSONObject();
            } catch (RestApiException ex) {
                throw new RuntimeException(ex);
            }
        }

        public JSONArray getUsers() {
            try {
                RestApiRequest request = api.createRequest("/get_users");
                return request.get().toJSONArray();
            } catch (RestApiException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public Cases cases() {
        return new Cases();
    }

    public class Cases {
        public TestRailCase getById(int id) throws RestApiException {
            RestApiRequest request = api.createRequest("/get_case/" + id);
            JSONObject description = request.get().toJSONObject();
            return new TestRailCase(description);
        }

        public List<TestRailCase> getFrom(TestRailProject project) throws RestApiException {
            return getFrom(project, new HashMap());
        }

        public List<TestRailCase> getFrom(TestRailProject project, Map<String, Object> filters) throws RestApiException {

            StringBuilder req = new StringBuilder("/get_cases/" + project.getId());

            for(Map.Entry<String, Object> filter: filters.entrySet()) {
                req.append("&")
                        .append(filter.getKey())
                        .append("=")
                        .append(filter.getValue());
            }

            RestApiRequest request = api.createRequest(req.toString());
            JSONArray json = request.get().toJSONArray();

            try {
                List<TestRailCase> cases = new ArrayList();

                for (int i = 0; i < json.length(); i++) {
                    JSONObject testCase = json.getJSONObject(i);
                    cases.add(new TestRailCase(testCase));
                }
                return cases;
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Tests tests() {
        return new Tests();
    }

    public class Tests {

        public TestRailTest getById(int id) throws RestApiException {
            RestApiRequest request = api.createRequest("/get_test/" + id);
            JSONObject description = request.get().toJSONObject();
            return new TestRailTest(description);
        }

        public List<TestRailTest> getFrom(TestRailRun run) throws RestApiException {
            try {
                RestApiRequest request = api.createRequest("/get_tests/" + run.getId());
                JSONArray json = request.get().toJSONArray();

                List<TestRailTest> tests = new ArrayList();

                for (int i = 0; i < json.length(); i++) {
                    TestRailTest test = new TestRailTest(json.getJSONObject(i));
                    tests.add(test);
                }
                return tests;
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Runs runs() {
        return new Runs();
    }

    public class Runs {

        public TestRailRun getById(int id) throws RestApiException {
            RestApiRequest request = api.createRequest("/get_run/" + id);
            JSONObject description = request.get().toJSONObject();
            return new TestRailRun(description);

        }

        public TestRailRun add(TestRailProject project, JSONObject json) throws RestApiException {
            RestApiRequest request = api.createRequest("/add_run/" + project.getId());
            JSONObject description = request.post(json).toJSONObject();
            return new TestRailRun(description);
        }

        public void update(TestRailProject project, JSONObject json) throws RestApiException {
            RestApiRequest request = api.createRequest("/update_run/" + project.getId());
            request.post(json);
        }

        public TestRailRun close(int id) throws RestApiException {
            RestApiRequest request = api.createRequest("/close_run/" + id);
            JSONObject description = request.post("").toJSONObject();
            return new TestRailRun(description);
        }
    }

    public Results results() {
        return new Results();
    }

    public class Results {

        public void add(TestRailTest test, TestRailStatus status) throws RestApiException {
            add(test, getStatusJson(status));
        }

        public void add(TestRailTest test, JSONObject json) throws RestApiException {
            RestApiRequest request = api.createRequest("/add_result/" + test.getId());
            request.post(json);
        }

        public void addForTests(TestRailRun run, JSONArray json) throws RestApiException {
            RestApiRequest request = api.createRequest("/add_results/" + run.getId());
            request.post(getResultsJson(json));
        }


        public void add(TestRailRun run, TestRailCase testCase, TestRailStatus status) throws RestApiException {
            add(run, testCase, getStatusJson(status));
        }

        public void add(TestRailRun run, TestRailCase testCase, JSONObject json) throws RestApiException {
            RestApiRequest request = api.createRequest("/add_result_for_case/" + run.getId() + "/" + testCase.getId());
            request.post(json);
        }

        public void addForCases(TestRailRun run, JSONArray json) throws RestApiException {
            RestApiRequest request = api.createRequest("/add_results_for_cases/" + run.getId());
            request.post(getResultsJson(json));
        }

        private JSONObject getStatusJson(TestRailStatus status) {
            try {
                return new JSONObject("{\"status_id\":" + status.getId() + "}");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        private JSONObject getResultsJson(JSONArray results) {
            try {
                return new JSONObject().put("results", results);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
