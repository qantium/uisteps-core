package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qantium.uisteps.core.utils.testrail.entity.TestRailType.TEST;

/**
 * Created by Anton Solyankin
 */
public class TestRailTest extends TestRailEntity {

    public TestRailTest(int id) {
        super(TEST, id);
    }

    public TestRailTest(JSONObject description) {
        super(TEST, description);
    }
}
