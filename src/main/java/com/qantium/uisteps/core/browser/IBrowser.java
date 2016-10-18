package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.*;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.browser.pages.elements.alert.AuthenticationAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.ConfirmAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.PromtAlert;
import com.qantium.uisteps.core.name.Named;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;

import java.net.URL;

/**
 * Created by Anton Solyankin
 */
public interface IBrowser extends BrowserActions, SearchContext, Named {

    public BrowserMobProxyServer getProxy();

    public void setProxy(BrowserMobProxyServer proxy);

    public String getHub();

    public void setHub(String hub);

    boolean isAlive();

    void close();

    URL getNodeUrl();

    URL getHubUrl();

    //UIElement
    void clickAndHold(UIElement element);

    void doubleClick(UIElement element);

    void contextClick(UIElement element);

    void releaseMouse(UIElement element);

    void dragAndDrop(UIElement source, UIElement target);

    void dragAndDrop(UIElement element, int xOffset, int yOffset);

    void keyDown(UIElement element, Keys theKey);

    void keyUp(UIElement element, Keys theKey);

    void click(UIElement element);

    void clickOnPoint(UIElement element, int x, int y);

    void moveToElement(UIElement element, int xOffset, int yOffset);

    void moveMouseOver(UIElement element);

    void typeInto(TextField input, Object text);

    void clear(TextField input);

     void enterInto(TextField input, Object text);

    String getTagNameOf(UIElement element);

    String getAttribute(UIElement element, String attribute);

    String getCSSPropertyOf(UIElement element, String cssProperty);

    Point getPositionOf(UIElement element);

    Point getMiddlePositionOf(UIElement element);

    Point getRelativePositionOf(UIElement element, UIElement target);

    Point getRelativeMiddlePositionOf(UIElement element, UIElement target);

    Dimension getSizeOf(UIElement element);

    String getTextFrom(UIElement input);

    //Select
    void select(Select.Option option);

    void deselectAllValuesFrom(Select select);

    void deselect(Select.Option option);

    //Radio button
    boolean select(RadioButton button);

    //CheckBox
    boolean select(CheckBox checkBox);

    boolean deselect(CheckBox checkBox);

    boolean select(CheckBox checkBox, boolean select);

    //Scroll window
    void scrollWindowByOffset(int x, int y);

    void scrollWindowToTarget(UIElement element);

    void scrollWindowToTargetByOffset(UIElement element, int x, int y);

    //Scroll
    void scrollToTarget(UIElement scroll, UIElement target);

    void verticalScrollToTarget(UIElement scroll, UIElement target);

    void horizontalScrollToTarget(UIElement scroll, UIElement target);

    void horizontalScroll(UIElement scroll, int pixels);

    void verticalScroll(UIElement scroll, int pixels);

    void scroll(UIElement scroll, int x, int y);

    //FileInput
    void setFileToUpload(FileInput fileInput, String filePath);

    //Alert
    void accept(Alert alert);

    void dismiss(ConfirmAlert confirm);

    PromtAlert enterInto(PromtAlert promt, String text);

    void authenticateUsing(AuthenticationAlert authenticationAlert, String login, String password);

}
