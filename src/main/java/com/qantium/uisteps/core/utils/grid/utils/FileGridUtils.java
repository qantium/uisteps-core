package com.qantium.uisteps.core.utils.grid.utils;

import com.google.common.io.Files;
import com.qantium.net.rest.RestApiException;
import com.qantium.net.rest.RestApiResponse;
import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.utils.grid.client.GridClient;
import com.qantium.uisteps.core.utils.grid.client.NodeType;
import com.qantium.uisteps.core.utils.grid.servlets.files.GetFileServlet;
import org.webbitserver.helpers.Base64;


import java.io.File;
import java.io.IOException;

/**
 * Created by Anton Solyankin
 */
public class FileGridUtils {

    public static File saveFile(String host, NodeType nodeType, String fromPath, String toPath) throws RestApiException, IOException {
        String content = getFileContent(host, nodeType, fromPath);
        return saveFile(content, toPath);
    }

    public static File saveFileFromNode(IBrowser browser, String fromPath, String toPath) throws RestApiException, IOException {
        String content = getFileContentFromNode(browser, fromPath);
        return saveFile(content, toPath);
    }

    public static String getFileContentFromNode(IBrowser browser, String path) throws RestApiException {
        return getFileContent(browser, path);
    }

    public static String getFileContent(IBrowser browser, String path) throws RestApiException {
        GridClient client = new GridClient(browser, GetFileServlet.class);
        RestApiResponse response = client.createRequest("?path=" + path).get();
        return response.toString();
    }

    public static String getFileContent(String host, NodeType nodeType, String path) throws RestApiException {
        GridClient client = new GridClient(host, nodeType, GetFileServlet.class);
        RestApiResponse response = client.createRequest("?path=" + path).get();
        return response.toString();
    }

    public static File saveFileOnNode(String host, NodeType nodeType, String fromPath, String toPath) throws RestApiException, IOException {
        String content = getFileContent(host, nodeType, fromPath);
        return saveFile(content, toPath);
    }

    public static File saveFileOnNode(IBrowser browser, String fromPath, String toPath) throws RestApiException, IOException {
        String content = getFileContentFromNode(browser, fromPath);
        return saveFile(content, toPath);
    }

    private static File saveFile(String content, String toPath) throws IOException{
        File file = new File(toPath);
        Files.write(Base64.decode(content), file);
        return file;
    }

}
