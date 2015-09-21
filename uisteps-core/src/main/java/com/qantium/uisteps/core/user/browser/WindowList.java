package com.qantium.uisteps.core.user.browser;

import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ASolyankin
 */
public class WindowList {

    private int currentHandleIndex;
    private final Browser browser;
    private final long timeOutInSeconds;

    public WindowList(Browser browser, long timeOutInSeconds) {
        this.browser = browser;
        this.timeOutInSeconds = timeOutInSeconds;
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
        WebDriver driver = browser.getDriver();
        Set<String> setHandles = driver.getWindowHandles();

        if (setHandles.isEmpty()) {
            throw new NoWindowException();
        }

        if (index < 0 || index >= setHandles.size()) {
            switchToDefaultWindow();
        } else {
            browser.waitUntil(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    Set<String> setHandles = driver.getWindowHandles();
                    Object[] handles = setHandles.toArray();
                    return !handles[handles.length - 1].equals("");
                }
            }, timeOutInSeconds);

            setHandles = driver.getWindowHandles();
            Object[] handles = setHandles.toArray();
            driver.switchTo().window((String) handles[index]);
            currentHandleIndex = index;
        }
    }

    public int getCountOfWindows() {
        return this.browser.getDriver().getWindowHandles().size();
    }
}
