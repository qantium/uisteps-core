package com.qantium.uisteps.core.browser.pages;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.name.Name;

import java.lang.reflect.AccessibleObject;
import java.util.*;

public class MemberCollector {

    private final HtmlObject uiObject;
    private Map<String, Queue<AccessibleObject>> members;

    public MemberCollector(HtmlObject uiObject) {
        this.uiObject = uiObject;
    }

    public Map<String, Queue<AccessibleObject>> getMembers() {
        if (members == null) {
            members = new HashMap<>();
            collectMembers(uiObject.getClass());
        }
        return members;
    }

    private void collectMembers(Class<?> uiObject) {
        if (!uiObject.isAnnotationPresent(NotInit.class) && HtmlObject.class.isAssignableFrom(uiObject)) {
            Arrays.asList(uiObject.getDeclaredFields()).stream()
                    .forEach(field -> {
                                if (field.isAnnotationPresent(Name.class)
                                        && HtmlObject.class.isAssignableFrom(field.getType())) {
                                    String name = field.getAnnotation(Name.class).value();
                                    putMember(name, field);
                                }
                            }
                    );

            Arrays.asList(uiObject.getDeclaredMethods()).stream()
                    .forEach(method -> {
                                if (method.isAnnotationPresent(Name.class)
                                        && HtmlObject.class.isAssignableFrom(method.getReturnType())) {
                                    String name = method.getAnnotation(Name.class).value();
                                    putMember(name, method);
                                }
                            }
                    );
            collectMembers(uiObject.getSuperclass());
        }
    }

    private void putMember(String name, AccessibleObject member) {
        Queue<AccessibleObject> memberQueue;
        if (members.containsKey(name)) {
            memberQueue = members.get(name);
        } else {
            memberQueue = new LinkedList<>();
            members.put(name, memberQueue);
        }

        memberQueue.add(member);
    }
}
