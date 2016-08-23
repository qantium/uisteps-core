package com.qantium.uisteps.core.screenshots;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.shooting.ImageReadException;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Anton Solyankin
 */
public class ScreenshotStrategy extends SimpleShootingStrategy {

    @Override
    public BufferedImage getScreenshot(WebDriver driver) {
            ByteArrayInputStream imageArrayStream = null;
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;

            BufferedImage bufferedImage;
            try {
                imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
                bufferedImage = ImageIO.read(imageArrayStream);
            } catch (IOException ex) {
                throw new ImageReadException("Can not parse screenshot data", ex);
            } finally {
                IOUtils.closeQuietly(imageArrayStream);
            }

            return bufferedImage;
    }

}
