package cn.reghao.jutil.tool.http;

import cn.reghao.jutil.jdk.http.proxy.RequestProxy;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author reghao
 * @date 2019-11-29 10:03:18
 */
public class BaseWebRequest {
    protected final CloseableHttpClient client;
    @Deprecated
    protected final Charset charset;
    protected final String bodyCharset;
    protected final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";

    public BaseWebRequest() {
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionConfig())
                .setDefaultRequestConfig(requestConfig());
        //.setKeepAliveStrategy(keepAliveConfig())
        this.client = builder.build();
        this.charset = StandardCharsets.UTF_8;
        this.bodyCharset = "utf8";
    }

    public BaseWebRequest(String charsetName, boolean enableProxy) {
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionConfig(enableProxy))
                .setDefaultRequestConfig(requestConfig());
        //.setKeepAliveStrategy(keepAliveConfig())
        this.client = builder.build();
        this.charset = StandardCharsets.UTF_8;
        this.bodyCharset = charsetName;
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
     * 连接池配置
     *
     * @param
     * @return
     * @date 2021-03-23 下午6:21
     */
    private PoolingHttpClientConnectionManager connectionConfig() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
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
    protected HttpContext httpContext(List<Cookie> cookies, RequestProxy proxy) {
        HttpContext context = HttpClientContext.create();
        setCookies(context, cookies);
        if (proxy != null) {
            setProxy(context, proxy);
        }
        return context;
    }

    private void setCookies(HttpContext context, List<Cookie> cookies) {
        // BasicClientCookie
        CookieStore cookieStore = new BasicCookieStore();
        cookies.forEach(cookieStore::addCookie);
        // 设置 cookies
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    private void setProxy(HttpContext context, RequestProxy proxy) {
        InetSocketAddress socketAddress = new InetSocketAddress(proxy.getHost(), proxy.getPort());
        // 设置 SOCKS5 代理
        context.setAttribute("socks.address", socketAddress);
        // TODO 设置 HTTP/HTTPS 代理
    }
}
