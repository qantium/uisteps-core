package com.qantium.uisteps.core.utils.grid.servlets.robot;

import com.qantium.uisteps.core.utils.grid.servlets.GridResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class RobotGridResponse extends GridResponse {

    private List<Object> results = new ArrayList();

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    @Override
    public JSONObject toJSON() {
        try {
            return super.toJSON().put("results", getResults().toArray());
        } catch (JSONException ex) {
            throw new RuntimeException("Cannot get JSONObject", ex);
        }
    }
}
