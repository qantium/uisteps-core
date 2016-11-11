package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qantium.uisteps.core.utils.testrail.entity.TestRailType.RUN;

/**
 * Created by Anton Solyankin
 */
public class TestRailRun extends TestRailEntity {

    public TestRailRun(int id) {
        super(RUN, id);
    }

    public TestRailRun(JSONObject description) {
        super(RUN, description);
    }
}
