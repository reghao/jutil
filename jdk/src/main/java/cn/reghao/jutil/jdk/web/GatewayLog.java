package cn.reghao.jutil.jdk.web;

import java.io.Serializable;
import java.util.Map;

/**
 * @author reghao
 * @date 2024-11-20 16:40:24
 */
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

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setTargetRoute(String targetRoute) {
        this.targetRoute = targetRoute;
    }

    public String getTargetRoute() {
        return targetRoute;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBytes(long requestBytes) {
        this.requestBytes = requestBytes;
    }

    public long getRequestBytes() {
        return requestBytes;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBytes(long responseBytes) {
        this.responseBytes = responseBytes;
    }

    public long getResponseBytes() {
        return responseBytes;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
