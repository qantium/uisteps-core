package com.qantium.uisteps.core.browser;

import java.util.Set;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author ASolyankin
 */
public class WindowList {

    private int currentHandleIndex;
    private final WebDriver driver;

    public WindowList(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToNextWindow() {
        switchToWindowByIndex(currentHandleIndex + 1);
    }

    public void switchToPreviousWindow() {
        switchToWindowByIndex(currentHandleIndex - 1);
    }

    public void switchToDefaultWindow() {
        switchToWindowByIndex(0);
    }

    public void switchToWindowByIndex(int index) throws NoWindowException {
        Set<String> handles = driver.getWindowHandles();

        if (handles.isEmpty()) {
            throw new NoWindowException();
        }

        if (index < 0 || index >= handles.size()) {
            switchToDefaultWindow();
        } else {
            driver.switchTo().window((String) handles.toArray()[index]);
            currentHandleIndex = index;
        }
    }

    public int getCountOfWindows() {
        return driver.getWindowHandles().size();
    }
}
