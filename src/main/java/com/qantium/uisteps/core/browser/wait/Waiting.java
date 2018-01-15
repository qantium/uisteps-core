package com.qantium.uisteps.core.browser.wait;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.qantium.uisteps.core.browser.wait.TimeoutBuilder.defaultTimeouts;

public class Waiting {

    public static void waitFor(Runnable runnable) {
        waitFor(defaultTimeouts(), runnable);
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
