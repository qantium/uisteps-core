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
package com.qantium.uisteps.core.verify.results;

import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.conditions.ConditionContainer;
import com.qantium.uisteps.core.verify.conditions.ConditionPool;
import com.qantium.uisteps.core.verify.conditions.WithLogicOperation;
import java.util.Iterator;

/**
 *
 * @author ASolyankin
 */
public class Result extends ConditionContainer {

    public Result() {
        reset();
    }

    @Override
    public final void reset() {
        super.reset();
        add(new ConditionPool());
    }

    public void add(Condition... conditions) {

        for (Condition condition : conditions) {
            getlastConditionPool().add(condition);
        }
    }

    public ConditionPool getlastConditionPool() {
        return (ConditionPool) getConditions().get(getConditions().size() - 1);
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result
                .append("<table border='1' cellpadding='3' style='background-color:#EEEADD !important'>")
                .append("<tr>")
                .append("<td><b>Expected result</b></td>")
                .append("<td><b>Actual result</b></td>")
                .append("<td><b>Status</b></td>")
                .append("</tr>");

        Iterator<WithLogicOperation> iterator = getConditions().iterator();
        ConditionPool previousCondition = null;

        while (iterator.hasNext()) {

            ConditionPool condition = (ConditionPool) iterator.next();

            if (previousCondition != null) {
                result
                        .append("<tr>")
                        .append("<td colspan='4'><b>")
                        .append(previousCondition.getLogicOperation())
                        .append("</b></td>")
                        .append("</tr>");
            }

            result.append(condition);
            previousCondition = condition;
        }

        return result.append("</table>").toString();

    }
}
