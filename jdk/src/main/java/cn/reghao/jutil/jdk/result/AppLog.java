package cn.reghao.jutil.jdk.result;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2025-07-18 11:33:46
 */
public class AppLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String app;
    private String host;
    private long timestamp;
    private String level;
    private String thread;
    private String logger;
    private String message;

    public AppLog() {
    }

    public AppLog(String requestId, String app, String host, long timestamp, String level, String thread, String logger, String message) {
        this.requestId = requestId;
        this.app = app;
        this.host = host;
        this.timestamp = timestamp;
        this.level = level;
        this.thread = thread;
        this.logger = logger;
        this.message = message;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getApp() {
        return app;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getThread() {
        return thread;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLogger() {
        return logger;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
