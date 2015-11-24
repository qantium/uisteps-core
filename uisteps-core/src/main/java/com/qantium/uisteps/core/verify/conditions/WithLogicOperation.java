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
public abstract class WithLogicOperation {

    private LogicOperation logicOperation = LogicOperation.OR;

    public abstract boolean isSuccessful();

    public LogicOperation getLogicOperation() {
        return logicOperation;
    }

    public <T extends WithLogicOperation> T set(LogicOperation logicOperation) {
        this.logicOperation = logicOperation;
        return (T) this;
    }

    public WithLogicOperation and() {
        return set(LogicOperation.AND);
    }

    public WithLogicOperation or() {
        return set(LogicOperation.OR);
    }
}
