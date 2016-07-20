package com.qantium.uisteps.core.lifecycle;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qantium.uisteps.core.properties.UIStepsProperties.getProperty;
import static com.qantium.uisteps.core.properties.UIStepsProperty.META_INFO_REGEXP;
import static com.qantium.uisteps.core.properties.UIStepsProperty.META_PARAM_REGEXP;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by Anton Solyankin
 */
public class MetaInfo {

    private final String title;
    private String titleWithoutMeta;
    private String meta = "";
    private Map<String, String> metaParams = new HashMap();

    public MetaInfo(String title) {
        this.title = title;
        titleWithoutMeta = title;
        Pattern pattern = Pattern.compile(getProperty(META_INFO_REGEXP));
        Matcher matcher = pattern.matcher(title);

        if (matcher.find()) {
            meta = matcher.group(1);
            titleWithoutMeta = title.replace(matcher.group(), "");
            parseMetaData();
        }
    }

    private void parseMetaData() {

        Pattern pattern = Pattern.compile(getProperty(META_PARAM_REGEXP));
        Matcher matcher = pattern.matcher(meta);
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
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

    public Map<String, String> getMetaParams() {
        return metaParams;
    }

    public String get(String key) {

        if (isEmpty(key)) {
            return null;
        }

        if (!metaParams.containsKey(key)) {
            return null;
        }

        return metaParams.get(key);
    }
}
