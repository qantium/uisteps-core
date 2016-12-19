/*
 * Copyright 2015 ASolyankin.
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
package com.qantium.uisteps.core.storage;

import com.google.common.io.Files;
import com.qantium.uisteps.core.screenshots.Screenshot;
import net.lightbody.bmp.core.har.Har;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import static com.qantium.uisteps.core.properties.UIStepsProperty.STORAGE_DIR;
import static com.qantium.uisteps.core.properties.UIStepsProperty.USER_DIR;

/**
 *
 * @author Anton Solyankin
 */
public class Storage {

    private String dir = USER_DIR.getValue() + STORAGE_DIR.getValue();

    public Storage toDir(String dir) {
        this.dir = dir;
        return this;
    }

    public File save(String data, String path) throws IOException {
        return save(data.getBytes(), path);
    }

    public File save(Har har, String path) throws IOException {
        File file = new File(dir, path);
        Files.createParentDirs(file);
        har.writeTo(file);
        return file;
    }

    public File save(byte[] bytes, String path) throws IOException {
        File file = new File(dir, path);
        Files.createParentDirs(file);
        Files.write(bytes, file);
        return file;
    }

    public File save(Screenshot screenshot, String path) throws IOException {
        return save(screenshot.getImage(), path);
    }

    public File save(RenderedImage image, String path) throws IOException {
        File file = new File(dir, path);
        String extension = com.google.common.io.Files.getFileExtension(path);
        Files.createParentDirs(file);
        ImageIO.write(image, extension, file);
        return file;
    }
}