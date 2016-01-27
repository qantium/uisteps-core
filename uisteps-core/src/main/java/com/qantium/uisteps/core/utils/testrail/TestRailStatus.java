package com.qantium.uisteps.core.utils.testrail;

/**
 * Created by Solan on 25.01.2016.
 */
public enum TestRailStatus {

    PASSED {
        @Override
        public String toString() {
            return "1";
        }
    },
    BLOCKED {
        @Override
        public String toString() {
            return "2";
        }
    },
    FAILED {
        @Override
        public String toString() {
            return "5";
        }
    }
}
