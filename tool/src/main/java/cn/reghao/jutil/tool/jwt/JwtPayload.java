package cn.reghao.jutil.tool.jwt;

import java.util.Date;

/**
 * @author reghao
 * @date 2021-07-26 09:58:45
 */
public class JwtPayload {
    private String userId;
    private String roles;
    private Long expireIn;
    private String signKey;

    public JwtPayload(String userId, String roles) {
        this.userId = userId;
        this.roles = roles;
        this.expireIn = System.currentTimeMillis() + 1000*3600*24*7;
    }

    public JwtPayload(String userId, String roles, long expireIn) {
        this.userId = userId;
        this.roles = roles;
        this.expireIn = expireIn;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoles() {
        return roles;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public String getSignKey() {
        return signKey;
    }
}
