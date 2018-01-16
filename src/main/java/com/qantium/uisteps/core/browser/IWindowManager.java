package com.qantium.uisteps.core.browser;

/**
 * Created Anton Solyankin
 */
public interface IWindowManager {

    void openNewWindow();

    void closeWindow();

    void switchToNextWindow();

    void switchToPreviousWindow();

    void switchToDefaultWindow();

    void switchToWindowByIndex(int index);

    boolean hasNextWindow();

    boolean hasPreviousWindow();

    int getCountOfWindows();

    int getCurrentWindowIndex();

    String getCurrentWindowHash();
}
