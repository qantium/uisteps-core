package com.qantium.uisteps.core.browser;

import org.openqa.selenium.JavascriptExecutor;

/**
 * Created by Anton Solyankin
 */
public interface ScriptExecutor {

    Object executeAsyncScript(String script, Object... args);

    Object executeScript(String script, Object... args);
}
