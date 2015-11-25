/*
 * Copyright 2015 A.Solyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this data except in compliance with the License.
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
import com.qantium.uisteps.core.properties.UIStepsProperties;
import static com.qantium.uisteps.core.properties.UIStepsProperty.SCREENSHOTS_SCALE_HEIGHT;
import static com.qantium.uisteps.core.properties.UIStepsProperty.SCREENSHOTS_SCALE_WIDTH;
import java.io.File;

/**
 *
 * @author A.Solyankin
 */
public class Saved {

    private final File file;

    public Saved(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {

        StringBuilder link = new StringBuilder();
        link
                .append("<a target='blank' href='")
                .append(getFile());

        String extension = Files.getFileExtension(getFile().getPath()).toLowerCase();
        link.append("'>");

        if ("jpg".equals(extension) || "png".equals(extension) || "gif".equals(extension)) {
            String width = UIStepsProperties.getProperty(SCREENSHOTS_SCALE_WIDTH);
            String height = UIStepsProperties.getProperty(SCREENSHOTS_SCALE_HEIGHT);

            link
                    .append("<img class='screenshot' width='").append(width)
                    .append("' height='").append(height)
                    .append("' src='").append(getFile())
                    .append("' href='").append(getFile())
                    .append("'>");
        } else {
            link.append(getFile());
        }

        link
                .append("</a> <a download href='")
                .append(getFile())
                .append("'>")
                .append("[<u>download</u>]")
                .append("</a>");

        return link.toString();
    }

}
