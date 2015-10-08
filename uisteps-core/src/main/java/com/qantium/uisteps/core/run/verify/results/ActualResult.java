/*
 * Copyright 2015 ASolyankin.
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
package com.qantium.uisteps.core.run.verify.results;

import com.qantium.uisteps.core.run.verify.conditions.Condition;
import com.qantium.uisteps.core.run.verify.Then;
import com.qantium.uisteps.core.run.verify.Verify;

/**
 *
 * @author ASolyankin
 */
public class ActualResult {

    private final Verify verify;
    private final boolean condition;
    private final String expectedResult;

    public ActualResult(Verify verify, boolean condition, String expectedResult) {
        this.verify = verify;
        this.condition = condition;
        this.expectedResult = expectedResult;
    }

    protected Verify verify() {
        return verify;
    }
    
    protected Then verify(String actualResult) {
        verify._conditions(new Condition(condition, expectedResult, actualResult));
        return new Then(verify);
    }

    public Then ifElse(String actualResult) {
        return verify(actualResult);
    }
}
