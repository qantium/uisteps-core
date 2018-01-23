package com.qantium.uisteps.core.browser.pages;

import java.util.LinkedList;

public class DisplayStack {

    private static ThreadLocal<LinkedList<UIObject>> list = ThreadLocal.withInitial(() -> new LinkedList<UIObject>());

    public static boolean contains(UIObject uiObject) {
        return list.get().contains(uiObject);
    }

    public static void remove(UIObject uiObject) {
        list.get().remove(uiObject);
    }

    public static void add(UIObject uiObject) {
        list.get().add(uiObject);
    }

}
