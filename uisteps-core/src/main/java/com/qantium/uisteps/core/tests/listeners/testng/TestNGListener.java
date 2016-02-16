package com.qantium.uisteps.core.tests.listeners.testng;

/**
 * Created by Anton Solyankin
 */

import com.qantium.uisteps.core.utils.testrail.TestRail;
import com.qantium.uisteps.core.utils.testrail.TestRailType;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestNGListener extends TestListenerAdapter {

    private TestRail.Adapter testRailAdapter = testRail().getAdapter();

    @Override
    public void onTestSuccess(ITestResult testResult) {
        reportTestRail(testResult);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        reportTestRail(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        reportTestRail(testResult);
    }

    protected void reportTestRail(ITestResult testResult) {

        String[] groups = testResult.getMethod().getGroups();

        for (String group : groups) {
            int status = getTestRailStatus(testResult);

            if (group.startsWith(TestRailType.CASE.mark)) {
                testRailAdapter.addTestResult(group, status);
            }
        }
    }

    protected int getTestRailStatus(ITestResult testResult) {
        switch (testResult.getStatus()) {
            case 2:
                return 5;
            default:
                return 1;
        }
    }

    protected TestRail testRail() {
        return new TestRail();
    }
    
}
