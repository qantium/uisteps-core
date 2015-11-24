/*
 * Copyright 2015 A.Solyankin.
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

import com.qantium.uisteps.core.browser.Browser;
import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.eclipse.aether.util.StringUtils;

/**
 *
 * @author A.Solyankin
 */
public class ConditionBuilder {

    public final Browser browser;
    protected boolean not;

    public ConditionBuilder(Browser browser) {
        this.browser = browser;
    }

    public <T extends ConditionBuilder> T not(boolean not) {
        this.not = not;
        return (T) this;
    }

    public <T extends ConditionBuilder> T not(String not) {
        return (T) not(Boolean.valueOf(not));
    }

    public Condition compile(String description, boolean successful, String expected, String actual, String prefix, String postfix) {
        return compile(description, successful, expected, actual, prefix, "not", postfix);
    }

    public Condition compile(String description, boolean successful, String expected, String actual, String prefix, String notSuffix, String postfix) {

        StringBuilder message = new StringBuilder();
        message
                .append(" ")
                .append(prefix)
                .append(" ")
                .append(postfix);

        StringBuilder notMessage = new StringBuilder();
        notMessage
                .append(" ")
                .append(prefix)
                .append(" ")
                .append(notSuffix)
                .append(" ")
                .append(postfix);

        StringBuilder expectedMessage = new StringBuilder(description);

        if (!StringUtils.isEmpty(expected)) {
            expectedMessage
                    .append(" ")
                    .append(expected);
        }

        StringBuilder actualMessage = new StringBuilder(description);

        if (!StringUtils.isEmpty(actual)) {
            actualMessage
                    .append(" ")
                    .append(actual);
        }

        if (StringUtils.isEmpty(expected) || UIStepsProperties.getProperty(UIStepsProperty.NULL).equals(expected)) {
            not(true);
            successful = !successful;
        }

        if (not) {
            expectedMessage.append(notMessage);
            actualMessage.append(message);
        } else {
            expectedMessage.append(message);
            actualMessage.append(notMessage);
        }

        return Condition.isTrue(not, successful, expectedMessage.toString().trim().replace("\\s*", "\\s"), actualMessage.toString().trim().replace("\\s*", "\\s"));
    }
}
