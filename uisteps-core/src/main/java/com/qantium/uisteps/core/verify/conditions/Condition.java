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
package com.qantium.uisteps.core.verify.conditions;

import com.qantium.uisteps.core.verify.results.LogicOperation;

/**
 *
 * @author ASolyankin
 */
public class Condition extends WithLogicOperation {

    private final boolean successful;
    private String expectedResult;
    private String actualResult;

    public Condition(boolean successful, String expectedResult) {
        this(successful, expectedResult, "");
    }

    public Condition(boolean successful, String expectedResult, String actualResult) {
        this.successful = successful;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
    }

    public static Condition isTrue(boolean successful, String expectedResult) {
        return isTrue(false, successful, expectedResult);
    }

    public static Condition isTrue(boolean successful, String expectedResult, String actualResult) {
        return isTrue(false, successful, expectedResult, actualResult);
    }

    static Condition isTrue(boolean not, boolean successful, String expectedResult) {
        return isTrue(not, successful, expectedResult, "");
    }

    static Condition isTrue(boolean not, boolean successful, String expectedResult, String actualResult) {

        if (not) {
            successful = !successful;
        }
        return new Condition(successful, expectedResult, actualResult);
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public String getActualResult() {
        return actualResult;
    }

    public <T extends Condition> T setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
        return (T) this;
    }

    public <T extends Condition> T setActualResult(String actualResult) {
        this.actualResult = actualResult;
        return (T) this;
    }

    @Override
    public Condition set(LogicOperation logicOperation) {
        return (Condition) super.set(logicOperation);
    }

    @Override
    public Condition or() {
        return (Condition) super.or();
    }

    @Override
    public Condition and() {
        return (Condition) super.and();
    }

    @Override
    public String toString() {
        return "Condition{" + "successful=" + successful + ", expectedResult=" + expectedResult + ", actualResult=" + actualResult + ", logicOperation=" + getLogicOperation() + "}";
    }

}