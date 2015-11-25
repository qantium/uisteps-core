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
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import net.lightbody.bmp.core.har.Har;

/**
 *
 * @author ASolyankin
 */
public class Storage {

    private final static ThreadLocal<Map> threadLocalMap = new ThreadLocal();
    private String dir = UIStepsProperties.getProperty(UIStepsProperty.HOME_DIR);

    public Storage() {
        threadLocalMap.set(new ConcurrentHashMap());
    }

    public Storage toDir(String dir) {
        this.dir = dir;
        return this;
    }

    private Map getMap() {
        return threadLocalMap.get();
    }
    
    public File save(Saved file) {
        return put(file.getFile());
    }

    public File save(Save file, String path) throws IOException {
        Saved savedFile = new Saved(file.toDir(dir).save(path));
        return save(savedFile);
    }
    
     public File save(String data, String path) throws IOException {
        return save(data.getBytes(), path);
    }

    public File save(Har har, String path) throws IOException {
        File file = new File(new File(dir),path);
        Files.createParentDirs(file);
        har.writeTo(file);
        return save(new Saved(new File(path)));
    }
    
    public File save(byte[] bytes, String path) throws IOException {
        File file = new File(new File(dir), path);
        Files.createParentDirs(file);
        Files.write(bytes, file);
        return save(new Saved(new File(path)));
    }
    
    public File save(RenderedImage image, String path) throws IOException {
        File file = new File(new File(dir), path);
        String extension = com.google.common.io.Files.getFileExtension(path);
        Files.createParentDirs(file);
        ImageIO.write(image, extension, file);
        return save(new Saved(new File(path)));
    }

    public File put(File file) {
        return Storage.this.put(file.getPath(), file);
    }

    public <T> T put(String key, T value) {
        getMap().put(key, value);
        return value;
    }

    public <T> T put(T value) {
        return Storage.this.put(value.getClass().getName(), value);
    }

    public <T> T get(String key) {
        return (T) getMap().get(key);
    }

    public <T> T get(Class<T> key) {
        return Storage.this.get(key.getName());
    }

}
