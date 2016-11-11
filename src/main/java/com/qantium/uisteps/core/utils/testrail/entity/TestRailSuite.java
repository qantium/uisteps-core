package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qantium.uisteps.core.utils.testrail.entity.TestRailType.SUITE;

/**
 * Created by Anton Solyankin
 */
public class TestRailSuite extends TestRailEntity {

    public TestRailSuite(int id) {
        super(SUITE, id);
    }

    public TestRailSuite(JSONObject description) {
        super(SUITE, description);
    }
}
