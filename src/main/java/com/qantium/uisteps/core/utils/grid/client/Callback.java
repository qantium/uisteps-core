package com.qantium.uisteps.core.utils.grid.client;

import com.qantium.uisteps.core.utils.grid.servlets.GridServlet;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anton Solyankin
 */
public class Callback {

    private final NodeType nodeType;
    private final String servlet;
    private final URL url;

    public Callback(NodeType nodeType, Class<? extends GridServlet> servlet, URL url) {
        this(nodeType, servlet.getSimpleName(), url);
    }

    public Callback(NodeType nodeType, String servlet, URL url) {
        this.nodeType = nodeType;
        this.servlet = servlet;
        this.url = url;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getServlet() {
        return servlet;
    }

    public URL getUrl() {
        return url;
    }

    public static Callback getCallback(String description) {
        try {
            String[] callback = description.split(";");
            return new Callback(NodeType.NODE.valueOf(callback[1].toUpperCase()), callback[2], new URL(callback[0]));
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot get callback by description: " + description, ex);
        }
    }

    @Override
    public String toString() {
        return  url + ";" + nodeType + ";" + servlet;
    }
}
