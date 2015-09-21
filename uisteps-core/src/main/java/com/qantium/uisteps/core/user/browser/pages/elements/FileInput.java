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
package com.qantium.uisteps.core.user.browser.pages.elements;

import com.qantium.uisteps.core.user.browser.Browser;
import com.qantium.uisteps.core.user.browser.pages.UIElement;
import org.openqa.selenium.WebElement;

/**
 *
 * @author ASolyankin
 */
public abstract class FileInput extends UIElement {

    private final ru.yandex.qatools.htmlelements.element.FileInput wrappedFileInput;
    
    public FileInput(WebElement wrappedElement) {
        super(wrappedElement);
        wrappedFileInput = new ru.yandex.qatools.htmlelements.element.FileInput(wrappedElement);
    }

    public ru.yandex.qatools.htmlelements.element.FileInput getWrappedFileInput() {
        return wrappedFileInput;
    }

    public Object setFileToUpload(String filePath) {
        inOpenedBrowser().setTo(this, filePath);
        return null;
    }
    
}