package cn.reghao.jutil.tool.http;

import cn.reghao.jutil.tool.http.util.FakeDnsResolver;
import cn.reghao.jutil.tool.http.util.MyConnectionSocketFactory;
import cn.reghao.jutil.tool.http.util.MySSLConnectionSocketFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author reghao
 * @date 2019-11-29 10:03:18
 */
public class BaseWebRequest {
    protected final CloseableHttpClient client;
    protected final String bodyCharset;
    protected HttpContext context;

    public BaseWebRequest() {
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionConfig(false))
                .setDefaultRequestConfig(requestConfig());
        //.setKeepAliveStrategy(keepAliveConfig())
        this.client = builder.build();
        this.bodyCharset = StandardCharsets.UTF_8.name();
        this.context = HttpClientContext.create();
    }

    public BaseWebRequest(String cookie, String domain) {
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionConfig(false))
                .setDefaultRequestConfig(requestConfig());
        //.setKeepAliveStrategy(keepAliveConfig())
        this.client = builder.build();
        this.bodyCharset = StandardCharsets.UTF_8.name();
        this.context = HttpClientContext.create();
        setCookies(cookie, domain);
    }

    /**
     * 连接池配置
     *
     * @param
     * @return
     * @date 2021-03-23 下午6:21
     */
    private PoolingHttpClientConnectionManager connectionConfig(boolean enableProxy) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", new MyConnectionSocketFactory())
                .register("https", new MySSLConnectionSocketFactory(SSLContexts.createSystemDefault(),
                        NoopHostnameVerifier.INSTANCE))
                .build();

        PoolingHttpClientConnectionManager cm;
        if (enableProxy) {
            cm = new PoolingHttpClientConnectionManager(registry, new FakeDnsResolver());
        } else {
            cm = new PoolingHttpClientConnectionManager();
        }

        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(20);
        return cm;
    }

    /**
     * 请求配置
     *
     * @param
     * @return
     * @date 2021-03-23 下午6:21
     */
    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(600_000)
                .setConnectTimeout(600_000)
                .setSocketTimeout(600_000)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
    }

    /**
     * 请求上下文(每个 Website 一个上下文)
     *
     * @param
     * @return
     * @date 2021-03-24 上午2:04
     */
    protected HttpContext httpContext() {
        HttpContext context = HttpClientContext.create();
        String host = "";
        int port = 8888;
        setProxy(context, host, port);
        return context;
    }

    private void setCookies(String cookieText, String domain) {
        String[] pairs = cookieText.replace("\\s+", "").split(";");
        List<Cookie> cookies = new ArrayList<>();
        for (String pair : pairs) {
            String[] strs = pair.split("=");
            String name = strs[0];
            String value = strs[1];
            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setAttribute("domain", domain);
            cookie.setDomain(domain);
            cookie.setPath("/");
            long ms = (long)3600*24*180*1000 + System.currentTimeMillis();
            cookie.setExpiryDate(new Date(ms));
            cookies.add(cookie);
        }

        // BasicClientCookie
        CookieStore cookieStore = new BasicCookieStore();
        cookies.forEach(cookieStore::addCookie);

        // 设置 cookies
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    private void setProxy(HttpContext context, String host, int port) {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        // 设置 SOCKS5 代理
        context.setAttribute("socks.address", socketAddress);
        // TODO 设置 HTTP/HTTPS 代理
    }
}
