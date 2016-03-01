package com.qantium.uisteps.core.screenshots;

/**
 * Created by SolAN on 25.02.2016.
 */
public class ScreenshotException extends RuntimeException {

    public ScreenshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScreenshotException(String message) {
        super(message);
    }
}
