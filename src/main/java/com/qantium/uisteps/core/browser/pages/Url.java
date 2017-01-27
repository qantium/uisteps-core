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
        init(url);
    }

    public Url(URL url) {
        init(url);
    }

    protected Url init(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);

        init(url);

        if(StringUtils.contains(urlString, "#")) {
            postfix += urlString.substring(urlString.indexOf("#"));
        }
        return this;
    }

    protected Url init(URL url) {
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
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public Url setProtocol(String value) {

        if(StringUtils.isEmpty(value)) {
            protocol = "";
        } else {
            protocol = value;
        }
        return this;
    }

    public String getUser() {
        return user;
    }

    public Url setUser(String value) {

        if(StringUtils.isEmpty(value)) {
            user = "";
        } else {
            user = value;
        }
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Url setPassword(String value) {
        
        if(StringUtils.isEmpty(value)) {
            password = "";
        } else {
            password = value;
        }
        return this;
    }

    public String getHost() {
        return host;
    }

    public Url setHost(String value) {

        if(StringUtils.isEmpty(value)) {
            host = "";
        } else {
            host = value;
        }
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Url setPort(Integer value) {

        if (value != null) {
            port = value;
        }
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Url setPrefix(String value) {
        
        if(StringUtils.isEmpty(value)) {
            prefix = "";
        } else {
            prefix = value;
        }
        return this;
    }
    
    public Url appendPrefix(String value) {

        if (value != null) {
            prefix += value;
        }
        return this;
    }

    public Url prependPrefix(String value) {

        if (value != null) {
            prefix = value + prefix;
        }
        return this;
    }

    public String getPostfix() {
        return postfix;
    }

    public Url setPostfix(String value) {

        if (value != null) {
            postfix = value;
        }
        return this;
    }

    public Url appendPostfix(String value) {
        
        if(StringUtils.isEmpty(value)) {
            postfix = "";
        } else {
            postfix = value;
        }
        return this;
    }

    public Url prependPostfix(String value) {

        if (value != null) {
            postfix = value + postfix;
        }
        return this;
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
