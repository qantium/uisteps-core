package com.qantium.uisteps.core.browser.pages.elements.finder;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
public enum How {
    CONTAINS {
        @Override
        public boolean isFound(String attribute, String value) {
            Bean bean = getBean(attribute, value);
            return bean.attribute.contains(bean.value);
        }
    },
    EQUALS {
        @Override
        public boolean isFound(String attribute, String value) {
            Bean bean = getBean(attribute, value);
            return bean.attribute.equals(bean.value);
        }
    },
    MATCHES {
        @Override
        public boolean isFound(String attribute, String value) {
            getBean(attribute, value);
            return attribute.matches(value);
        }
    },
    STARTS_WITH {
        @Override
        public boolean isFound(String attribute, String value) {
            Bean bean = getBean(attribute, value);
            return bean.attribute.startsWith(bean.value);
        }
    },
    ENDS_WITH {
        @Override
        public boolean isFound(String attribute, String value) {
            Bean bean = getBean(attribute, value);
            return bean.attribute.endsWith(bean.value);
        }
    };

    private boolean ignoreCase;


    public How ignoringCase() {
        return ignoreCase(true);
    }

    public How ignoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", " ");
    }

    public abstract boolean isFound(String attribute, String value);

    protected Bean getBean(String attribute, String value) {
        return new Bean(attribute, value);
    }

    private class Bean {
        public final String attribute;
        public final String value;

        public Bean(String attribute, String value) {
            if (isEmpty(value)) {
                throw new IllegalArgumentException("Value for attribute " + attribute + " cannot be empty!");
            }
            if (ignoreCase) {
                this.attribute = attribute.toUpperCase();
                this.value = value.toUpperCase();
            } else {
                this.attribute = attribute;
                this.value = value;
            }
        }
    }
}
