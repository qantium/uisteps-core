package com.qantium.uisteps.core.utils.grid.client;

import com.qantium.net.rest.RestApi;
import com.qantium.net.rest.RestApiRequest;
import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.utils.grid.servlets.GridServlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anton Solyankin
 */
public class GridClient {

    private final String url;
    private String responseEncoding = "UTF-8";
    private Callback callback;

    public GridClient(Callback callback) {
        this(callback.getUrl().toString(), callback.getNodeType(), callback.getServlet());
    }

    public GridClient(IBrowser browser, Class<? extends GridServlet> servlet) {
        this(browser.getNodeUrl().toString(), NodeType.NODE, servlet);
    }


    public GridClient(String host, NodeType nodeType, Class<? extends GridServlet> servlet) {
        this(host, nodeType, servlet.getSimpleName());
    }

    public GridClient(String host, NodeType nodeType, String servlet) {
        this(host + "/" + nodeType + "/" + servlet);
    }

    public GridClient(String url) {
        this.url = url;
    }

    public static GridClient getWithSelfCallback(IBrowser browser, Class<? extends GridServlet> servlet) {
        Callback callback = new Callback(NodeType.HUB, servlet.getSimpleName(), browser.getHubUrl());
        return new GridClient(browser, servlet).with(callback);
    }

    public GridClient with(Callback callback) {
        this.callback = callback;
        return this;
    }

    public String send(Serializable object) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        if (callback != null) {
            connection.setRequestProperty("Callback", callback.toString());
        }

        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readStreamToString(connection.getInputStream(), responseEncoding);
        } else {
            throw new IllegalStateException("Response code is " + responseCode + "!");
        }
    }

    public RestApiRequest createRequest() {
        return createRequest("");
    }

    public RestApiRequest createRequest(String path) {
        return new RestApi(url.toString()).createRequest(path);
    }

    private String readStreamToString(InputStream in, String encoding) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, encoding);
        int c;

        while ((c = reader.read()) != -1) {
            builder.append((char) c);
        }

        return builder.toString();
    }

    public GridClient setResponseEncoding(String responseEncoding) {
        this.responseEncoding = responseEncoding;
        return this;
    }
}
