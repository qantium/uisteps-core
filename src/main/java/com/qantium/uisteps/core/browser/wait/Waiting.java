package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.pages.BaseUrl;
import com.qantium.uisteps.core.browser.pages.Page;
import com.qantium.uisteps.core.browser.pages.elements.Button;
import com.qantium.uisteps.core.browser.pages.elements.Link;
import com.qantium.uisteps.core.browser.pages.elements.TextBlock;
import com.qantium.uisteps.core.browser.pages.elements.TextField;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import com.qantium.uisteps.core.user.User;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.qantium.uisteps.core.browser.wait.TimeoutBuilder.defaultTimeouts;

public class Waiting {

    public static void waitFor(Runnable runnable) {
        waitFor(defaultTimeouts(), runnable);
    }


    public static void main(String[] args) throws InterruptedException {
        UIStepsProperty.WEBDRIVER_DRIVER.setValue("chrome");

        User user = new User();

        user.open(P.class).getLoginField().clear();
        //  ...enter("Антон");
        Thread.sleep(2000);
        user.get(P.class).getLoginField().enter("Солянкин");
        user.get(P.class).getPasswordField().enter("Антон");
        user.get(P.class).getLoginButton().click();
        Thread.sleep(5000);
    }

    @BaseUrl(host = "10.1.12.179", port = 9090)
    public static class P extends Page {

        public TextField getLoginField() {
            return get(TextField.class, By.cssSelector("input[name='username']")).withName("Логин");
        }

        public TextField getPasswordField() {
            return get(TextField.class, By.cssSelector("input[name='password']")).withName("Пароль");
        }

        public Button getLoginButton() {
            return get(Button.class, By.cssSelector(".bss-button")).withName("Войти в банк");
        }

        @Name("Поле Капча")
        @FindBy(xpath = ".//*[@name='captchaCode']//..")
        public Link captchaField;

        @FindBy(css = ".a.bss-login-block__link:nth-child(1)")
        public Button passwordRecoveryButton;

        public Button getRegistrationButton() {
            return get(Button.class, By.cssSelector(".a.bss-login-block__link:nth-child(2)")).withName("Зарегистрироваться");
        }

        public TextBlock getCurrencyBlock() {
            return get(TextBlock.class, By.cssSelector(".bss-courses"));
        }

        public Link getPasswordRecoveryLink() {
            return get(Link.class, By.cssSelector("[class = 'a bss-login-block__link'] > span")).withName("Ссылка 'Забыли пароль'");
        }

        @Override
        public boolean isDisplayed() {
            return super.isDisplayed() && getLoginField().isDisplayed() && getPasswordField().isDisplayed();
        }
    }

    public static <T extends WithTimeout> void waitFor(T obj, Consumer<T> consumer) {
        new Waiter<Void>(obj) {
            @Override
            protected Void run() {
                consumer.accept(obj);
                return null;
            }
        }.perform();
    }

    public static void waitFor(WithTimeout timeouts, Runnable runnable) {
        new Waiter<Void>(timeouts) {
            @Override
            protected Void run() {
                runnable.run();
                return null;
            }
        }.perform();
    }

    public static <T> T waitFor(Supplier<T> supplier) {
        return waitFor(defaultTimeouts(), supplier);
    }

    public static <T extends WithTimeout> T waitFor(T obj, Function<T, T> function) {
        return new Waiter<T>(obj) {
            @Override
            protected T run() {
                return function.apply(obj);
            }
        }.perform();
    }

    public static <T> T waitFor(WithTimeout timeouts, Supplier<T> supplier) {
        return new Waiter<T>(timeouts) {
            @Override
            protected T run() {
                return supplier.get();
            }
        }.perform();
    }

    public static Boolean waitUntil(Supplier<Boolean> supplier) {
        return waitUntil(defaultTimeouts(), supplier);
    }

    public static <T extends WithTimeout> Boolean waitUntil(T obj, Function<T, Boolean> function) {
        try {
            return new Waiter<Boolean>(obj) {
                @Override
                protected Boolean run() {
                    return function.apply(obj);
                }
            }.checkResult();
        } catch (Exception ex) {
            return false;
        }
    }

    public static Boolean waitUntil(WithTimeout timeouts, Supplier<Boolean> supplier) {
        try {
            return new Waiter<Boolean>(timeouts) {
                @Override
                protected Boolean run() {
                    return supplier.get();
                }
            }.checkResult();
        } catch (Exception ex) {
            return false;
        }
    }

    public static Boolean waitUntilNot(Supplier<Boolean> supplier) {
        return waitUntilNot(defaultTimeouts(), supplier);
    }

    public static <T extends WithTimeout> Boolean waitUntilNot(T obj, Function<T, Boolean> function) {
        try {
            return new Waiter<Boolean>(obj) {
                @Override
                protected Boolean run() {
                    return !function.apply(obj);
                }
            }.checkResult();
        } catch (Exception ex) {
            return true;
        }
    }

    public static Boolean waitUntilNot(WithTimeout timeouts, Supplier<Boolean> supplier) {
        try {
            return new Waiter<Boolean>(timeouts) {
                @Override
                protected Boolean run() {
                    return !supplier.get();
                }
            }.checkResult();
        } catch (Exception ex) {
            return true;
        }
    }
}
