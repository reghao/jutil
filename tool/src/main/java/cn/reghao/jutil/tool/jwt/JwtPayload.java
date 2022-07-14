package cn.reghao.jutil.tool.jwt;

import java.util.Date;

/**
 * @author reghao
 * @date 2021-07-26 09:58:45
 */
public class JwtPayload {
    private String userId;
    private String roles;
    private Date expiration;
    private String signKey;

    public JwtPayload(String userId, String roles) {
        this.userId = userId;
        this.roles = roles;
    }

    public JwtPayload(String userId, String roles, Date expiration) {
        this.userId = userId;
        this.roles = roles;
        this.expiration = expiration;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoles() {
        return roles;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getSignKey() {
        return signKey;
    }
}
