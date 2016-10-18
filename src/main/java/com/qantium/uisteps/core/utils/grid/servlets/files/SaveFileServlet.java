package com.qantium.uisteps.core.utils.grid.servlets.files;

import com.google.common.io.Files;
import com.qantium.uisteps.core.utils.grid.servlets.GridResponse;
import com.qantium.uisteps.core.utils.grid.servlets.GridServlet;
import com.qantium.uisteps.core.utils.grid.servlets.robot.RobotGridResponse;
import org.apache.xerces.impl.dv.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.internal.Registry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Anton Solyankin
 */
public class SaveFileServlet extends GridServlet {

    public SaveFileServlet() {
        this(null);
    }

    public SaveFileServlet(Registry registry) {
        super(registry);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GridResponse griResponse = new RobotGridResponse();
        ;

        try {
            String requestString = getRequestString(request);
            JSONObject json = new JSONObject(requestString);

            String toPath = json.getString("toPath");
            String content = json.getString("file");
            File file = new File(toPath);
            Files.write(Base64.decode(content), file);
        } catch (JSONException ex) {
            griResponse.failed(ex);
            ex.printStackTrace();
        } finally {
            PrintWriter out = response.getWriter();
            out.print(griResponse);
        }
    }
}
