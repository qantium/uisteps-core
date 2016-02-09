package com.qantium.uisteps.core.tests.listeners;

/**
 * Created by Anton Solyankin
 */

import com.qantium.uisteps.core.utils.testrail.TestRailAdapter;
import com.qantium.uisteps.core.utils.testrail.TestRailStatus;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class UIStepsTestNGListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult testResult) {
        reportTestrail(testResult);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        reportTestrail(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        reportTestrail(testResult);
    }

    protected void reportTestrail(ITestResult testResult) {

        TestRailStatus status = getStatus(testResult);
        TestRailAdapter testRailAdapter = new TestRailAdapter();
        String[] groups = testResult.getMethod().getGroups();

        for (String group : groups) {

            if (group.startsWith("#")) {
                testRailAdapter.addTestResult(group.replace("#", ""), status);
            }
        }
    }

    protected TestRailStatus getStatus(ITestResult testResult) {
        switch (testResult.getStatus()) {
            case 2:
                return TestRailStatus.FAILED;
            default:
                return TestRailStatus.PASSED;
        }
    }
}
