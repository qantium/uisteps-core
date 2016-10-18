package com.qantium.uisteps.core.utils.grid.servlets;

import org.json.JSONObject;

/**
 * Created Anton Solyankin
 */
public class GridResponse {

    private boolean success = true;
    private Exception errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Exception getErrors() {
        return errors;
    }

    public void setErrors(Exception errors) {
        this.errors = errors;
    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }

    public void failed(Exception errors) {
        setSuccess(false);
        setErrors(errors);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
