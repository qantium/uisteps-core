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
package com.qantium.uisteps.core.run.verify.conditions;

import java.util.ArrayList;

/**
 *
 * @author ASolyankin
 */
public class ConditionContainer extends WithLogicOperation {

    private ArrayList<WithLogicOperation> conditions = new ArrayList();
    private Boolean successful = null;

    @Override
    public boolean isSuccessful() {

        if (successful == null) {

            WithLogicOperation previousCondition = null;

            for (WithLogicOperation condition : conditions) {

                if (previousCondition == null) {
                    successful = condition.isSuccessful();
                } else {
                    successful = previousCondition.getLogicOperation().execute(successful, condition.isSuccessful());
                }

                previousCondition = condition;
            }

            if (successful == null) {
                successful = true;
            }

        }
        return successful;
    }

    public void reset() {
        conditions = new ArrayList();
        successful = null;
        set(LogicOperation.OR);
    }

    public void add(WithLogicOperation condition) {
        conditions.add(condition);
    }

    public ArrayList<WithLogicOperation> getConditions() {
        return conditions;
    }

}
