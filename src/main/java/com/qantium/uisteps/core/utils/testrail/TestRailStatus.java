package com.qantium.uisteps.core.utils.testrail;

import java.util.Map;

/**
 * Created by Anton Solyankin
 */
public class TestRailStatus {

    private final String name;
    private final int id;

    public TestRailStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestRailStatus that = (TestRailStatus) o;

        if (getId() != that.getId()) return false;
        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getId();
        return result;
    }
}
