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
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

/**
 *
 * @author A.Solyankin
 */
public class Screenshot {

    private final BufferedImage image;
    private File dir = new File(UIStepsProperties.getProperty(UIStepsProperty.SCREENSHOTS_TO_DIR));

    public Screenshot(BufferedImage image) {
        this.image = image;
    }

    public static Screenshot getFrom(File file) throws IOException {
        return new Screenshot(ImageIO.read(file));
    }

    public <T extends Screenshot> T toDir(String dir) {
        this.dir = new File(dir);
        return (T) this;
    }

    public File getDir() {
        return dir;
    }

    public BufferedImage getImage() {
        return image;
    }

    public File save(String file) throws IOException {

        if (!dir.exists()) {
            dir.mkdir();
        }
        String extension = Files.getFileExtension(file);
        File screenshot = new File(dir, file);
        ImageIO.write(image, extension, screenshot);
        return screenshot;
    }

    public ImageDiff getDiffFrom(Screenshot screenshot) {
        return new ImageDiffer().makeDiff(this.getImage(), screenshot.getImage());
    }
}
