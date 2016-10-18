package com.qantium.uisteps.core.utils.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solan on 27.09.2016.
 */
public class Command {

    private final List<String> stdoutLog = new ArrayList();
    private final List<String> stdoutError = new ArrayList();
    private final String cmd;

    public Command(String cmd) {
        this.cmd = cmd;
    }

    public void run() throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);

        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = stdout.readLine()) != null) {
            stdoutLog.add(line);
        }
        stdout.close();

        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            stdoutError.add(line);
        }
        stderr.close();
    }

    public List<String> getStdoutLog() {
        return stdoutLog;
    }

    public List<String> getStdoutError() {
        return stdoutError;
    }
}
