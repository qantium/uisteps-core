package com.qantium.uisteps.core.utils.grid.servlets;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created Anton Solyankin
 */
public class GridResponse {

    private boolean success = true;
    private Exception error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                    .put("success", isSuccess())
                    .put("error", getError());
        } catch (JSONException ex) {
            throw new RuntimeException("Cannot get JSONObject", ex);
        }
    }

    public void failed(Exception errors) {
        setSuccess(false);
        setError(errors);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
