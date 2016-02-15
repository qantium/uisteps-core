/*
 * Copyright 2016 ASolyankin.
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
package com.qantium.uisteps.core.utils.zk;

/**
 *
 * @author Anton Solyankin
 */
public class ZKNumber {

    private final static String ZK_NUMBERS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final int intValue;
    private final String stringValue;

    public ZKNumber(int intValue) {
        this.intValue = intValue;
        this.stringValue = getStringValue(intValue);
    }

    public ZKNumber(String stringValue) {
        this.stringValue = stringValue;
        this.intValue = getIntValue(stringValue);
    }

    public int toInt() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.intValue;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ZKNumber other = (ZKNumber) obj;
        if (this.intValue != other.intValue) {
            return false;
        }
        return true;
    }

    private int getIntValue(String zkValue) {
        int value = 0;

        for (int i = 0; i < zkValue.length(); i++) {
            char number = zkValue.charAt(i);
            value += Math.pow(36, i) * ZK_NUMBERS.indexOf(number);
        }
        return value;
    }

    private String getStringValue(int intValue) {
        String value = "";

        int cel;
        int ost;

        while(true) {
            ost = intValue % 36;
            cel = intValue / 36;

            if(cel == 0) {
                value += ZK_NUMBERS.charAt(intValue);
                break;
            } else {
                intValue = cel;
                value += ZK_NUMBERS.charAt(ost);
            }
        }
        return value;
    }
}
