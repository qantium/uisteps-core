package com.qantium.uisteps.core.utils.testrail;

import com.qantium.uisteps.core.properties.UIStepsProperties;

import static com.qantium.uisteps.core.properties.UIStepsProperty.TESTRAIL_ACTION;

/**
 * Created by Anton Solyankin
 */
public class TestRail {

    public final Action ACTION;
    private static Adapter adapter;

    public TestRail() {
        this(Action.valueOf(UIStepsProperties.getProperty(TESTRAIL_ACTION).toUpperCase()));
    }

    public TestRail(Action ACTION) {
        this.ACTION = ACTION;
    }

    public TestRail.Adapter getAdapter() {
        if (adapter == null) {
            if (ACTION != TestRail.Action.UNDEFINED) {
                synchronized (this) {
                    if (adapter == null) {
                        adapter = getAdapterInstance();
                    }
                }
            } else {
                synchronized (this) {
                    if (adapter == null) {
                        adapter = getMockAdapter();
                    }
                }
            }
        }
        return adapter;
    }

    public interface Adapter {
        void addTestResult(String testId, int status);
    }

    protected TestRail.Adapter getAdapterInstance() {
        TestRailConfig config = new TestRailConfig();
        return new TestRailAdapter(config);
    }

    protected TestRail.Adapter getMockAdapter() {
        return new TestRailMockAdapter();
    }

    public enum Action {
        EXPORT, IMPORT, UNDEFINED
    }
}
