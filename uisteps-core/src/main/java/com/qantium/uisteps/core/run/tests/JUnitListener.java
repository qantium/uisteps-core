/*
 * Copyright 2015 A.Solyankin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qantium.uisteps.core.run.tests;

import com.qantium.uisteps.core.browser.BrowserManager;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 *
 * @author A.Solyankin
 */
public class JUnitListener extends RunListener {
    
    @Override
    public void testIgnored(Description description) throws Exception {
        closeAllBrowsers();
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        closeAllBrowsers();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        closeAllBrowsers();
    }

    @Override
    public void testFinished(Description description) throws Exception {
        closeAllBrowsers();
    }

    @Override
    public void testRunFinished(Result result) throws Exception { 
        closeAllBrowsers();
    }
    
    protected void closeAllBrowsers() {
        BrowserManager.reset();
    }
}
