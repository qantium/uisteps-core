package com.qantium.uisteps.core.utils.grid.utils;

import com.qantium.net.rest.RestApiException;
import com.qantium.uisteps.core.browser.IBrowser;
import com.qantium.uisteps.core.utils.grid.client.GridClient;
import com.qantium.uisteps.core.utils.grid.client.NodeType;
import com.qantium.uisteps.core.utils.grid.servlets.robot.RobotActions;
import com.qantium.uisteps.core.utils.grid.servlets.robot.RobotActionsBuilder;
import com.qantium.uisteps.core.utils.grid.servlets.robot.RobotServlet;

import java.io.IOException;

/**
 * Created by Anton Solyankin
 */
public class RobotGridUtils {

    public static void run(IBrowser browser, RobotActionsBuilder builder) throws RestApiException, IOException {
        RobotActions actions = builder.build();
        run(browser, actions);
    }

    public static void run(IBrowser browser, RobotActions actions) throws RestApiException, IOException {
        GridClient client = new GridClient(browser, RobotServlet.class);
        client.send(actions);
    }

    public static void run(String host, NodeType nodeType, RobotActionsBuilder builder) throws RestApiException, IOException {
        RobotActions actions = builder.build();
        run(host, nodeType, actions);
    }

    public static void run(String host, NodeType nodeType, RobotActions actions) throws RestApiException, IOException {
        GridClient client = new GridClient(host, nodeType, RobotServlet.class);
        client.send(actions);
    }
}
