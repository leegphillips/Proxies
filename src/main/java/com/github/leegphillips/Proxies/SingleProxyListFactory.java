package com.github.leegphillips.Proxies;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;

public class SingleProxyListFactory {
    public List<Proxy> get() {
        return Collections.singletonList(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.90.58.187", 3128)));
    }
}
