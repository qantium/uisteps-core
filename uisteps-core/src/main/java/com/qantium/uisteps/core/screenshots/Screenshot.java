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

import com.google.common.io.Files;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.HOME_DIR;

/**
 * @author Anton Solyankin
 */
public class Screenshot {

    private BufferedImage image;
    private String dir = getProperty(HOME_DIR);

    public Screenshot(BufferedImage image) {
        this.image = image;
    }

    public static Screenshot getFrom(InputStream inputStream) {
        try {
            return new Screenshot(ImageIO.read(inputStream));
        } catch (Exception ex) {
            throw new ScreenshotException("Cannot get screenshot from  inputStream: " + inputStream, ex);
        }
    }

    public static Screenshot getFrom(File file) {
        try {
            BufferedImage in = ImageIO.read(file);
            Screenshot sc = new Screenshot(in);
            return sc;
        } catch (Exception ex) {
            throw new ScreenshotException("Cannot get screenshot from: " + file, ex);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public File save(String file) {
        try {
            String extension = Files.getFileExtension(file);
            File screenshot = new File(dir, file);
            Files.createParentDirs(screenshot);
            ImageIO.write(getImage(), extension, screenshot);
            return new File(file);
        } catch (Exception ex) {
            throw new ScreenshotException("Cannot save screenshot to file: " + file, ex);
        }
    }

    public boolean hasDiffFrom(Screenshot screenshot) {
        return getDiffFrom(screenshot).hasDiff();
    }

    public ImageDiff getDiffFrom(Screenshot screenshot) {
        return new ImageDiffer().makeDiff(this.getImage(), screenshot.getImage());
    }

    public Screenshot getDiffImageFrom(Screenshot screenshot) {
        BufferedImage diffImage = getDiffFrom(screenshot).getDiffImage();
        return new Screenshot(diffImage);
    }

    public byte[] asByteArray() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            os.flush();
            return os.toByteArray();
        } catch (Exception ex) {
            throw new ScreenshotException("Cannot get bytes from screenshot: ", ex);
        }
    }

    public Screenshot toDir(String dir) {
        this.dir = dir;
        return this;
    }
}
