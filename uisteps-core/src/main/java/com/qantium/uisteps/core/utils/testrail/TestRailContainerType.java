package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by Anton Solyankin
 */
public enum TestRailContainerType {
    RUN("R"), SUITE("S"), TEST("T"), CASE("C");

    private final String mark;

    TestRailContainerType(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    public static TestRailContainerType get(String type) {
        for (TestRailContainerType value :values()) {
            if(value.getMark().equals(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Cannot find testrail entity type for " + type);
    }
}
