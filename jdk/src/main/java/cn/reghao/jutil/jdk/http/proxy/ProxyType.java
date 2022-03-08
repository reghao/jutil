package cn.reghao.jutil.jdk.http.proxy;

/**
 * HTTP 请求代理类型
 *
 * @author reghao
 * @date 2019-11-01 12:49:28
 */
public enum ProxyType {
    // HTTP 代理
    HTTP("http"),
    // HTTPS 代理
    HTTPS("https"),
    // SOCKS4 代理
    SOCKS4("socks4"),
    // SOCKS5 代理
    SOCKS5("socks5");

    private String value;

    ProxyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
