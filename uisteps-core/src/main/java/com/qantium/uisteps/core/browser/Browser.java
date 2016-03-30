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
package com.qantium.uisteps.core.browser;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.qantium.uisteps.core.browser.actions.Click;
import com.qantium.uisteps.core.browser.actions.EnterInto;
import com.qantium.uisteps.core.browser.context.Context;
import com.qantium.uisteps.core.browser.context.UseContext;
import com.qantium.uisteps.core.browser.pages.*;
import com.qantium.uisteps.core.browser.pages.elements.CheckBox;
import com.qantium.uisteps.core.browser.pages.elements.FileInput;
import com.qantium.uisteps.core.browser.pages.elements.RadioButtonGroup.RadioButton;
import com.qantium.uisteps.core.browser.pages.elements.Select;
import com.qantium.uisteps.core.browser.pages.elements.Select.Option;
import com.qantium.uisteps.core.browser.pages.elements.alert.Alert;
import com.qantium.uisteps.core.browser.pages.elements.alert.AuthenticationAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.ConfirmAlert;
import com.qantium.uisteps.core.browser.pages.elements.alert.PromtAlert;
import com.qantium.uisteps.core.browser.wait.*;
import com.qantium.uisteps.core.name.NameConverter;
import com.qantium.uisteps.core.screenshots.Ignored;
import com.qantium.uisteps.core.screenshots.Photographer;
import com.qantium.uisteps.core.screenshots.Screenshot;
import com.qantium.uisteps.core.then.GetValueAction;
import com.qantium.uisteps.core.then.OnDisplayedAction;
import com.qantium.uisteps.core.then.Then;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.*;

/**
 * @author Anton Solyankin
 */
public class Browser implements SearchContext {

    private WebDriver driver;
    private String name;
    private WindowManager windowManager;
    private LocatorFactory locatorFactory = new LocatorFactory();
    private UrlFactory urlFactory = new UrlFactory();
    private BrowserMobProxyServer proxy;
    private Photographer photographer;


