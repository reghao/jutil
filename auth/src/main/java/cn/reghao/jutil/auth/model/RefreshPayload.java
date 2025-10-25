package cn.reghao.jutil.auth.model;

/**
 * @author reghao
 * @date 2023-02-17 15:24:30
 */
public class RefreshPayload {
    private long userId;
    private int plat;
    private String loginId;

    public RefreshPayload() {
    }

    public RefreshPayload(long userId, int plat, String loginId) {
        this.userId = userId;
        this.plat = plat;
        this.loginId = loginId;
    }

    public long getUserId() {
        return userId;
    }

    public int getPlat() {
        return plat;
    }

    public String getLoginId() {
        return loginId;
    }
}
