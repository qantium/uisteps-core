package com.qantium.uisteps.core.utils.testrail;

import com.qantium.uisteps.core.properties.UIStepsProperties;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

import com.qantium.uisteps.core.utils.data.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class TestRailConfig extends Data {

    private final String action;
    private final String host;
    private final String user;
    private final String password;
    private final Set<TestRailEntity> runs = new HashSet();
    private final Map<String, Integer> statusCodes = new HashMap();

    public TestRailConfig() {
        this(
                UIStepsProperties.getProperty(TESTRAIL_HOST),
                UIStepsProperties.getProperty(TESTRAIL_USER),
                UIStepsProperties.getProperty(TESTRAIL_PASSWORD),
                UIStepsProperties.getProperty(TESTRAIL_RUNS),
                UIStepsProperties.getProperty(TESTRAIL_STATUS_CODES),
                UIStepsProperties.getProperty(TESTRAIL_ACTION)
        );
    }

    public TestRailConfig(String host, String user, String password, String runs, String statusCodes, String action) {
        this.host = host;
        this.user = user;
        this.password = password;

        for (String id : runs.split(",")) {
            this.runs.add(new TestRailEntity(id.trim()));
        }

        put("host", host);
        put("user", user);
        put("password", password);
        put("runs", runs);

        for(String status: statusCodes.split(",")) {
            putStatus(status);
        }
        this.action = action;

        if (!isValid()) {
            throw new TestRailException("Config " + this + " is not valid!");
        }
    }

    public boolean actionIsDefined() {
        return !action.toLowerCase().equals(Action.UNDEFINED.name().toLowerCase());
    }

    private void putStatus(String status) {
        try {

            String[] statusCode = status.split(":");
            String key = statusCode[0].trim();
            Integer code = Integer.valueOf(statusCode[1].trim());
            statusCodes.put(key, code);
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            throw new TestRailException("Cannot parce testrail status \"" + status + "\"!\nCause:" + ex);
        }
    }

    public boolean isValid() {
        return !StringUtils.isEmpty(host)
                && !StringUtils.isEmpty(user)
                && !StringUtils.isEmpty(password)
                && !runs.isEmpty()
                && !statusCodes.isEmpty();
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

    public String getAction() {
        return action;
    }

    public Set<TestRailEntity> getRuns() {
        return runs;
    }

    public Integer getStatusCode(String status) {
        String key = status.toLowerCase();

        if(!statusCodes.containsKey(key)) {
            throw new TestRailException("Cannot find code for status \"" + status + "\"!");
        }
        return statusCodes.get(key);
    }
}
