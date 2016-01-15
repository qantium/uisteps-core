package com.qantium.uisteps.core.tests;

import com.qantium.uisteps.core.properties.UIStepsProperties;
import com.qantium.uisteps.core.properties.UIStepsProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Solyankin
 */
public class StepMeta {

    private final String META_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.STEPS_META_REGEXP);
    private final String META_PARAM_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.STEPS_META_PARAM_REGEXP);
    private final String stepTitle;
    private String stepTitleWithoutMeta;
    private String meta = "";
    private String metaData = "";
    private Map<String, String> metaParams = new HashMap();

    public StepMeta(String stepTitle) {
        this.stepTitle = stepTitle;

        Pattern pattern = Pattern.compile(META_REGEXP);
        Matcher matcher = pattern.matcher(stepTitle);

        if (matcher.find()) {
            meta = matcher.group();
            metaData = matcher.group();
            parceMetaData();
        }

        if(StringUtils.isEmpty(meta)) {
            stepTitleWithoutMeta = stepTitle;
        } else {
            stepTitleWithoutMeta = stepTitle.replace(meta, "");
        }
    }

    private void parceMetaData() {

        Pattern pattern = Pattern.compile(META_PARAM_REGEXP);
        Matcher matcher = pattern.matcher(metaData);


        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            metaParams.put(key, value);
        }
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public String getStepTitleWithoutMeta() {
        return stepTitleWithoutMeta;
    }

    public String getMeta() {
        return meta;
    }

    public String getMetaData() {
        return metaData;
    }

    public Map<String, String> getMetaParams() {
        return metaParams;
    }
}
