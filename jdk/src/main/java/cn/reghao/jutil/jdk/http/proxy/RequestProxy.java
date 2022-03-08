package cn.reghao.jutil.jdk.http.proxy;

/**
 * HTTP 请求代理
 *
 * @author reghao
 * @date 2019-12-17 13:21:38
 */
public class RequestProxy {
    private ProxyType type;
    private String host;
    private int port;
    private String usrename;
    private String password;

    public RequestProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.type = ProxyType.SOCKS5;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
