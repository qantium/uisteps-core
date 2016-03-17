package com.qantium.uisteps.core.browser.pages;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author ASolyankin
 */
public class Url {

    private String protocol = "";
    private String user = "";
    private String password = "";
    private String prefix = "";
    private String host = "";
    private Integer port = -1;
    private String postfix = "";

    public Url() {
    }

    public Url(String url) throws MalformedURLException {
        this(new URL(url));
        
        if(StringUtils.contains(url, "#")) {
            postfix += url.substring(url.indexOf("#"));
        }
    }

    public Url(URL url) {
        protocol = url.getProtocol();
        port = url.getPort();

        if (url.getUserInfo() != null) {
            String[] userInfo = url.getUserInfo().split(":");

            if (userInfo.length > 0) {
                user = userInfo[0];
            }

            if (userInfo.length > 1) {
                password = userInfo[1];
            }
        }

        if (url.getHost() != null) {
            host = url.getHost();
        }

        if (url.getFile() != null) {
            postfix = url.getFile();
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public <T extends Url> T setProtocol(String value) {

        if(StringUtils.isEmpty(value)) {
            protocol = "";
        } else {
            protocol = value;
        }
        return (T) this;
    }

    public String getUser() {
        return user;
    }

    public <T extends Url> T setUser(String value) {

        if(StringUtils.isEmpty(value)) {
            user = "";
        } else {
            user = value;
        }
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public <T extends Url> T setPassword(String value) {
        
        if(StringUtils.isEmpty(value)) {
            password = "";
        } else {
            password = value;
        }
        return (T) this;
    }

    public String getHost() {
        return host;
    }

    public <T extends Url> T setHost(String value) {

        if(StringUtils.isEmpty(value)) {
            host = "";
        } else {
            host = value;
        }
        return (T) this;
    }

    public Integer getPort() {
        return port;
    }

    public <T extends Url> T setPort(Integer value) {

        if (value != null) {
            port = value;
        }
        return (T) this;
    }

    public String getPrefix() {
        return prefix;
    }

    public <T extends Url> T setPrefix(String value) {
        
        if(StringUtils.isEmpty(value)) {
            prefix = "";
        } else {
            prefix = value;
        }
        return (T) this;
    }
    
    public <T extends Url> T appendPrefix(String value) {

        if (value != null) {
            prefix += value;
        }
        return (T) this;
    }

    public <T extends Url> T prependPrefix(String value) {

        if (value != null) {
            prefix = value + prefix;
        }
        return (T) this;
    }

    public String getPostfix() {
        return postfix;
    }

    public <T extends Url> T setPostfix(String value) {

        if (value != null) {
            postfix = value;
        }
        return (T) this;
    }

    public <T extends Url> T appendPostfix(String value) {
        
        if(StringUtils.isEmpty(value)) {
            postfix = "";
        } else {
            postfix = value;
        }
        return (T) this;
    }

    public <T extends Url> T prependPostfix(String value) {

        if (value != null) {
            postfix = value + postfix;
        }
        return (T) this;
    }

    @Override
    public String toString() {

        StringBuilder url = new StringBuilder();

        if(!StringUtils.isEmpty(protocol)) {
            url.append(protocol).append("://");
        }
        
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {
            url.append(user).append(":").append(password).append("@");
        }
        url.append(prefix).append(host);

        if (port > -1) {
            url.append(":").append(port);
        }

        url.append(postfix);

        return url.toString();
    }

    public URL getURL() throws MalformedURLException {
        return new URL(toString());
    }
}
