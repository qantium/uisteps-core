package com.qantium.uisteps.core.browser.zk;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Solyankin
 */
public class ZK {

    public static final String ID_MARK = UIStepsProperties.getProperty(UIStepsProperty.ZK_ID_MARK);

    public static boolean isZkId(String id) {
        return id.contains(ID_MARK);
    }

    public static ByZkId byId(String id) {
        return new ByZkId(id);
    }
}
