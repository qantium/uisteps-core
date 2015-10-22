/*
 * Copyright 2014 ASolyankin.
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
package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.name.NameConvertor;
import com.qantium.uisteps.core.then.Then;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 *
 * @author ASolyankin
 */
public class UIBlock extends HtmlElement implements UIBlockOrElement {

    public UIBlock() {
    }

    public UIBlock(By locator) {
        setWrappedElement(findElement(locator));
    }

    @Override
    public void click() {
        inOpenedBrowser().click(this);
    }

    public Object afterClick() {
        inOpenedBrowser().click(this);
        return this;
    }

    public Object moveMouseOver() {
        inOpenedBrowser().moveMouseOver(this);
        return this;
    }

    public Object clickOnPoint(int x, int y) {
        inOpenedBrowser().clickOnPoint(this, x, y);
        return this;
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }

    public <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }

    public void switchToNextWindow() {
        inOpenedBrowser().switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        inOpenedBrowser().switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        inOpenedBrowser().switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        inOpenedBrowser().switchToWindowByIndex(index);
    }

    @Override
    public String getName() {

        if (StringUtils.isEmpty(super.getName())) {
            setName(NameConvertor.humanize(getClass()));
        }

        return super.getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        if(Page.class.isAssignableFrom(uiObject)) { 
             return inOpenedBrowser().onDisplayed(uiObject);
        } else {
            return (T) inOpenedBrowser().onDisplayed((Class<UIBlockOrElement>) uiObject, this);
        }
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, by, this);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayed(uiObject, name, this);
    }

    public <T extends UIBlockOrElement> T onDisplayed(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayed(uiObject, name, by, this);
    }

    public <T extends UIBlockOrElement> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(List<T> proxyElements) {
        return inOpenedBrowser().onDisplayedAll(proxyElements);
    }
    
    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayedAll(uiObject, this);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, by, this);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name, this);
    }

    public <T extends UIBlockOrElement> UIElements<T> onDisplayedAll(Class<T> uiObject, String name, By by) {
        return inOpenedBrowser().onDisplayedAll(uiObject, name, by, this);
    }

    @Override
    public SearchContext getSearchContext() {
        return getWrappedElement();
    }
}
