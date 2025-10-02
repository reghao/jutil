package cn.reghao.jutil.web.log;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2023-11-07 14:58:07
 */
@Getter
public class NginxLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("time_iso8601") private String timeIso8601;
    @SerializedName("remote_addr") private String remoteAddr;
    private String request;
    private Integer status;
    @SerializedName("request_method") private String requestMethod;
    @SerializedName("body_bytes_sent") private Integer bodyBytesSent;
    @SerializedName("request_time") private Double requestTime;
    @SerializedName("upstream_response_time") private String upstreamResponseTime;
    @SerializedName("upstream_addr") private String upstreamAddr;
    private String host;
    private String url;
    @SerializedName("http_x_forwarded_for") private String httpXForwardedFor;
    @SerializedName("http_referer") private String httpReferer;
    @SerializedName("http_user_agent") private String httpUserAgent;
}
