package com.qantium.uisteps.core.utils.testrail;

import java.util.*;
import java.util.function.Consumer;

import static com.qantium.uisteps.core.properties.UIStepsProperty.TESTRAIL_STATUSES;

/**
 * Created Anton Solyankin
 */
public class TestRailStatuses implements Iterable {

    private static Map<String, TestRailStatus> defaultStatuses = getStatuses();
    private Map<String, TestRailStatus> statuses = getStatuses();

    public TestRailStatuses() {
        statuses = defaultStatuses;
    }

    public TestRailStatuses(Map<String, Integer> statuses) {
        this.statuses = new HashMap();

        for (Map.Entry<String, Integer> status : statuses.entrySet()) {
            TestRailStatus testRailStatus = new TestRailStatus(status.getKey(), status.getValue());
            this.statuses.put(testRailStatus.getName(), testRailStatus);
        }
    }

    public TestRailStatuses(Set<TestRailStatus> statuses) {
        this.statuses = new HashMap();

        for (TestRailStatus status : statuses) {
            this.statuses.put(status.getName(), status);
        }
    }


    public TestRailStatuses(String statuses) {
        this.statuses = getStatuses(statuses);
    }

    private static Map<String, TestRailStatus> getStatuses() {
        return getStatuses(TESTRAIL_STATUSES.getValue());
    }

    private static Map<String, TestRailStatus> getStatuses(String statuses) {

        Map<String, TestRailStatus> statusMap = new HashMap();

        for (String status : statuses.split(";")) {

            String[] keyValue = status.split("=");
            String key = keyValue[0];
            int value = Integer.parseInt(keyValue[1]);

            TestRailStatus testRailStatus = new TestRailStatus(key, value);
            statusMap.put(key, testRailStatus);
        }

        return statusMap;
    }

    public TestRailStatus getByName(String name) {
        return statuses.get(name);
    }

    public TestRailStatus getById(int id) {
        for (TestRailStatus status : statuses.values()) {
            if (status.getId() == id) {
                return status;
            }
        }

        return null;
    }

    public boolean containsByName(String name) {
        return statuses.keySet().contains(name);
    }

    public boolean containsById(int id) {

        for (TestRailStatus status : statuses.values()) {
            if (status.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(TestRailStatus status) {
        return statuses.values().contains(status);
    }

    @Override
    public Iterator iterator() {
        return statuses.values().iterator();
    }

    @Override
    public void forEach(Consumer action) {
        statuses.values().forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return statuses.values().spliterator();
    }
}
