package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.wait.IsNotDisplayedException;
import com.qantium.uisteps.core.browser.wait.Waiting;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/**
 * Provides switching between browser windows
 *
 * @author Anton Solyankin
 */
public class WindowManager implements IWindowManager {

    /**
     * Index of available window
     */
    private int currentWindowIndex = 0;
    private final WebDriver driver;

    public WindowManager(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void openNewWindow() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.open()");
        switchToNextWindow();
    }

    @Override
    public void closeWindow() {
        driver.close();

        if (hasPreviousWindow()) {
            switchToPreviousWindow();
        } else  {
            currentWindowIndex--;
            switchToNextWindow();
        }
    }

    @Override
    public void switchToNextWindow() {
        switchToWindowByIndex(currentWindowIndex + 1);
    }

    @Override
    public void switchToPreviousWindow() {
        switchToWindowByIndex(currentWindowIndex - 1);
    }

    @Override
    public void switchToDefaultWindow() {
        switchToWindowByIndex(0);
    }

    @Override
    public void switchToWindowByIndex(int index) throws NoWindowException, IndexOutOfBoundsException {
        currentWindowIndex = index;

        Set<String> handles = driver.getWindowHandles();
        Waiting.waitFor(() -> !handles.isEmpty() && index >= 0 && index < handles.size());

        if (handles.isEmpty()) {
            throw new NoWindowException();
        }

        if (index < 0 || index >= handles.size()) {
            throw new IndexOutOfBoundsException("Cannot switch to window by index " + 1);
        }

        driver.switchTo().window((String) handles.toArray()[index]);

        if(Waiting.waitUntilNot(() -> (
                (JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"))) {
            throw new IsNotDisplayedException(new Page().withName("Page in opened window"));
        }

    }

    @Override
    public boolean hasNextWindow() {
        return currentWindowIndex < driver.getWindowHandles().size() - 1;
    }

    @Override
    public boolean hasPreviousWindow() {
        return currentWindowIndex > 0;
    }

    @Override
    public int getCountOfWindows() {
        return driver.getWindowHandles().size();
    }

    @Override
    public int getCurrentWindowIndex() {
        return currentWindowIndex;
    }
}
