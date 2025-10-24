package cn.reghao.jutil.jdk.model.jwt;

/**
 * @author reghao
 * @date 2021-07-26 09:58:45
 */
public class JwtPayload {
    private final Integer loginType;
    private final Integer plat;
    private final String userId;
    private final String roles;
    // 在何时过期
    private final Long expireAt;
    private final String signKey;

    public JwtPayload(Integer loginType, Integer plat, String userId, String roles, long expireAt, String signKey) {
        this.loginType = loginType;
        this.plat = plat;
        this.userId = userId;
        this.roles = roles;
        this.expireAt = expireAt;
        this.signKey = signKey;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public Integer getPlat() {
        return plat;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoles() {
        return roles;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public String getSignKey() {
        return signKey;
    }
}
