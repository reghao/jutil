package cn.reghao.jutil.jdk.result;

/**
 * 结果状态
 *
 * @author reghao
 * @date 2019-10-17 16:18:25
 */
public enum ResultStatus {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    ERROR(-1, "错误");

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