    public Browser() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
        windowManager = new WindowManager(driver);
    }

    public WebElement getWrappedElement(UIElement element) {
        wait(element).untilIsDisplayed();
        return element.getWrappedElement();
    }

    public WindowManager getWindowManager() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get window manager");
        }
        return windowManager;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isAlive() {
        try {
            driver.getWindowHandles().size();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void setUrlFactory(UrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public UrlFactory getUrlFactory() {
        return urlFactory;
    }

    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
    }

    public Photographer getPhotographer() {

        if (driver == null) {
            throw new NullPointerException("Driver must be set to get photographer");
        }

        if (photographer == null) {
            photographer = new Photographer(driver);
        }

        return photographer;
    }

    public LocatorFactory getLocatorFactory() {
        return locatorFactory;
    }

    public void setLocatorFactory(LocatorFactory locatorFactory) {
        this.locatorFactory = locatorFactory;
    }

    public BrowserMobProxyServer getProxy() {
        return proxy;
    }

    public void setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }

        if (proxy != null) {
            proxy.stop();
        }
    }

    public Page openUrl(String url, String... params) {
        try {
            return open(new Url(url), params);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot open url " + url, ex);
        }
    }

    public Page open(Url url, String... params) {
        return open(new Page(), url, params);
    }

    public <T extends Page> T open(Class<T> page, Url url, String... params) {
        return open(instatiate(page), url, params);
    }

    public <T extends Page> T open(T page, Url url, String... params) {
        page.setUrl(url);
        return open(page, params);
    }

    public <T extends Page> T open(Class<T> page, String... params) {
        return open(instatiate(page), params);
    }

    public <T extends Page> T open(T page, String... params) {
        Url url = getUrlFactory().getUrlOf(page, params);
        page.setUrl(url);
        return openPage(page);
    }

    public <T extends Page> T openPage(T page) {
        getDriver().get(page.getUrl().toString());
        init(page, null, null);
        return page;
    }

    //Wait
    public <V> V waitUntil(Function<? super WebDriver, V> isTrue) {
        return new BrowserWait(this).until(isTrue);
    }

    public void waitUntil(Predicate<WebDriver> isTrue) {
        new BrowserWait(this).until(isTrue);
    }

    public UIObjectWait wait(UIObject uiObject) {
        return new UIObjectWait(uiObject);
    }

    public UIElementWait wait(UIElement uiElement) {
        return new UIElementWait(uiElement);
    }

    public UIObjectWait wait(UIObject context, Class<? extends UIObject> uiObject, By by) {
        return wait(init(uiObject, context, by));
    }

    public UIObjectWait wait(UIObject context, Class<? extends UIObject> uiObject) {
        return wait(init(uiObject, context, null));
    }

    public UIObjectWait wait(Class<? extends UIObject> uiObject, By by) {
        return wait(init(uiObject, null, by));
    }

    public UIObjectWait wait(Class<? extends UIObject> uiObject) {
        return wait(init(uiObject, null, null));
    }

    public boolean isDisplayed(UIObject uiObject) {
        try {
            wait(uiObject).untilIsDisplayed();
            return true;
        } catch (DisplayException e) {
            return false;
        }
    }

    public boolean isDisplayed(UIObject context, Class<? extends UIObject> uiObject, By by) {
        try {
            wait(context, uiObject, by).untilIsDisplayed();
            return true;
        } catch (DisplayException e) {
            return false;
        }
    }

    public boolean isDisplayed(UIObject context, Class<? extends UIObject> uiObject) {
        return isDisplayed(context, uiObject, null);
    }

    public boolean isDisplayed(Class<? extends UIObject> uiObject, By by) {
        return isDisplayed(null, uiObject, by);
    }

    public boolean isDisplayed(Class<? extends UIObject> uiObject) {
        return isDisplayed(null, uiObject, null);
    }

    public boolean isNotDisplayed(UIObject context, Class<? extends UIObject> uiObject, By by) {
        try {
            wait(init(uiObject, context, by)).untilIsNotDisplayed();
            return true;
        } catch (NotDisplayException ex) {
            return false;
        }
    }

    public boolean isNotDisplayed(UIObject uiObject) {
        try {
            wait(uiObject).untilIsNotDisplayed();
            return true;
        } catch (NotDisplayException ex) {
            return false;
        }
    }

    public boolean isNotDisplayed(UIObject context, Class<? extends UIObject> uiObject) {
        return isNotDisplayed(context, uiObject, null);
    }

    public boolean isNotDisplayed(Class<? extends UIObject> uiObject, By by) {
        return isNotDisplayed(null, uiObject, by);
    }

    public boolean isNotDisplayed(Class<? extends UIObject> uiObject) {
        return isNotDisplayed(null, uiObject, null);
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getDriver().getTitle();
    }

    public Page getCurrentPage() {

        try {
            return new Page().<Page>withName("page").setUrl(new Url(getCurrentUrl()));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Window
    public void openNewWindow() {
        executeScript("window.open()");
        getWindowManager().switchToNextWindow();
    }

    public void switchToNextWindow() {
        getWindowManager().switchToNextWindow();
    }

    public void switchToPreviousWindow() {
        getWindowManager().switchToPreviousWindow();
    }

    public void switchToDefaultWindow() {
        getWindowManager().switchToDefaultWindow();
    }

    public void switchToWindowByIndex(int index) {
        getWindowManager().switchToWindowByIndex(index);
    }

    //Window position
    public Point getWindowPosition() {
        return getDriver().manage().window().getPosition();
    }

    public void setWindowPosition(int newX, int newY) {
        getDriver().manage().window().setPosition(new Point(newX, newY));
    }

    public void moveWindowBy(int xOffset, int yOffset) {
        getDriver().manage().window().getPosition().moveBy(xOffset, yOffset);

    }

    public void moveWindowTo(int newX, int newY) {
        getDriver().manage().window().getPosition().moveBy(newX, newY);
    }

    public void maximizeWindow() {
        getDriver().manage().window().maximize();
    }

    //Navigation
    public void goBack() {
        getDriver().navigate().back();
    }

    public void goForward() {
        getDriver().navigate().forward();
    }

    //Window size
    public Dimension getWindowSize() {
        return getDriver().manage().window().getSize();
    }

    public void setWindowSize(int width, int height) {
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    public void setWindowWidth(int width) {
        setWindowSize(width, getWindowSize().getHeight());
    }

    public void setWindowHeight(int height) {
        setWindowSize(getWindowSize().getWidth(), height);

    }

    public void setWindowSize(String size) {
        Dimension defaultDimension = getWindowSize();
        int width = defaultDimension.width;
        int height = defaultDimension.height;

        String[] dimension = size.split("x");

        if (dimension.length > 0) {

            if (!StringUtils.isEmpty(size)) {
                width = Integer.parseInt(dimension[0]);
            }
        }

        if (dimension.length > 1) {
            height = Integer.parseInt(dimension[1]);
        }

        setWindowSize(width, height);
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
    }

    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void deleteCookieNamed(String name) {
        getDriver().manage().deleteCookieNamed(name);

    }

    public Set<Cookie> getCookies() {
        return getDriver().manage().getCookies();
    }

    public Actions getActions() {
        return new Actions(getDriver());
    }

    //Elements
    public void click() {
        getActions().click().perform();
    }

    public void clickAndHold() {
        getActions().clickAndHold().perform();
    }

    public void clickAndHold(UIElement element) {
        getActions().clickAndHold(getWrappedElement(element)).perform();
    }

    public void doubleClick() {
        getActions().doubleClick().perform();
    }

    public void doubleClick(UIElement element) {
        getActions().doubleClick(getWrappedElement(element)).perform();
    }

    public void contextClick() {
        getActions().contextClick().perform();
    }

    public void contextClick(UIElement element) {
        getActions().contextClick(getWrappedElement(element)).perform();
    }

    public void releaseMouse() {
        getActions().release().perform();
    }

    public void releaseMouse(UIElement element) {
        getActions().release(getWrappedElement(element)).perform();
    }

    public void dragAndDrop(UIElement source, UIElement target) {
        getActions().dragAndDrop(getWrappedElement(source), getWrappedElement(target)).perform();
    }

    public void dragAndDrop(UIElement element, int xOffset, int yOffset) {
        getActions().dragAndDropBy(getWrappedElement(element), xOffset, yOffset).perform();
    }

    public void keyDown(Keys theKey) {
        getActions().keyDown(theKey).perform();
    }

    public void keyDown(UIElement element, Keys theKey) {
        getActions().keyDown(getWrappedElement(element), theKey).perform();
    }

    public void keyUp(Keys theKey) {
        getActions().keyUp(theKey).perform();
    }

    public void keyUp(UIElement element, Keys theKey) {
        getActions().keyUp(getWrappedElement(element), theKey).perform();
    }

    public void click(UIElement element) {
        new Click(this, element).execute();
    }



    private void sleep(long pollingTime) {
        try {
            Thread.sleep(pollingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickOnPoint(UIElement element, int x, int y) {
        getActions().moveToElement(getWrappedElement(element), x, y).click().build().perform();
    }

    public void moveMouseByOffset(int xOffset, int yOffset) {
        getActions().moveByOffset(xOffset, yOffset).perform();
    }

    public void moveToElement(UIElement element, int xOffset, int yOffset) {
        getActions().moveToElement(getWrappedElement(element), xOffset, yOffset).perform();
    }

    public void moveMouseOver(UIElement element) {
        getActions().moveToElement(getWrappedElement(element)).perform();
    }

    public void typeInto(UIElement input, Object text) {
        getWrappedElement(input).sendKeys(text.toString());
    }

    public void clear(UIElement input) {
        getWrappedElement(input).clear();
    }

    public void enterInto(UIElement input, Object text) {
        new EnterInto(this, input, text).execute();
    }

    //Tags
    public String getTagNameOf(UIElement element) {
        return getWrappedElement(element).getTagName();
    }

    public String getAttribute(UIElement element, String attribute) {
        return getWrappedElement(element).getAttribute(attribute);
    }

    public String getCSSPropertyOf(UIElement element, String cssProperty) {
        return getWrappedElement(element).getCssValue(cssProperty);
    }

    public Point getPositionOf(UIElement element) {
        return getWrappedElement(element).getLocation();
    }

    public Point getMiddlePositionOf(UIElement element) {
        Point position = getPositionOf(element);
        Dimension size = getSizeOf(element);

        int x = position.x + size.width / 2;
        int y = position.y + size.height / 2;

        return new Point(x, y);
    }

    public Point getRelativePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getPositionOf(element);
        Point targetPosition = getPositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Point getRelativeMiddlePositionOf(UIElement element, UIElement target) {
        Point elementPosition = getMiddlePositionOf(element);
        Point targetPosition = getMiddlePositionOf(target);

        int x = elementPosition.x - targetPosition.x;
        int y = elementPosition.y - targetPosition.y;

        return new Point(x, y);
    }

    public Dimension getSizeOf(UIElement element) {
        return getWrappedElement(element).getSize();
    }

    public String getTextFrom(UIElement input) {
        WebElement inputWrappedElement = getWrappedElement(input);

        if ("input".equals(getWrappedElement(input).getTagName())) {
            String enteredText = inputWrappedElement.getAttribute("value");

            if (enteredText != null) {
                return enteredText;
            } else {
                return "";
            }
        } else {
            return inputWrappedElement.getText();
        }
    }

    public <T extends UIObject> Then<T> then(Class<T> uiObject) {
        return new Then(new OnDisplayedAction<>(this, uiObject));
    }

    public <T> Then<T> then(T value) {
        return new Then(new GetValueAction<>(value));
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args);
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder browserName = new StringBuilder();

        String os = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        Dimension size = getWindowSize();

        browserName.append(NameConverter.humanize(getDriver().getClass()).replace(" driver", ""))
                .append(" os.name: ").append(os)
                .append(" os.arch: ").append(osArch)
                .append(" os.version: ").append(osVersion)
                .append(" width: ").append(size.width)
                .append(" height: ").append(size.height);

        if (!StringUtils.isEmpty(name)) {
            browserName.append(" name: ").append(name);
        }

        return browserName.toString();
    }

    //Select
    public void select(Option option) {
        option.select();
    }

    public void deselectAllValuesFrom(Select select) {
        select.getWrappedSelect().deselectAll();
    }

    public void deselect(Option option) {
        option.deselect();
    }

    //Radio button
    public void select(RadioButton button) {
        if (!button.isSelected()) {
            getWrappedElement(button).click();
        }
    }

    //CheckBox
    public void select(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().select();
    }

    public void deselect(CheckBox checkBox) {
        checkBox.getWrappedCheckBox().deselect();
    }

    //Scrooll window
    public void scrollWindowByOffset(int x, int y) {
        executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    public void scrollWindowToTarget(UIElement element) {
        executeScript("arguments[0].scrollIntoView();", getWrappedElement(element));
    }

    public void scrollWindowToTargetByOffset(UIElement element, int x, int y) {
        WebElement target = getWrappedElement(element);
        Point location = target.getLocation();

        int xLocation = location.x + x;
        int yLocation = location.y + y;
        String script = "window.scroll(arguments[0],arguments[1]);";

        executeScript(script, xLocation, yLocation);
    }

    //Scroll
    public void scrollToTarget(UIElement scroll, UIElement target) {
        Point scrollPosition = getPositionOf(scroll);
        Point targetPosition = getPositionOf(target);
        scroll(scroll, new Point(targetPosition.x - scrollPosition.x, targetPosition.y - scrollPosition.y));
    }

    public void verticalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        verticalScroll(scroll, targetPosition.y - scrollPosition.y);
    }

    public void horizontalScrollToTarget(UIElement scroll, UIElement target) {
        Point targetPosition = getPositionOf(target);
        Point scrollPosition = getPositionOf(scroll);
        horizontalScroll(scroll, targetPosition.x - scrollPosition.x);
    }

    public void horizontalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(pixels, position.y));
    }

    public void verticalScroll(UIElement scroll, int pixels) {
        Point position = getPositionOf(scroll);
        scroll(scroll, new Point(position.x, pixels));
    }

    public void scroll(UIElement scroll, int x, int y) {
        getActions()
                .clickAndHold(getWrappedElement(scroll))
                .moveByOffset(x, y)
                .release()
                .perform();
    }

    protected void scroll(UIElement scroll, Point point) {
        getActions()
                .clickAndHold(getWrappedElement(scroll))
                .moveByOffset(point.x, point.y)
                .release()
                .perform();
    }

    //FileInput
    public void setFileToUpload(FileInput fileInput, String filePath) {
        fileInput.getWrappedFileInput().setFileToUpload(filePath);
    }

    public <T extends UIObject> T instatiate(Class<T> uiObject) {

        try {
            return ConstructorUtils.invokeConstructor(uiObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Cannot instantiate " + uiObject, ex);
        }
    }

    //Alert
    public Alert getDisplayedAlert() {
        return init(Alert.class, null, null);
    }

    public ConfirmAlert getDisplayedConfirmAlert() {
        return init(ConfirmAlert.class, null, null);
    }

    public PromtAlert getDisplayedPromtAlert() {
        return init(PromtAlert.class, null, null);
    }

    public void accept(Alert alert) {
        alert.getWrappedAlert().accept();
    }

    public void dismiss(ConfirmAlert cofirm) {
        cofirm.getWrappedAlert().dismiss();
    }

    public PromtAlert enterInto(PromtAlert promt, String text) {
        promt.getWrappedAlert().sendKeys(text);
        return promt;
    }

    public void authenticateUsing(AuthenticationAlert authenticationAlert, String login, String password) {
        Credentials credentials = new UserAndPassword(login, password);
        authenticationAlert.getWrappedAlert().authenticateUsing(credentials);
    }

    public <T extends UIObject> T init(Class<T> uiObject, UIObject context, By locator) {
        return init(uiObject, context, locator, true);
    }

    public <T extends UIObject> T initWithoutAfterInitialization(Class<T> uiObject, UIObject context, By locator) {
        return init(uiObject, context, locator, false);
    }

    private <T extends UIObject> T init(Class<T> uiObject, UIObject context, By locator, boolean afterInitialization) {
        return init(instatiate(uiObject), context, locator, afterInitialization);
    }

    public <T extends UIObject> T init(T uiObject, UIObject context, By locator) {
        return init(uiObject, context, locator, true);
    }

    public <T extends UIObject> T initWithoutAfterInitialization(T uiObject, UIObject context, By locator) {
        return init(uiObject, context, locator, false);
    }

    private <T extends UIObject> T init(T uiObject, UIObject context, By locator, boolean afterInitialization) {

        uiObject.setBrowser(this);

        if (uiObject instanceof UIElement) {
            UIElement uiElement = (UIElement) uiObject;

            if (context == null && uiObject.getClass().isAnnotationPresent(Context.class)) {
                context = getContext(uiObject.getClass().getAnnotation(Context.class));
            }

            if (locator == null) {
                locator = getLocator(uiElement);
            }

            uiElement.setContext(context);
            uiElement.setLocator(locator);
        }

        if (!uiObject.getClass().isAnnotationPresent(NotInit.class)) {

            try {

                for (Field field : getUIObjectFields(uiObject)) {

                    if (field.get(uiObject) == null) {
                        UIElement uiElement = instatiate((Class<UIElement>) field.getType());

                        uiElement.setName(NameConverter.humanize(field));
                        field.set(uiObject, uiElement);
                        UIObject fieldContext = uiObject;

                        if (field.isAnnotationPresent(Context.class)) {
                            fieldContext = getContext(field.getAnnotation(Context.class));
                        } else if (field.isAnnotationPresent(UseContext.class)) {

                            if (field.getClass().isAnnotationPresent(Context.class)) {
                                fieldContext = getContext(field.getClass().getAnnotation(Context.class));
                            } else {
                                throw new RuntimeException("Context is not set for " + field);
                            }
                        }

                        initWithoutAfterInitialization(uiElement, fieldContext, getLocatorFactory().getLocator(field));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (afterInitialization) {
            uiObject.afterInitialization();
        }
        return uiObject;
    }

    private By getLocator(UIElement uiElement) {
        try {
            return getLocatorFactory().getLocator(uiElement);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private UIObject getContext(Context context) {
        Class<? extends UIObject> uiObject = context.value();
        By contextLocator = null;

        if (UIElement.class.isAssignableFrom(uiObject)) {
            contextLocator = getContextLocator(context);
        }

        return init(uiObject, null, contextLocator);
    }

    private By getContextLocator(Context context) {
        try {
            return getLocatorFactory().getLocator(context.findBy());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private List<Field> getUIObjectFields(UIObject uiObject) throws IllegalArgumentException, IllegalAccessException {
        return getUIObjectFields(uiObject.getClass(), new ArrayList());
    }

    private List<Field> getUIObjectFields(Class<?> uiObjectType, List<Field> fields) throws IllegalArgumentException, IllegalAccessException {

        if (uiObjectType.isAnnotationPresent(NotInit.class)) {
            return fields;
        }

        for (Field field : uiObjectType.getDeclaredFields()) {

            if (!field.isAnnotationPresent(NotInit.class) && UIObject.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }

        return getUIObjectFields(uiObjectType.getSuperclass(), fields);
    }

    //find
    public UIElement find(By locator) {
        return find(UIElement.class, locator);
    }

    public <T extends UIElement> T find(Class<T> uiObject) {
        return find(uiObject, null);
    }

    public <T extends UIElement> T find(Class<T> uiObject, By locator) {
        return init(instatiate(uiObject), null, locator);
    }

    public <T extends UIElement> T find(UIObject context, Class<T> uiObject) {
        return find(context, uiObject, null);
    }

    public <T extends UIElement> T find(UIObject context, Class<T> uiObject, By locator) {
        return init(instatiate(uiObject), context, locator);
    }

    //find all
    public UIElements findAll(By locator) {
        return findAll(UIElement.class, locator);
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject) {
        return findAll(uiObject, getLocatorFactory().getLocator(uiObject));
    }

    public <T extends UIElement> UIElements<T> findAll(Class<T> uiObject, By locator) {
        return findAll(null, uiObject, locator);
    }

    public <T extends UIElement> UIElements<T> findAll(UIObject context, Class<T> uiObject) {
        return findAll(context, uiObject, null);
    }

    public <T extends UIElement> UIElements<T> findAll(UIObject context, Class<T> uiObject, By locator) {
        UIElements<T> uiElements = new UIElements(uiObject);
        return init(uiElements, context, locator);
    }

    //onDisplayed
    public <T extends UIObject> T onDisplayed(T uiObject) {
        if(this.equals(uiObject.inOpenedBrowser())) {
            return uiObject;
        }
        return init(uiObject, null, null);
    }

    public <T extends UIElement> T onDisplayed(T uiObject, UIObject context) {
        return onDisplayed(init(uiObject, context, null));
    }

    public <T extends UIObject> T onDisplayed(Class<T> uiObject) {
        return onDisplayed(init(uiObject, null, null));
    }

    public <T extends UIElement> T onDisplayed(Class<T> uiObject, By by) {
        return onDisplayed(init(uiObject, null, by));
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject) {
        return onDisplayed(init(uiObject, context, null));
    }

    public <T extends UIElement> T onDisplayed(UIObject context, Class<T> uiObject, By by) {
        return onDisplayed(init(uiObject, context, by));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject) {
        return onDisplayed(findAll(uiObject));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(Class<T> uiObject, By by) {
        return onDisplayed(findAll(uiObject, by));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject) {
        return onDisplayed(findAll(context, uiObject));
    }

    public <T extends UIElement> UIElements<T> onDisplayedAll(UIObject context, Class<T> uiObject, By by) {
        return onDisplayed(findAll(context, uiObject, by));
    }

    //Screenshots
    public Photographer inScreenshotIgnoring(By... locators) {
        return getPhotographer().ignore(locators);
    }

    public Photographer inScreenshotIgnoring(UIElement... elements) {
        return getPhotographer().ignore(elements);
    }

    public Photographer inScreenshotIgnoring(Coords... areas) {
        return getPhotographer().ignore(areas);
    }

    public Screenshot takeScreenshot() {
        return getPhotographer().takeScreenshot();
    }

    public Screenshot takeScreenshot(UIElement... elements) {
        return getPhotographer().takeScreenshot(elements);
    }

    public Screenshot takeScreenshot(Ignored... elements) {
        return getPhotographer().takeScreenshot(elements);
    }

    //Page source
    public String getPageSource() {
        try {
            return getDriver().getPageSource();
        } catch (Exception ex) {
            if ("true".equals(getProperty(SOURCE_TAKE_FAKE).toLowerCase())) {
                return "CANNOT TAKE PAGE SOURCE!";
            } else {
                throw ex;
            }

        }

    }

    @Override
    public List<WebElement> findElements(By by) {
        return getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getDriver().findElement(by);
    }
}
