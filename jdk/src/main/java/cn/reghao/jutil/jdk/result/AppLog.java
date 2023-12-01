package cn.reghao.jutil.jdk.result;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2023-06-03 15:37:49
 */
public class AppLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String app;
    private final String host;
    private final long timestamp;
    private String dateTimeStr;
    private final String level;
    private final String thread;
    private final String logger;
    private final String message;

    public AppLog(String app, String host, long timestamp, String level, String thread, String logger, String message) {
        this.app = app;
        this.host = host;
        this.timestamp = timestamp;
        this.level = level;
        this.thread = thread;
        this.logger = logger;
        this.message = message;
    }

    public String getApp() {
        return app;
    }

    public String getHost() {
        return host;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public String getLevel() {
        return level;
    }

    public String getThread() {
        return thread;
    }

    public String getLogger() {
        return logger;
    }

    public String getMessage() {
        return message;
    }
}
