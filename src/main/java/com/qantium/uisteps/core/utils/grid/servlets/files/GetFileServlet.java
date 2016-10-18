package com.qantium.uisteps.core.utils.grid.servlets.files;

import com.qantium.uisteps.core.utils.grid.servlets.GridServlet;
import org.apache.xerces.impl.dv.util.Base64;
import org.openqa.grid.internal.Registry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Created by Anton Solyankin
 */
public class GetFileServlet extends GridServlet {

    public GetFileServlet() {
        this(null);
    }

    public GetFileServlet(Registry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        PrintWriter out = response.getWriter();
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String encoded = Base64.encode(bytes);
        out.print(encoded);
    }
}
