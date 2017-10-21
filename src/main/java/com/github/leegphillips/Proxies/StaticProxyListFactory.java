package com.github.leegphillips.Proxies;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

public class StaticProxyListFactory {
    public List<Proxy> get() {
        return Arrays.asList(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("201.16.246.168", 3128)),
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("187.115.138.131", 3128)),
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.90.58.187", 3128)),
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("190.147.134.57", 53281)),
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("116.58.39.76", 8080))
        );
    }
}
