package cn.reghao.jutil.tool.jwt;

/**
 * @author reghao
 * @date 2021-07-26 09:58:45
 */
public class JwtPayload {
    private final String userId;
    private final String roles;
    // 在何时过期
    private final Long expireIn;
    private final String signKey;

    public JwtPayload(String userId, String roles) {
        this.userId = userId;
        this.roles = roles;
        this.expireIn = System.currentTimeMillis() + 1000*3600*24*7;
        this.signKey = "tnb.cn";
    }

    public JwtPayload(String userId, String roles, long expireIn, String signKey) {
        this.userId = userId;
        this.roles = roles;
        this.expireIn = expireIn;
        this.signKey = signKey;
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
