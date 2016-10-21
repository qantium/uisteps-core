package com.qantium.uisteps.core.utils.grid.servlets;

import com.qantium.uisteps.core.utils.grid.client.Callback;
import com.qantium.uisteps.core.utils.grid.client.GridClient;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
public class GridServlet extends RegistryBasedServlet {

    public GridServlet() {
        this(null);
    }

    public GridServlet(Registry registry) {
        super(registry);
    }

    protected GridClient getGridClient(HttpServletRequest request) {
        Callback callback = getCallback(request);
        if(callback != null) {
            return new GridClient(callback);
        } else {
            return null;
        }
    }

    protected String getRequestString(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonRequest = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonRequest.append(line);
        }
        return jsonRequest.toString();
    }

    protected Callback getCallback(HttpServletRequest request) {
        String callbackDescription = request.getHeader("Callback");
        if(isEmpty(callbackDescription)) {
            return null;
        } else {
            return Callback.getCallback(callbackDescription);
        }
    }
}