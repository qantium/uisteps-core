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

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;

import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumSet;
import java.util.HashSet;

/**
 * @author A.Solyankin
 */
public class Proxy {

    private final BrowserMobProxyServer mobProxy;
    private org.openqa.selenium.Proxy seleniumProxy;
    private final InetAddress ip;
    private Integer port;

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

        if (seleniumProxy == null) {

            if (!mobProxy.isStarted()) {

                if (ip != null && port != null && port >= 0) {
                    mobProxy.start(port, this.ip);
                } else if (port != null) {

                    if (port < 0) {
                        port = 0;

                        while (true) {
                            try {
                                mobProxy.start(port);
                                break;
                            } catch (Exception ex) {
                                if (BindException.class.equals(ex.getCause().getClass())) {
                                    port++;
                                } else {
                                    throw ex;
                                }
                            }
                        }
                    } else {
                        mobProxy.start(port);
                    }
                } else {
                    mobProxy.start();
                }
            }
            mobProxy.setHarCaptureTypes(CaptureType.values());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (!getIp().equals(proxy.getIp())) return false;
        return getPort().equals(proxy.getPort());

    }

    @Override
    public int hashCode() {
        int result = getIp().hashCode();
        result = 31 * result + getPort().hashCode();
        return result;
    }
}
