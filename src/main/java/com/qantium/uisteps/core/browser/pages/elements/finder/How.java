package com.qantium.uisteps.core.browser.pages.elements.finder;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Anton Solyankin
 */
public enum How {
    CONTAINS {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.contains(value);
        }
    },
    EQUALS {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.equals(value);
        }
    },
    EQUALS_IGNORE_CASE {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.toUpperCase().equals(value.toUpperCase());
        }
    },
    CONTAINS_IGNORE_CASE {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.toUpperCase().contains(value.toUpperCase());
        }
    },
    MATCHES {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.matches(value);
        }
    },
    STARTS_WITH {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.startsWith(value);
        }
    },
    ENDS_WITH {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.endsWith(value);
        }
    },
    STARTS_WITH_IGNORE_CASE {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.toUpperCase().startsWith(value.toUpperCase());
        }
    },
    ENDS_WITH_IGNORE_CASE {
        @Override
        public boolean isFound(String attribute, String value) {
            verifyIsNotEmpty(attribute, value);
            return attribute.toUpperCase().endsWith(value.toUpperCase());
        }
    };

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", " ");
    }

    public abstract boolean isFound(String attribute, String value);

    private static void verifyIsNotEmpty(String attribute, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Value for attribute " + attribute + " cannot be empty!");
        }
    }
}
