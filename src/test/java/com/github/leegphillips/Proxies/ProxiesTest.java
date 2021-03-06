package com.github.leegphillips.Proxies;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class ProxiesTest {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(256);

    @BeforeClass
    public static void createTrustManagerThatDoesntValidate() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    @Test
    public void basicFetch() throws IOException {
        URL url = new URL("https://canihazip.com/s");
        URLConnection connection = url.openConnection();
        String ipNoProxy = IOUtils.toString(connection.getInputStream(), connection.getContentEncoding());

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.90.58.187", 3128));
        URLConnection proxiedConnection = url.openConnection(proxy);
        String ipProxy = IOUtils.toString(proxiedConnection.getInputStream(), proxiedConnection.getContentEncoding());

        assertNotNull(ipNoProxy);
        assertNotNull(ipProxy);
        assertNotEquals("", ipNoProxy);
        assertNotEquals("", ipProxy);
        assertNotEquals(ipNoProxy, ipProxy);
    }

    @Ignore
    @Test
    public void multithreadedFetch() throws IOException, ExecutionException, InterruptedException {
        URL url = new URL("https://canihazip.com/s");
        URLConnection connection = url.openConnection();
        String ipNoProxy = IOUtils.toString(connection.getInputStream(), connection.getContentEncoding());

        List<CompletableFuture<String>> futures = new ArrayList<>();

        new StaticProxyListFactory().get().stream().forEach(proxy -> futures.add(CompletableFuture.supplyAsync(() -> {
            try {
                URLConnection proxiedConnection = url.openConnection(proxy);
                String result = IOUtils.toString(proxiedConnection.getInputStream(), proxiedConnection.getContentEncoding());
                return result;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, POOL)));

        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(futures.toArray(new CompletableFuture[]{}));

        System.out.println(objectCompletableFuture.get());
    }

    @Test
    public void processList() throws IOException {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "256");
        String proxyList = IOUtils.toString(ClassLoader.getSystemClassLoader().getResourceAsStream("proxies2.txt"), Charset.defaultCharset());
        List<Proxy> proxies = stream(proxyList.split("\n")).parallel()
                .map(ProxyFactory::create)
                .filter(ProxyFactory::check)
                .collect(toList());
        System.out.println(proxies.size());
    }

    @Test
    @Ignore
    public void processList3() throws IOException {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "256");
        String proxyList = IOUtils.toString(ClassLoader.getSystemClassLoader().getResourceAsStream("proxies3.txt"), Charset.defaultCharset());
        List<Proxy> proxies = stream(proxyList.split("\n")).parallel()
//                .filter(line -> line.contains("elite"))
                .map(ProxyFactory::create)
                .filter(Objects::nonNull)
                .filter(ProxyFactory::check)
                .collect(toList());
        System.out.println(proxies.size());
    }
}
