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
package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.data.DataContainer;
import com.qantium.uisteps.core.data.DataFillable;

/**
 * @author ASolyankin
 */
@NotInit
public class FileInput extends UIElement implements DataContainer, DataFillable {

    public ru.yandex.qatools.htmlelements.element.FileInput getWrappedFileInput() {
        return new ru.yandex.qatools.htmlelements.element.FileInput(getWrappedElement());
    }

    public Object setFileToUpload(String filePath) {
        if (filePath == null) {
            throw new NullPointerException("Cannot set file with null path to " + this);
        }
        inOpenedBrowser().setFileToUpload(this, filePath);
        return null;
    }


    @Override
    public String getData() {
        return getText();
    }

    @Override
    public void setData(Object rawData) {
        setFileToUpload(rawData.toString());
    }
}
