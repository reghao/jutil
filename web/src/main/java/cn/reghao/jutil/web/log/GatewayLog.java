package cn.reghao.jutil.web.log;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author reghao
 * @date 2024-11-20 16:40:24
 */
@Setter
@Getter
public class GatewayLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    // ms
    private long requestTime;
    private String targetRoute;
    private String targetService;
    private String requestUrl;
    private String requestMethod;
    private Map<String, String> requestHeaders;
    private String requestBody;
    private long requestBytes;
    private String remoteAddr;
    private int remotePort;
    private int statusCode;
    private Map<String, String> responseHeaders;
    private String responseBody;
    private long responseBytes;
    // ms
    private long responseTime;
    // ms
    private long executeTime;
    private String fingerprint;

    public GatewayLog() {
        this.targetRoute = "";
        this.targetService = "";
        this.requestBody = "";
        this.requestBytes = 0;
        this.remoteAddr = "";
        this.remotePort = 0;
        this.responseBody = "";
        this.responseBytes = 0;
        this.fingerprint = "";
    }

    public GatewayLog(String requestId, long requestTime, String requestUrl, String requestMethod, Map<String, String> requestHeaders,
                      String remoteAddr, int remotePort, int statusCode, Map<String, String> responseHeaders, long responseTime) {
        this.requestId = requestId;
        this.requestTime = requestTime;
        this.targetRoute = "";
        this.targetService = "";
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.requestHeaders = requestHeaders;
        this.requestBody = "";
        this.remoteAddr = remoteAddr;
        this.remotePort = remotePort;
        this.statusCode = statusCode;
        this.responseHeaders = responseHeaders;
        this.responseBody = "";
        this.responseTime = responseTime;
        this.executeTime = responseTime-requestTime;
    }
}
