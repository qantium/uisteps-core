package com.qantium.uisteps.core.browser;

import java.util.Set;
import org.openqa.selenium.WebDriver;

/**
 * Provides switching between browser windows
 *
 * @author ASolyankin
 */
public class WindowManager {

    /**
     * Index of available window
     */
    private int currentWindowIndex;
    private final WebDriver driver;

    public WindowManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Switch to next available window
     *
     * @throws NoWindowException if no opened windows
     * @throws IndexOutOfBoundsException if index less than 0 or more count of opened windows
     * 
     * @see WindowManager#switchToWindowByIndex(int index)
     */
    public void switchToNextWindow() {
        switchToWindowByIndex(currentWindowIndex + 1);
    }
    
    /**
     * Switch to previous available window
     *
     * @throws NoWindowException if no opened windows
     * @throws IndexOutOfBoundsException if index less than 0 or more count of opened windows
     * 
     * @see WindowManager#switchToWindowByIndex(int index)
     */
    public void switchToPreviousWindow() {
        switchToWindowByIndex(currentWindowIndex - 1);
    }
    
    /**
     * Switch to window by index 0
     *
     * @throws NoWindowException if no opened windows
     * 
     * @see WindowManager#switchToWindowByIndex(int index)
     */
    public void switchToDefaultWindow() {
        switchToWindowByIndex(0);
    }

    /**
     * Switch to window by index
     * 
     * @param index index of window to switch to
     * @throws NoWindowException if no opened windows
     * @throws IndexOutOfBoundsException if index less than 0 or more count of opened windows  
     */
    public void switchToWindowByIndex(int index) throws NoWindowException, IndexOutOfBoundsException {
        Set<String> handles = driver.getWindowHandles();

        if (handles.isEmpty()) {
            throw new NoWindowException();
        }

        if (index < 0 || index >= handles.size()) {
            throw new IndexOutOfBoundsException("Cannot switch to window by index " + 1);
        }

        driver.switchTo().window((String) handles.toArray()[index]);
        currentWindowIndex = index;
    }

    /**
     * @return true if it is possible to switch to next window
     */
    public boolean hasNextWindow() {
        return currentWindowIndex < driver.getWindowHandles().size() - 1;
    }

    /**
     * @return true if it is possible to switch to previous window
     */
    public boolean hasPreviousWindow() {
        return currentWindowIndex > 0;
    }

    /**
     * @return count of opened windows
     */
    public int getCountOfWindows() {
        return driver.getWindowHandles().size();
    }

    /**
     * @return index of available window
     */
    public int getCurrentWindowIndex() {
        return currentWindowIndex;
    }
}
