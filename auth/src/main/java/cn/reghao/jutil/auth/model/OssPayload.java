package cn.reghao.jutil.auth.model;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2023-08-23 10:26:01
 */
public class OssPayload implements Serializable {
    private static final long serialVersionUID = 1L;

    private int channelCode;
    private String action;
    private int userId;

    public OssPayload() {
    }

    public OssPayload(int channelCode, String action, int userId) {
        this.channelCode = channelCode;
        this.action = action;
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public int getChannelCode() {
        return channelCode;
    }

    public int getUserId() {
        return userId;
    }
}
