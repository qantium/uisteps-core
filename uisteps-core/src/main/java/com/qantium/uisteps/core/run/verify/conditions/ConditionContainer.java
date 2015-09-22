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
    private boolean modified;
    private boolean isSuccessful = true;
    private boolean hasFailures = false;

    @Override
    public boolean isSuccessful() {

        if (modified) {

            for (WithLogicOperation condition : conditions) {

                isSuccessful = condition.getLogicOperation().execute(isSuccessful, condition.isSuccessful());

                if (conditions.indexOf(condition) != 0 && condition.getLogicOperation() == LogicOperation.AND && !isSuccessful) {
                    break;
                }

                if (condition.getLogicOperation() == LogicOperation.OR && isSuccessful) {
                    break;
                }
            }

            for (WithLogicOperation condition : conditions) {
                
                hasFailures = !condition.isSuccessful();
                
                if(hasFailures) {
                    break;
                }
            }
            
            modified = false;
        }

        return isSuccessful;
    }

    public boolean hasFailures() {

        if (modified) {
            isSuccessful() ;
        }

        return hasFailures;
    }
    
    public void reset() {
        modified = false;
        isSuccessful = true;
        conditions = new ArrayList();
    }

    public void add(WithLogicOperation condition) {
        conditions.add(condition);
        modified = true;
    }

    public ArrayList<WithLogicOperation> getConditions() {
        return conditions;
    }
    
}
