package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONObject;

import static com.qantium.uisteps.core.utils.testrail.entity.TestRailType.CASE;

/**
 * Created by Anton Solyankin
 */
public class TestRailCase extends TestRailEntity {

    public TestRailCase(int id) {
        super(CASE, id);
    }

    public TestRailCase(JSONObject description) {
        super(CASE, description);
    }
}
