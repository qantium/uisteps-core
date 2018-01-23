package com.qantium.uisteps.core.browser;

import com.qantium.uisteps.core.browser.pages.AbstractUIObject;
import com.qantium.uisteps.core.browser.pages.UIElement;
import com.qantium.uisteps.core.browser.pages.elements.*;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.browser.pages.elements.alert.AuthenticationAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.ConfirmAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.PromtAlert;
import com.qantium.uisteps.core.browser.wait.Waiting;
import com.qantium.uisteps.core.name.Named;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.qantium.uisteps.core.properties.UIStepsProperty.NULL_VALUE;

/**
 * Created by Anton Solyankin
 */
public interface IBrowser extends DriverActions, SearchContext, Named {

    BrowserMobProxyServer getProxy();

    void setProxy(BrowserMobProxyServer proxy);

    String getHub();

    void setHub(String hub);

    default boolean isAlive() {
        try {
            getDriver().getWindowHandles().size();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    void close();

    URL getNodeUrl();

    URL getHubUrl();

    @Override
    default List<WebElement> findElements(By locator) {
        return getDriver().findElements(locator);
    }

    @Override
    default WebElement findElement(By locator) {
        return getDriver().findElement(locator);
    }

    //UIElement
    default void click(UIElement element) {
        Waiting.waitFor(element, () -> element.getWrappedElement().click());
    }

    default void clickAndHold(UIElement element) {
        Waiting.waitFor(element, () -> new Actions(getDriver())
                .clickAndHold(element.getWrappedElement())
                .perform());
    }

    default void clickOnPoint(UIElement element, int x, int y) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).moveToElement(element.getWrappedElement(), x, y).click().perform());
    }

    default void doubleClick(UIElement element) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).doubleClick(element.getWrappedElement()).perform());
    }

    default void contextClick(UIElement element) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).contextClick(element.getWrappedElement()).perform());
    }

    default void releaseMouse(UIElement element) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).release(element.getWrappedElement()).perform());
    }

    default void dragAndDrop(UIElement source, UIElement target) {
        Waiting.waitFor(source, () -> new Actions(getDriver()).dragAndDrop(source.getWrappedElement(), target.getWrappedElement()).perform());
    }

    default void dragAndDrop(UIElement element, int xOffset, int yOffset) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).dragAndDropBy(element.getWrappedElement(), xOffset, yOffset).perform());
    }

    default void keyDown(UIElement element, Keys theKey) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).keyDown(element.getWrappedElement(), theKey).perform());
    }

    default void keyUp(UIElement element, Keys theKey) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).keyUp(element.getWrappedElement(), theKey).perform());
    }

    default void keyPress(UIElement element, Keys theKey) {
        keyDown(element, theKey);
        keyUp(element, theKey);
    }

    default void moveMouseOver(UIElement element, int xOffset, int yOffset) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).moveToElement(element.getWrappedElement(), xOffset, yOffset).perform());
    }

    default void moveMouseOver(UIElement element) {
        Waiting.waitFor(element, () -> new Actions(getDriver()).moveToElement(element.getWrappedElement()).perform());
    }

    default void typeInto(TextField input, Object text) {
        Waiting.waitFor(input, () -> {
            WebElement webElement = input.getWrappedElement();
            String keys = text == null ? NULL_VALUE.getValue() : text.toString();

            if (!NULL_VALUE.getValue().equals(keys)) {
                webElement.sendKeys(keys);
            }
        });
    }

    default void sendKeys(UIElement element, CharSequence... keysToSend) {
        Waiting.waitFor(element, () -> {
            CharSequence[] keys = keysToSend == null ? new CharSequence[0] : keysToSend;
            if (ArrayUtils.isNotEmpty(keys)) {
                WebElement webElement = element.getWrappedElement();
                webElement.sendKeys(keys);
            }
        });
    }

    default void clear(TextField input) {
        Waiting.waitFor(input, () -> input.getWrappedElement().clear());
    }

    default void enterInto(TextField input, Object text) {
        if (text != null && !NULL_VALUE.getValue().equals(text.toString())) {
            clear(input);
        }
        typeInto(input, text);
    }

    //Tags
    default String getTagNameOf(UIElement element) {
        return Waiting.waitFor(element, () -> element.getWrappedElement().getTagName());
    }

    default String getAttribute(UIElement element, String attribute) {
        return Waiting.waitFor(element, () -> {
            WebElement wrappedElement = element.getWrappedElement();
            return wrappedElement.getAttribute(attribute);
        });
    }

    default String getCSSPropertyOf(UIElement element, String cssProperty) {
        return Waiting.waitFor(element, () -> {
            WebElement wrappedElement = element.getWrappedElement();
            return wrappedElement.getCssValue(cssProperty);
        });
    }

    default String getHtmlOf(UIElement element) {
        return getAttribute(element, "innerHtml");
    }

    default Point getPositionOf(UIElement element) {
        return Waiting.waitFor(element, () -> element.getWrappedElement().getLocation());
    }

    default Point getMiddlePositionOf(UIElement element) {
        return Waiting.waitFor(element, () -> {
            Point position = getPositionOf(element);
            Dimension size = getSizeOf(element);

            int x = position.x + size.width / 2;
            int y = position.y + size.height / 2;

            return new Point(x, y);
        });
    }

    default Point getRelativePositionOf(UIElement element, UIElement target) {
        return Waiting.waitFor(element, () -> {
            Point elementPosition = getPositionOf(element);
            Point targetPosition = getPositionOf(target);

            int x = elementPosition.x - targetPosition.x;
            int y = elementPosition.y - targetPosition.y;

            return new Point(x, y);
        });
    }

    default Point getRelativeMiddlePositionOf(UIElement element, UIElement target) {
        return Waiting.waitFor(element, () -> {
            Point elementPosition = getMiddlePositionOf(element);
            Point targetPosition = getMiddlePositionOf(target);

            int x = elementPosition.x - targetPosition.x;
            int y = elementPosition.y - targetPosition.y;

            return new Point(x, y);
        });
    }

    default Dimension getSizeOf(UIElement element) {
        return Waiting.waitFor(element, () -> element.getWrappedElement().getSize());
    }

    default String getTextFrom(AbstractUIObject uiObject) {
        return Waiting.waitFor(uiObject, () -> {
            if (uiObject instanceof WrapsElement) {
                WebElement wrappedElement = ((WrapsElement) uiObject).getWrappedElement();
                if ("input".equals(wrappedElement.getTagName())) {
                    String enteredText = wrappedElement.getAttribute("value");
                    return enteredText == null ? "" : enteredText;
                } else {
                    return wrappedElement.getText();
                }
            } else {
                return uiObject.getText();
            }
        });
    }

    default boolean isSelected(UIElement element) {
        return element.getWrappedElement().isSelected();
    }

    default boolean isEnabled(UIElement element) {
        return element.getWrappedElement().isEnabled();
    }

    default boolean isNotSelected(UIElement element) {
        return !isSelected(element);
    }

    default boolean isNotEnabled(UIElement element) {
        return !isEnabled(element);
    }

    //Select
    default Group<TextBlock> getSelectedOptions(Select select) {
        return Waiting.waitFor(select, () -> {
            List<TextBlock> elements = select.stream()
                    .filter(option -> isSelected(option)).collect(Collectors.toList());
            return select.getGroup(elements);
        });
    }

    default Group<TextBlock> getNotSelectedOptions(Select select) {
        return Waiting.waitFor(select, () -> {
            List<TextBlock> elements = select.stream()
                    .filter(option -> isNotSelected(option)).collect(Collectors.toList());
            return select.getGroup(elements);
        });
    }

    default void selectFirstByVisibleValue(Select select, String... values) {
        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getText()))
                        .findFirst().ifPresent(option -> {
                    if (isSelected(option)) option.click();
                });
            return null;
        });
    }

    default void selectAllByVisibleValue(Select select, String... values) {
        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getText())
                                && isNotSelected(option))
                        .forEach(option -> option.click());
            return null;
        });
    }

    default void selectFirstByValue(Select select, String... values) {

        Waiting.waitFor(select, () -> {
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
        Waiting.waitFor(select, () -> {
            Arrays.asList(values).stream().forEach(value -> select.stream()
                    .filter(option -> value.equals(option.getAttribute("value"))
                            && isNotSelected(option))
                    .forEach(option -> option.click()));
            return null;
        });
    }

    default void selectAll(Select select) {
        Waiting.waitFor(select, () -> {
            select.stream().filter(option -> isNotSelected(option))
                    .forEach(option -> option.click());
            return null;
        });
    }

    default void deselectAll(Select select) {
        Waiting.waitFor(select, () ->
                select.stream().filter(option -> isSelected(option))
                        .forEach(option -> option.click())
        );
    }

    default void selectByIndex(Select select, Integer... indexes) {
        Waiting.waitFor(select, () -> {
            for (int index : indexes) {
                TextBlock option = select.get(index);
                if (isNotSelected(option)) option.click();
            }
        });
    }

    default void deselectByIndex(Select select, Integer... indexes) {
        Waiting.waitFor(select, () -> {
            for (int index : indexes) {
                TextBlock option = select.get(index);
                if (isSelected(option)) option.click();
            }
        });
    }

    default void deselectFirstByVisibleValue(Select select, String... values) {
        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getText()))
                        .findFirst().ifPresent(option -> {
                    if (isNotSelected(option)) option.click();
                });
        });
    }

    default void deselectAllByVisibleValue(Select select, String... values) {
        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getText()) && isSelected(option))
                        .forEach(option -> option.click());
        });
    }

    default void deselectFirstByValue(Select select, String... values) {

        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getAttribute("value")))
                        .findFirst().ifPresent(option -> {
                    if (isNotSelected(option)) option.click();
                });
        });
    }

    default void deselectAllByValue(Select select, String... values) {
        Waiting.waitFor(select, () -> {
            for (String value : values)
                select.stream()
                        .filter(option -> value.equals(option.getAttribute("value"))
                                && isSelected(option))
                        .forEach(option -> option.click());
        });
    }

    default boolean isMultiple(Select select) {
        return Waiting.waitFor(select, () -> {
            String value = getAttribute(select, "multiple");
            return value != null && !"false".equals(value);
        });
    }

    //CheckBox
    default boolean select(CheckBox checkBox) {
        return Waiting.waitFor(checkBox, () -> {
            if (isSelected(checkBox)) {
                return false;
            } else {
                WebElement wrappedElement = checkBox.getWrappedElement();
                wrappedElement.click();
                return true;
            }
        });
    }

    default boolean deselect(CheckBox checkBox) {
        return Waiting.waitFor(checkBox, () -> {
            if (isSelected(checkBox)) {
                WebElement wrappedElement = checkBox.getWrappedElement();
                wrappedElement.click();
                return true;
            } else {
                return false;
            }
        });
    }

    default boolean select(CheckBox checkBox, boolean select) {
        return Waiting.waitFor(checkBox, () -> {
            if (select) {
                return select(checkBox);
            } else {
                return deselect(checkBox);
            }
        });
    }

    //Scroll window
    default void scrollWindowByOffset(int x, int y) {
        executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    default void scrollWindowToTarget(UIElement element) {
        Waiting.waitFor(element, () -> {
            WebElement wrappedElement = element.getWrappedElement();
            return element.inOpenedBrowser().executeScript("arguments[0].scrollIntoView();", wrappedElement);
        });
    }

    default void scrollWindowToTargetByOffset(UIElement element, int x, int y) {
        Waiting.waitFor(element, () -> {
            WebElement target = element.getWrappedElement();
            Point location = target.getLocation();

            int xLocation = location.x + x;
            int yLocation = location.y + y;
            String script = "window.scroll(arguments[0],arguments[1]);";

            executeScript(script, xLocation, yLocation);
        });
    }

    //Scroll
    default void scrollToTarget(UIElement scroll, UIElement target) {
        Point scrollPosition = getPositionOf(scroll);
        Point targetPosition = getPositionOf(target);
        int targetX = targetPosition.x - scrollPosition.x;
        int targetY = targetPosition.y - scrollPosition.y;
        scroll(scroll, targetX, targetY);
    }

    default void verticalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        verticalScroll(scroll, targetPosition.y - scrollPosition.y);
    }

    default void horizontalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        horizontalScroll(scroll, targetPosition.x - scrollPosition.x);
    }

    default void horizontalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, pixels, position.y);
    }

    default void verticalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, position.x, pixels);
    }

    default void scroll(UIElement scroll, int x, int y) {
        Waiting.waitFor(scroll, () -> new Actions(getDriver())
                .clickAndHold(scroll.getWrappedElement())
                .moveByOffset(x, y)
                .release()
                .perform());
    }

    //FileInput
    default void setFileToUpload(FileInput fileInput, String filePath) {
        Waiting.waitFor(fileInput, () -> fileInput.getWrappedFileInput().setFileToUpload(filePath));
    }

    //Alert
    default void accept(Alert alert) {
        Waiting.waitFor(alert, () -> {
            alert.getWrappedAlert().accept();
        });
    }

    default void dismiss(ConfirmAlert confirm) {
        Waiting.waitFor(confirm, () -> {
            confirm.getWrappedAlert().dismiss();
        });
    }

    default PromtAlert enterInto(PromtAlert promt, String text) {
        return Waiting.waitFor(promt, () -> {
            promt.getWrappedAlert().sendKeys(text);
            return promt;
        });
    }

    default void authenticateUsing(AuthenticationAlert authenticationAlert, String login, String password) {
        Waiting.waitFor(authenticationAlert, () -> {
            Credentials credentials = new UserAndPassword(login, password);
            authenticationAlert.getWrappedAlert().authenticateUsing(credentials);
        });
    }
}