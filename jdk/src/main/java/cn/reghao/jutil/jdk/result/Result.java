package cn.reghao.jutil.jdk.result;

import java.io.Serializable;

import static cn.reghao.jutil.jdk.result.ResultStatus.*;


/**
 * 调用结果
 *
 * @author reghao
 * @date 2021-05-21 16:25:02
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private String data;
    private final long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Result result(ResultStatus resultStatus) {
        Result result = new Result();
        result.setCode(resultStatus.getCode());
        result.setMsg(resultStatus.getMsg());
        return result;
    }

    public static Result result(ResultStatus resultStatus, String msg) {
        Result result = new Result();
        result.setCode(resultStatus.getCode());
        result.setMsg(msg);
        return result;
    }

    public static Result success(String data) {
        Result result = new Result();
        result.setCode(SUCCESS.getCode());
        result.setMsg(SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static Result successWithMsg(String msg) {
        Result result = new Result();
        result.setCode(SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS.getCode());
        result.setMsg(SUCCESS.getMsg());
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode(FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(ERROR.getCode());
        result.setMsg(msg);
        return result;
    }
}
