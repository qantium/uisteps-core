package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by SolAN on 09.02.2016.
 */


public class TestRailEntity {

    private final TestRailContainerType type;
    private final String id;

    public TestRailEntity(String mark) {
        id = mark.substring(1);
        type = TestRailContainerType.get(mark.substring(0, 1));
    }

    public TestRailEntity(TestRailContainerType type, String id) {
        this.type = type;
        this.id = id;
    }

    public TestRailContainerType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return type.getMark() + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestRailEntity)) return false;

        TestRailEntity that = (TestRailEntity) o;

        if (getType() != that.getType()) return false;
        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
