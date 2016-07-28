package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.factory.Driver;
import com.qantium.uisteps.core.browser.factory.DriverBuilder;
import org.openqa.selenium.WebDriver;

/**
 * Created by Anton Solyankin
 */
public interface IBrowserManager {

    void closeAllBrowsers();

    void closeCurrentBrowser();

    IBrowser getCurrentBrowser();

    IBrowser openNewBrowser();

    IBrowser openNewBrowser(WebDriver driver);

    IBrowser openNewBrowser(Driver driver);

    IBrowser openNewBrowser(String driver);

    IBrowser openNewBrowser(DriverBuilder driverBuilder);

    IBrowser open(IBrowser browser);

    IBrowser switchToNextBrowser();

    IBrowser switchToPreviousBrowser();

    IBrowser switchToFirstBrowser();

    IBrowser switchToLastBrowser();

    IBrowser switchToBrowserByIndex(int index);

    boolean hasNextBrowser();

    boolean hasPreviousBrowser();

    boolean hasAnyBrowser();
}
