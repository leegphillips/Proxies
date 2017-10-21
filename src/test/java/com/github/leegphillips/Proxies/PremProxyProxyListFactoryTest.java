package com.github.leegphillips.Proxies;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class PremProxyProxyListFactoryTest {
    @BeforeClass
    public static void link() throws KeyManagementException, NoSuchAlgorithmException {
        ProxiesTest.createTrustManagerThatDoesntValidate();
    }

    @Test
    public void testListCreate() throws IOException {
        assertEquals(30, new PremProxyProxyListFactory().get().size());
    }
}
