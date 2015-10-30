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
package com.qantium.uisteps.core.run.verify;

import com.qantium.uisteps.core.run.verify.conditions.ConditionPool;
import com.qantium.uisteps.core.run.verify.conditions.LogicOperation;
import com.qantium.uisteps.core.run.verify.conditions.Result;

/**
 *
 * @author ASolyankin
 */
public class Then {

    private final Verify verify;

    public Then(Verify verify) {
        this.verify = verify;
    }

    public Verify and() {
        return then(LogicOperation.AND);
    }

    public Verify or() {
        return then(LogicOperation.OR);
    }

    private Verify then(LogicOperation logicOperation) {
        Result result = verify.getResult();
        result.getlastConditionPool().set(logicOperation);
        result.add(new ConditionPool());
        return verify;
    }

}
