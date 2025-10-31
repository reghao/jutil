package cn.reghao.jutil.jdk.web.result;

/**
 * 结果状态
 *
 * @author reghao
 * @date 2019-10-17 16:18:25
 */
public enum ResultStatus {
    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    NOTFOUND(2, "notfound"),
    ERROR(-1, "error");

    private int code;
    private String msg;

    ResultStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
