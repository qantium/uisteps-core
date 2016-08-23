package com.qantium.uisteps.core.screenshots;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.coordinates.Coords;

/**
 * Created by Anton Solyankin
 */
public interface IPhotographer {

    IPhotographer ignore(By... locators);

    IPhotographer ignore(UIElement... elements);

    IPhotographer ignore(Coords... areas);

    IPhotographer ignore(int width, int height);

    IPhotographer ignore(int x, int y, int width, int height);

    Screenshot takeScreenshot();

    Screenshot takeScreenshot(UIElement... elements);

    Screenshot takeScreenshot(Ignored... elements);
}
