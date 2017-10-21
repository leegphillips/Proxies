package com.github.leegphillips.Proxies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PremProxyProxyListFactory {
    private static final String URL = "https://premproxy.com/list/ip-port/1.htm";
    private static final String START_TOKEN = "<!-- IP:Port list -->";
    private static final String END_TOKEN = "<!-- End of IP:Port list -->";

    public List<Proxy> get() throws IOException {
        List<Proxy> result = new ArrayList<>();
        Document doc = Jsoup.connect(URL).get();
        String page = doc.toString();
        StringTokenizer st = new StringTokenizer(page.substring(page.indexOf(START_TOKEN) + START_TOKEN.length(), page.indexOf(END_TOKEN)));
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.matches("^[0-9].*$")) {
                String[] data = token.split(":");
                result.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(data[0], Integer.valueOf(data[1]))));
            }
        }
        return result;
    }
}
