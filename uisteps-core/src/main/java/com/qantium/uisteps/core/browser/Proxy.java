/*
 * Copyright 2015 A.Solyankin.
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
package com.qantium.uisteps.core.browser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

/**
 *
 * @author A.Solyankin
 */
public class Proxy {

    private final BrowserMobProxyServer mobProxy;
    private org.openqa.selenium.Proxy seleniumProxy;
    private final InetAddress ip;
    private final Integer port;

    public static class ProxyBuilder {

        private InetAddress ip;
        private Integer port = null;

        public ProxyBuilder setIp(String ip) {
            
            try {
                if ("localhost".equals(ip) || "127.0.0.1".equals(ip)) {
                    this.ip = InetAddress.getLocalHost();
                } else {
                    this.ip = InetAddress.getByName(ip);
                }
            } catch (UnknownHostException ex) {
                throw new RuntimeException("Cannot get proxy address!\nCause: " + ex);
            }
            return this;
        }

        public ProxyBuilder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Proxy build() {
            return new Proxy(ip, port);
        }
    }

    protected Proxy(InetAddress ip, Integer port) {
        this.ip = ip;
        this.port = port;
        mobProxy = new BrowserMobProxyServer();
    }
    
    public org.openqa.selenium.Proxy start() {
        
        if(seleniumProxy == null) {
            if (ip != null && port != null) {
                mobProxy.start(port, this.ip);
            } else if (port != null) {
                mobProxy.start(port);
            } else {
                mobProxy.start();
            }
            mobProxy.newHar();
            seleniumProxy = ClientUtil.createSeleniumProxy(mobProxy);
        }
        return seleniumProxy;
    }

    public BrowserMobProxyServer getMobProxy() {
        return mobProxy;
    }

    public InetAddress getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

}
