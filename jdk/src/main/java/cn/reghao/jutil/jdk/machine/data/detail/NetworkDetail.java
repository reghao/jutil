package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2021-10-16 18:15:16
 */
public class NetworkDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String iface;
    // 格式 e8-2a-ea-7c-4a-a2
    private String mac;
    private String ipv4;
    private String pubicIpv4;
    private String ipv6;

    public NetworkDetail(String iface, String mac) {
        this.iface = iface;
        this.mac = mac;
    }

    public String getIface() {
        return iface;
    }

    public String getMac() {
        return mac;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setPubicIpv4(String pubicIpv4) {
        this.pubicIpv4 = pubicIpv4;
    }

    public String getPubicIpv4() {
        return pubicIpv4;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getIpv6() {
        return ipv6;
    }
}
