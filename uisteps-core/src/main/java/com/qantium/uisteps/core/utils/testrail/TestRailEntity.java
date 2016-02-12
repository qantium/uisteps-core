package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by Anton Solyankin
 */


public class TestRailEntity {

    private final TestRailType type;
    private final String id;

    public TestRailEntity(String fullId) {
        id = fullId.substring(1);
        type = TestRailType.get(fullId.substring(0, 1));
    }

    public TestRailEntity(TestRailType type, String id) {
        this.type = type;
        this.id = id;
    }

    public TestRailType getType() {
        return type;
    }

    public String getId() {
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
