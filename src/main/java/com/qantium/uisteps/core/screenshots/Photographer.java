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

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.UIElements;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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

import static com.qantium.uisteps.core.properties.UIStepsProperty.SCREENSHOTS_TAKE_FAKE;

/**
 * @author Anton Solyankin
 */
public class Photographer implements IPhotographer {

    private final AShot aShot = new AShot();
    private final WebDriver driver;

    public Photographer(WebDriver driver) {
        this.driver = driver;
    }

    //settings
    protected Photographer setCoordinatesProvider(CoordsProvider coordsProvider) {
        aShot.coordsProvider(coordsProvider);
        return this;
    }

    protected Photographer setImageCropper(ImageCropper cropper) {
        aShot.imageCropper(cropper);
        return this;
    }

    protected Photographer shootingStrategy(ShootingStrategy strategy) {
        aShot.shootingStrategy(strategy);
        return this;
    }

    //ignore
    public Photographer ignore(By... locators) {
        Set<By> selectors = new HashSet();
        selectors.addAll(Arrays.asList(locators));
        aShot.ignoredElements(selectors);
        return this;
    }

    @Override
    public Photographer ignore(UIElement... elements) {
        List<Coords> coordinates = new ArrayList();

        for (UIElement element : elements) {
            Point position = element.getPosition();
            Dimension size = element.getSize();
            coordinates.add(new Coords(position.x, position.y, size.width, size.height));
        }

        return ignore(coordinates.toArray(new Coords[elements.length]));
    }

    @Override
    public Photographer ignore(Coords... areas) {
        Set<Coords> coordinates = new HashSet();
        coordinates.addAll(Arrays.asList(areas));
        aShot.ignoredAreas(coordinates);
        return this;
    }

    @Override
    public Photographer ignore(int width, int height) {
        return ignore(new Coords(width, height));
    }

    @Override
    public Photographer ignore(int x, int y, int width, int height) {
        return ignore(new Coords(x, y, width, height));
    }


    @Override
    public Screenshot takeScreenshot() {
        try {
            try {
                BufferedImage image = aShot.shootingStrategy(new ScreenshotStrategy()).takeScreenshot(driver).getImage();
                return new Screenshot(image);
            } catch (UnhandledAlertException ex) {
                return new Screenshot(new Robot().createScreenCapture(new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
            }
        } catch (Exception ex) {
            if (SCREENSHOTS_TAKE_FAKE.isTrue()) {
                InputStream resource = getClass().getResourceAsStream("/fake_screenshot.png");
                return Screenshot.getFrom(resource);
            } else {
                throw new ScreenshotException(ex.getMessage());
            }
        }
    }

    @Override
    public Screenshot takeScreenshot(UIElement... elements) {
        List<WebElement> webElements = new ArrayList();

        for (UIElement element : elements) {

            if (element instanceof UIElements) {

                UIElements uiElements = (UIElements) element;

                Iterator iterator = uiElements.iterator();
                while (iterator.hasNext()) {
                    UIElement uiElement = (UIElement) iterator.next();
                    webElements.add(uiElement.getWrappedElement());
                }

            } else {
                webElements.add(element.getWrappedElement());
            }
        }
        BufferedImage image = aShot.takeScreenshot(driver, webElements).getImage();
        return new Screenshot(image);
    }

    @Override
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
