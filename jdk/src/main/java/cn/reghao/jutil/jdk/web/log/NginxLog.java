package cn.reghao.jutil.jdk.web.log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2023-11-07 14:58:07
 */
public class NginxLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    @SerializedName("time_iso8601") private String timeIso8601;
    private Long requestTimestamp;
    @SerializedName("remote_addr") private String remoteAddr;
    private String request;
    private Integer status;
    @SerializedName("request_method") private String requestMethod;
    @SerializedName("body_bytes_sent") private Integer bodyBytesSent;
    @SerializedName("request_time") private Double requestTime;
    @SerializedName("upstream_response_time") private String upstreamResponseTime;
    private String upstreamResponseTimeDouble;
    @SerializedName("upstream_addr") private String upstreamAddr;
    private String host;
    private String url;
    @SerializedName("http_x_forwarded_for") private String httpXForwardedFor;
    @SerializedName("http_referer") private String httpReferer;
    @SerializedName("http_user_agent") private String httpUserAgent;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTimeIso8601() {
        return timeIso8601;
    }

    public void setRequestTimestamp(Long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public Long getRequestTimestamp() {
        return requestTimestamp;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getRequest() {
        return request;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public Integer getBodyBytesSent() {
        return bodyBytesSent;
    }

    public Double getRequestTime() {
        return requestTime;
    }

    public String getUpstreamResponseTime() {
        return upstreamResponseTime;
    }

    public String getUpstreamResponseTimeDouble() {
        return upstreamResponseTimeDouble;
    }

    public String getUpstreamAddr() {
        return upstreamAddr;
    }

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpXForwardedFor() {
        return httpXForwardedFor;
    }

    public String getHttpReferer() {
        return httpReferer;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }
}
