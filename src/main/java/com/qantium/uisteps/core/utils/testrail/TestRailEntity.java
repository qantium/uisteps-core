package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by Anton Solyankin
 */


public class TestRailEntity {

    private final TestRailType type;
    private final int id;

    public TestRailEntity(String fullId) {
        id = Integer.parseInt(fullId.substring(1));
        type = TestRailType.get(fullId.substring(0, 1));
    }

    public TestRailEntity(TestRailType type, int id) {
        this.type = type;
        this.id = id;
    }

    public TestRailType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return type.mark + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestRailEntity)) return false;

        TestRailEntity that = (TestRailEntity) o;

        if (getId() != that.getId()) return false;
        return getType() == that.getType();

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + getId();
        return result;
    }
}
