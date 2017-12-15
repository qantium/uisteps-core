package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.*;
import com.qantium.uisteps.core.browser.pages.elements.actions.ActionExecutor;
import com.qantium.uisteps.core.browser.pages.elements.actions.BrowserActions;
import com.qantium.uisteps.core.browser.pages.elements.actions.UIElementActions;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anton Solyankin
 */
public interface IBrowser extends DriverActions, SearchContext, Named {

    BrowserMobProxyServer getProxy();

    void setProxy(BrowserMobProxyServer proxy);

    String getHub();

    void setHub(String hub);

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

    default void keyPress(UIElement element, Keys theKey) {
        keyDown(element, theKey);
        keyUp(element, theKey);
    }

    void keyDown(Keys theKey);

    void keyUp(Keys theKey);

    default void keyPress(Keys theKey) {
        keyDown(theKey);
        keyUp(theKey);
    }

    void click(UIElement element);

    void clickOnPoint(UIElement element, int x, int y);

    void moveMouseOver(UIElement element, int xOffset, int yOffset);

    void moveMouseOver(UIElement element);

    void typeInto(TextField input, Object text);

    void sendKeys(UIElement element, CharSequence... keysToSend);

    void clear(TextField input);

    void enterInto(TextField input, Object text);

    String getTagNameOf(UIElement element);

    String getAttribute(UIElement element, String attribute);

    String getCSSPropertyOf(UIElement element, String cssProperty);

    String getHtmlOf(UIElement element);

    Point getPositionOf(UIElement element);

    Point getMiddlePositionOf(UIElement element);

    Point getRelativePositionOf(UIElement element, UIElement target);

    Point getRelativeMiddlePositionOf(UIElement element, UIElement target);

    Dimension getSizeOf(UIElement element);

    String getTextFrom(AbstractUIObject uiObject);

    default boolean isSelected(UIElement element) {
        return perform(element, () -> element.getWrappedElement().isSelected());
    }

    default boolean isEnabled(UIElement element) {
        return perform(element, () -> element.getWrappedElement().isEnabled());
    }

    default boolean isNotSelected(UIElement element) {
        return perform(element, () -> !element.getWrappedElement().isSelected());
    }

    default boolean isNotEnabled(UIElement element) {
        return perform(element, () -> !element.getWrappedElement().isEnabled());
    }

    //Select
    default Group<TextBlock> getSelectedOptions(Select select) {
        return perform(select, () -> {
            List<TextBlock> elements = select.stream()
                    .filter(option -> isSelected(option)).collect(Collectors.toList());
            return select.getGroup(elements);
        });
    }

    default Group<TextBlock> getNotSelectedOptions(Select select) {
        return perform(select, () -> {
            List<TextBlock> elements = select.stream()
                    .filter(option -> isNotSelected(option)).collect(Collectors.toList());
            return select.getGroup(elements);
        });
    }

    default void selectFirstByVisibleValue(Select select, String... values) {
        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getContent()))
                        .findFirst().ifPresent(option -> {
                    if (isSelected(option)) option.click();
                });
            return null;
        });
    }

    default void selectAllByVisibleValue(Select select, String... values) {
        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getContent())
                                && isNotSelected(option))
                        .forEach(option -> option.click());
            return null;
        });
    }

    default void selectFirstByValue(Select select, String... values) {

        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getAttribute("value")))
                        .findFirst().ifPresent(option -> {
                    if (isSelected(option)) option.click();
                });
            return null;
        });
    }

    default void selectAllByValue(Select select, String... values) {
        perform(select, () -> {
            Arrays.asList(values).stream().forEach(value -> select.stream()
                    .filter(option -> value.equals(option.getAttribute("value"))
                            && isNotSelected(option))
                    .forEach(option -> option.click()));
            return null;
        });
    }

    default void selectAll(Select select) {
        perform(select, () -> {
            select.stream().filter(option -> isNotSelected(option))
                    .forEach(option -> option.click());
            return null;
        });
    }

    default void deselectAll(Select select) {
        perform(select, () -> {
            select.stream().filter(option -> isSelected(option))
                    .forEach(option -> option.click());
            return null;
        });
    }

    default void selectByIndex(Select select, Integer... indexes) {
        perform(select, () -> {
            for (int index : indexes) {
                TextBlock option = select.get(index);
                if (isNotSelected(option)) option.click();
            }
            return null;
        });
    }

    default void deselectByIndex(Select select, Integer... indexes) {
        perform(select, () -> {
            for (int index : indexes) {
                TextBlock option = select.get(index);
                if (isSelected(option)) option.click();
            }
            return null;
        });
    }

    default void deselectFirstByVisibleValue(Select select, String... values) {
        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getContent()))
                        .findFirst().ifPresent(option -> {
                    if (isNotSelected(option)) option.click();
                });
            return null;
        });
    }

    default void deselectAllByVisibleValue(Select select, String... values) {
        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getContent()) && isSelected(option))
                        .forEach(option -> option.click());
            return null;
        });
    }

    default void deselectFirstByValue(Select select, String... values) {

        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getAttribute("value")))
                        .findFirst().ifPresent(option -> {
                    if (isNotSelected(option)) option.click();
                });
            return null;
        });
    }

    default void deselectAllByValue(Select select, String... values) {
        perform(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getAttribute("value"))
                                && isSelected(option))
                        .forEach(option -> option.click());
            return null;
        });
    }

    default boolean isMultiple(Select select) {
        return perform(select, () -> {
            String value = getAttribute(select, "multiple");
            return value != null && !"false".equals(value);
        });
    }

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

    default void perform(BrowserActions actions) {
        actions.perform(this);
    }

    default void perform(UIElement element, UIElementActions actions) {
        actions.perform(element);
    }

    default <T> T perform(AbstractUIObject element, ActionExecutor<T> action) {
        return action.perform(element);
    }
}