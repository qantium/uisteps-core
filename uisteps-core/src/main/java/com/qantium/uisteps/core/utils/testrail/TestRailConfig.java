package com.qantium.uisteps.core.utils.testrail;

import com.qantium.uisteps.core.properties.UIStepsProperties;

import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

import com.qantium.uisteps.core.utils.data.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton Solyankin
 */
public class TestRailConfig extends Data {

    private final String host;
    private final String user;
    private final String password;
    private final Set<TestRailEntity> runs = new HashSet();

    public TestRailConfig(String host, String user, String password, String runs) {
        this.host = host;
        this.user = user;
        this.password = password;

        for (String id : runs.split(",")) {
            this.runs.add(new TestRailEntity(id));
        }

        put("host", host);
        put("user", user);
        put("password", password);
        put("runs", runs);

        if (!isValid()) {
            throw new TestRailException("Config " + this + " is not valid!");
        }
    }

    public TestRailConfig() {

        this(
                UIStepsProperties.getProperty(TESTRAIL_HOST),
                UIStepsProperties.getProperty(TESTRAIL_USER),
                UIStepsProperties.getProperty(TESTRAIL_PASSWORD),
                UIStepsProperties.getProperty(TESTRAIL_RUNS)
        );
    }

    public boolean isValid() {
        return !StringUtils.isEmpty(host)
                && !StringUtils.isEmpty(user)
                && !StringUtils.isEmpty(password)
                && !runs.isEmpty();
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

    public Set<TestRailEntity> getRuns() {
        return runs;
    }
}
