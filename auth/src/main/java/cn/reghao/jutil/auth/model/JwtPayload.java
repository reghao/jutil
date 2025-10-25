package cn.reghao.jutil.auth.model;

/**
 * @author reghao
 * @date 2025-10-24 19:58:45
 */
public class JwtPayload {
    private final int plat;
    private final String loginId;
    private final long userId;
    private final int loginType;
    private final String authorities;
    private final String jti;

    public JwtPayload(int plat, String loginId, long userId, int loginType, String roles, String jti) {
        this.plat = plat;
        this.loginId = loginId;
        this.userId = userId;
        this.loginType = loginType;
        this.authorities = roles;
        this.jti = jti;
    }

    public Integer getPlat() {
        return plat;
    }

    public String getLoginId() {
        return loginId;
    }

    public long getUserId() {
        return userId;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public String getAuthorities() {
        return authorities;
    }

    public String getJti() {
        return jti;
    }
}
