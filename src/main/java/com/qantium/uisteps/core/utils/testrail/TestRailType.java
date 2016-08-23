package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by Anton Solyankin
 */
public enum TestRailType {
    RUN("R"), SUITE("S"), TEST("T"), CASE("C");

    public final String mark;

    TestRailType(String mark) {
        this.mark = mark;
    }

    public static TestRailType get(String type) {
        for (TestRailType value :values()) {
            if(value.mark.equals(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Cannot find testrail entity type for " + type);
    }
}
