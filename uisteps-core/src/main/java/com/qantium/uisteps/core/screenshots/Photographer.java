/*
 * Copyright 2015 A.Solyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qantium.uisteps.core.screenshots;

import com.qantium.uisteps.core.browser.AlertException;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.UIElements;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.coordinates.CoordsProvider;
import ru.yandex.qatools.ashot.cropper.ImageCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.SCREENSHOTS_TAKE_FAKE;

/**
 * @author A.Solyankin
 */
public class Photographer {

    private final AShot aShot = new AShot();
    private final WebDriver driver;

    public Photographer(WebDriver driver) {
        this.driver = driver;
    }

    public AShot getAShot() {
        return aShot;
    }

    public WebDriver getDriver() {
        return driver;
    }

    //settings
    protected Photographer setCoordsProvider(CoordsProvider coordsProvider) {
        getAShot().coordsProvider(coordsProvider);
        return this;
    }

    protected Photographer setImageCropper(ImageCropper cropper) {
        getAShot().imageCropper(cropper);
        return this;
    }

    public Photographer shootingStrategy(ShootingStrategy strategy) {
        getAShot().shootingStrategy(strategy);
        return this;
    }

    //ignore
    public Photographer ignore(By... locators) {
        Set<By> selectors = new HashSet();
        selectors.addAll(Arrays.asList(locators));
        getAShot().ignoredElements(selectors);
        return this;
    }

    public Photographer ignore(UIElement... elements) {
        List<Coords> coords = new ArrayList();

        for (UIElement element : elements) {
            Point position = element.getPosition();
            Dimension size = element.getSize();
            coords.add(new Coords(position.x, position.y, size.width, size.height));
        }

        return ignore(coords.toArray(new Coords[elements.length]));
    }

    public Photographer ignore(Coords... areas) {
        Set<Coords> coords = new HashSet();
        coords.addAll(Arrays.asList(areas));
        getAShot().ignoredAreas(coords);
        return this;
    }

    public Photographer ignore(int width, int height) {
        return ignore(new Coords(width, height));
    }

    public Photographer ignore(int x, int y, int width, int height) {
        return ignore(new Coords(x, y, width, height));
    }


    //take screenshot
    public Screenshot takeScreenshot() {
        try {
            try {
                BufferedImage image = getAShot().shootingStrategy(new ScreenshotStrategy()).takeScreenshot(getDriver()).getImage();
                return new Screenshot(image);
            } catch (UnhandledAlertException ex) {
                return new Screenshot(new Robot().createScreenCapture(new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
            }
        } catch (Exception ex) {
            if ("true".equals(getProperty(SCREENSHOTS_TAKE_FAKE).toLowerCase())) {
                InputStream resource = getClass().getResourceAsStream("/fake_screenshot.png");
                return Screenshot.getFrom(resource);
            } else {
                throw new ScreenshotException(ex.getMessage());
            }
        }
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        List<WebElement> webElements = new ArrayList();

        for (UIElement element : elements) {

            if (element instanceof UIElements) {

                UIElements uiElements = (UIElements) element;

                Iterator iterator = uiElements.getElements().iterator();
                while (iterator.hasNext()) {
                    UIElement uiElement = (UIElement) iterator.next();
                    webElements.add(uiElement.getWrappedElement());
                }

            } else {
                webElements.add(element.getWrappedElement());
            }
        }
        BufferedImage image = getAShot().takeScreenshot(getDriver(), webElements).getImage();
        return new Screenshot(image);
    }

    public Screenshot takeScreenshot(Ignored... elements) {

        for (Ignored ignored : elements) {

            switch (ignored.elements) {
                case LOCATORS:
                    ignore((By[]) ignored.getLocators().toArray());
                    break;
                case ELEMENTS:
                    ignore((UIElement[]) ignored.getElements().toArray());
                    break;
                case AREAS:
                    ignore((Coords[]) ignored.getAreas().toArray());
                    break;
            }
        }
        return takeScreenshot();
    }
}
