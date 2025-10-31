package cn.reghao.jutil.jdk.web.result;

/**
 * @author reghao
 * @date 2021-11-08 16:53:59
 */
public enum NotAvailable {
    na("N/A");

    private String desc;

    NotAvailable(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
