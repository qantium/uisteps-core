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
package com.qantium.uisteps.core.proxy;

import com.google.common.io.Files;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import com.qantium.uisteps.core.storage.Save;
import java.io.File;
import java.io.IOException;
import net.lightbody.bmp.core.har.Har;

/**
 *
 * @author A.Solyankin
 */
public class HarFile implements Save{
    
    private final Har har;
    private String dir = UIStepsProperties.getProperty(UIStepsProperty.HOME_DIR);

    public HarFile(Har har) {
        this.har = har;
    }

    public Har getHar() {
        return har;
    }

    public File save(String file) throws IOException {
        File homeDir = new File(dir);
        File harFile = new File(homeDir, file);
        Files.createParentDirs(harFile);
        getHar().writeTo(harFile);
        return new File(file);
    }

    @Override
    public HarFile toDir(String dir) {
        this.dir = dir;
        return this;
    }
}
