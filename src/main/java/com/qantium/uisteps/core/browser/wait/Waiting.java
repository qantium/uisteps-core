package com.qantium.uisteps.core.browser.wait;

import java.util.function.Supplier;

import static com.qantium.uisteps.core.browser.wait.TimeoutBuilder.defaultTimeouts;

public class Waiting {

    public static void wait(Runnable runnable) {
        wait(defaultTimeouts(), runnable);
    }

    public static void wait(WithTimeout timeouts, Runnable runnable) {
        new Waiter<Void>(timeouts) {
            @Override
            protected Void run() {
                runnable.run();
                return null;
            }
        }.perform();
    }

    public static <T> T wait(Supplier<T> supplier) {
        return wait(defaultTimeouts(), supplier);
    }

    public static <T> T wait(WithTimeout timeouts, Supplier<T> supplier) {
        return new Waiter<T>(timeouts) {
            @Override
            protected T run() {
                return supplier.get();
            }
        }.perform();
    }

    public static <T> T waitUntil(Supplier<T> supplier) {
        return waitUntil(defaultTimeouts(), supplier);
    }

    public static <T> T waitUntil(WithTimeout timeouts, Supplier<T> supplier) {
        return new Waiter<T>(timeouts) {
            @Override
            protected T run() {
                return supplier.get();
            }
        }.checkResult();
    }

    public static Boolean isTrue(Supplier<Boolean> supplier) {
        return isTrue(defaultTimeouts(), supplier);
    }

    public static Boolean isTrue(WithTimeout timeouts, Supplier<Boolean> supplier) {
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

    public static Boolean isFalse(Supplier<Boolean> supplier) {
        return isFalse(defaultTimeouts(), supplier);
    }

    public static Boolean isFalse(WithTimeout timeouts, Supplier<Boolean> supplier) {
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
