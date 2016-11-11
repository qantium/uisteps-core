package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qantium.uisteps.core.utils.testrail.entity.TestRailType.PROJECT;

/**
 * Created by Anton Solyankin
 */
public class TestRailProject extends TestRailEntity {

    public TestRailProject(int id) {
        super(PROJECT, id);
    }

    public TestRailProject(JSONObject description) {
        super(PROJECT, description);
    }
}