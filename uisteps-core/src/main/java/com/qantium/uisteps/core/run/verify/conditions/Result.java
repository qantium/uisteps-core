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

        result.append("<table border='1' cellpadding='3'>");

        result.append("<tr>");

        if (this.isSuccessful()) {
            result.append("<td>");
        } else {
            result.append("<td colspan='2'>");
        }

        result.append("<b>Expected result</b></td>");

        if (this.isSuccessful()) {
            result.append("<td><b>Actual result</b></td>");
        }

        result.append("<td><b>Status</b></td>");
        result.append("<td></td></tr>");

        Iterator<WithLogicOperation> iterator = getConditions().iterator();
        ConditionPool previousCondition = null;
        
        while (iterator.hasNext()) {

            ConditionPool condition = (ConditionPool) iterator.next();

            result.append("<tbody>");

            if (previousCondition != null) {
                result.append("<tr><td colspan='4'><b>");
                result.append(previousCondition.getLogicOperation());
                result.append("</b></td></tr>");
            }

            result.append(condition);
            result.append("</tbody>");
            previousCondition = condition;
        }

        result.append("</table>");
        return result.toString();

    }
}
