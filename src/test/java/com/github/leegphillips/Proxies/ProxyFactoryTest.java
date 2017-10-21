package com.github.leegphillips.Proxies;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ProxyFactoryTest {
    @Test
    public void simple() {
        assertNotNull(ProxyFactory.create("46.102.241.199:3128 0.2990 Transp. NN N 100% 2011-11-19 17:56:02"));
    }
}
