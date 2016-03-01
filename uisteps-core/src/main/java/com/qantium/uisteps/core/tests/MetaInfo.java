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
public class MetaInfo {

    private final String META_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.META_INFO_REGEXP);
    private final String META_PARAM_REGEXP = UIStepsProperties.getProperty(UIStepsProperty.META_PARAM_REGEXP);
    private final String title;
    private String titleWithoutMeta;
    private String meta = "";
    private String metaData = "";
    private Map<String, String> metaParams = new HashMap();

    public MetaInfo(String title) {
        this.title = title;

        Pattern pattern = Pattern.compile(META_REGEXP);
        Matcher matcher = pattern.matcher(title);

        if (matcher.find()) {
            meta = matcher.group();
            metaData = matcher.group();
            parceMetaData();
        }

        if(StringUtils.isEmpty(meta)) {
            titleWithoutMeta = title;
        } else {
            titleWithoutMeta = title.replace(meta, "");
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

    public String getTitle() {
        return title;
    }

    public String getTitleWithoutMeta() {
        return titleWithoutMeta;
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

    public String get(String key) {

        if(StringUtils.isEmpty(key)) {
            return null;
        }

        if(!metaParams.containsKey(key)) {
            return null;
        }

        return metaParams.get(key);
    }
}
