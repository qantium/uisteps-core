package com.qantium.uisteps.core.data;

import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class MemberCollector {

    private final Object obj;
    private HashMap<String, AccessibleObject> members;

    protected MemberCollector(Object obj) {
        this.obj = obj;
    }

    public static MemberCollector membersOf(Object obj) {
        return new MemberCollector(obj);
    }

    public Map<String, AccessibleObject> getMembers() {
        if (members == null) {
            members = new HashMap<>();
            collectMembers(obj.getClass());
        }
        return members;
    }

    public Object getObject() {
        return obj;
    }

    private void collectMembers(Class<?> uiObject) {
        asList(uiObject.getFields()).stream().forEach(field -> collect(field));
        asList(uiObject.getMethods()).stream().forEach(method -> collect(method));
    }

    private void collect(AccessibleObject member) {
        if (member.isAnnotationPresent(KeyWord.class)) {
            String name = member.getAnnotation(KeyWord.class).value();
            putMember(name, member);
        }
    }

    private void putMember(String name, AccessibleObject member) {

        if (members.containsKey(name)) {
            throw new IllegalStateException("Content key \"" + name + "\" must be unique in \"" + this + "\"");
        }

        members.put(name, member);

    }
}
