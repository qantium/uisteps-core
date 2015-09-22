package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.then.Then;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 *
 * @author ASolyankin
 */
public abstract class UIElement extends TypifiedElement implements UIObject {

    public UIElement(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    
    public String getText() {
        return inOpenedBrowser().getTextFrom(this);
    }

    
    public Object click() {
        inOpenedBrowser().click(this);
        return null;
    }

    public Object moveMouseOver() {
        inOpenedBrowser().moveMouseOver(this);
        return null;
    }

    public Object clickOnPoint(int x, int y) {
        inOpenedBrowser().clickOnPoint(this, x, y);
        return null;
    }
    
    protected <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return inOpenedBrowser().then(uiObject);
    }
    
    protected <T> Then<T> then(T value) {
        return inOpenedBrowser().then(value);
    }
    
    protected <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }

    protected <T extends UIObject> T onDisplayed(T uiObject) {
        return inOpenedBrowser().onDisplayed(uiObject);
    }
}
