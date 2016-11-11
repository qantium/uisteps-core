package com.qantium.uisteps.core.utils.testrail.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anton Solyankin
 */


public class TestRailEntity {

    private final TestRailType type;
    private final int id;
    private JSONObject description;

    public TestRailEntity(TestRailType type, JSONObject description) {
        try {
            id = description.getInt("id");
            this.description = description;
            this.type = type;
        } catch (JSONException ex) {
           throw new RuntimeException("Cannot create TestRail entity of type " + type + " with description: " + description, ex);
        }
    }

    public TestRailEntity(String fullId) {
        id = Integer.parseInt(fullId.substring(1));
        type = TestRailType.get(fullId.substring(0, 1));
    }

    public TestRailEntity(TestRailType type, int id) {
        this.type = type;
        this.id = id;
    }

    public TestRailType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public JSONObject getDescription() {
        if (description != null) {
            return description;
        } else {
            throw new IllegalStateException("Description for testrail " + type.name().toLowerCase() + " " + this + " is not set");
        }
    }

    @Override
    public String toString() {
        return type.mark + id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestRailEntity that = (TestRailEntity) o;

        if (getId() != that.getId()) return false;
        return getType() == that.getType();

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getId();
        return result;
    }
}
