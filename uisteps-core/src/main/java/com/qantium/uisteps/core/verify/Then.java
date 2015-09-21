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
package com.qantium.uisteps.core.verify;

import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.conditions.ConditionPool;
import com.qantium.uisteps.core.verify.conditions.LogicOperation;
import com.qantium.uisteps.core.verify.results.ExpectedResult;
import com.qantium.uisteps.core.verify.results.LastExpectedResult;

/**
 *
 * @author ASolyankin
 */
public class Then {

    private final Verify verify;
    public final And and = new And();
    public final Or or = new Or();

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
        verify.getResult().add(new ConditionPool().set(logicOperation));
        return verify;
    }

    public class And extends Preposition {

        public And() {
            super(LogicOperation.AND);
        }

    }

    public class Or extends Preposition {

        public Or() {
            super(LogicOperation.OR);
        }

    }

    protected class Preposition {

        private final LogicOperation logicOperation;

        Preposition(LogicOperation logicOperation) {
            this.logicOperation = logicOperation;
        }

        public ExpectedResult _that(boolean condition) {
            return then(logicOperation)._that(condition);
        }

        public Then _that(Condition... conditions) {
            return then(logicOperation)._that(conditions);
        }

        public LastExpectedResult that(boolean condition) {
            return then(logicOperation).that(condition);
        }

        public Then that(Condition... conditions) {
            return then(logicOperation).that(conditions);
        }
    }

}
