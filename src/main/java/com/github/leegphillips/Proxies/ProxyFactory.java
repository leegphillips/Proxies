package com.github.leegphillips.Proxies;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyFactory {

    private static final String pattern = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})";
    private static final Pattern compiledPattern = Pattern.compile(pattern);

    public static Proxy create(String input) {
        Matcher matcher = compiledPattern.matcher(input);
        return matcher.find() ? createKnown(matcher.group()) : null;
    }

    private static Proxy createKnown(String input) {
        String[] split = input.split(":");
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.valueOf(split[1])));
    }

    public static boolean check(Proxy proxy) {
        try {
            URL url = new URL("https://canihazip.com/s");
//            URLConnection connection = url.openConnection();
//            String ipNoProxy = IOUtils.toString(connection.getInputStream(), connection.getContentEncoding());

            URLConnection proxyConnection = url.openConnection(proxy);
            String ipProxy = IOUtils.toString(proxyConnection.getInputStream(), proxyConnection.getContentEncoding());

            boolean result = proxy.address().toString().contains(ipProxy);
            System.out.println(Thread.currentThread().getName() + " " + result + " " + ipProxy);

            return result;
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
    }
}
