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

import java.util.Iterator;

/**
 *
 * @author ASolyankin
 */
public class ConditionPool extends ConditionContainer {

    @Override
    public ConditionPool set(LogicOperation logicOperation) {
        return (ConditionPool) super.set(logicOperation);
    }

    @Override
    public String toString() {

        StringBuilder resultBuilder = new StringBuilder();

        Iterator<WithLogicOperation> iterator = getConditions().iterator();

        while (iterator.hasNext()) {

            Condition condition = (Condition) iterator.next();

            resultBuilder.append("<tr>");

            if (condition.isSuccessful()) {
                resultBuilder.append("<td colspan='2'>");
            } else {
                resultBuilder.append("<td>");
            }

            resultBuilder.append(condition.getExpectedResult()).append("</td>");

            if (!condition.isSuccessful()) {
                resultBuilder.append("<td>").append(condition.getActualResult()).append("</td>");
            }

            if (condition.isSuccessful()) {
                resultBuilder.append("<td bgcolor='#79e86f'><b>SUCCESS</b></td>");
            } else {
                resultBuilder.append("<td bgcolor='#fd989b'><b>FAILURE</b></td>");
            }

            resultBuilder.append("<td bgcolor='#ededed'>");
            
            if(iterator.hasNext()) {
                resultBuilder.append(condition.getLogicOperation());
            }
            
            resultBuilder.append("</td>");
            resultBuilder.append("</tr>");
        }

        return resultBuilder.toString();
    }
}
