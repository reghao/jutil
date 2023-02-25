package cn.reghao.jutil.jdk.result;

import cn.reghao.jutil.jdk.serializer.JsonConverter;

/**
 * HTTP 响应 body 数据
 *
 * @author reghao
 * @date 2022-01-07 14:05:35
 */
public class WebResult<T> {
    private final int code;
    private final String msg;
    private final long timestamp;
    private final String requestId;
    private T data;

    private WebResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
        this.requestId = "";
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    private void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> String success() {
        WebResult<T> webBody = new WebResult<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String successWithMsg(String msg) {
        WebResult<T> webBody = new WebResult<>(ResultStatus.SUCCESS.getCode(), msg);
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String success(T data) {
        WebResult<T> webBody = new WebResult<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String fail() {
        WebResult<T> webBody = new WebResult<>(ResultStatus.FAIL.getCode(), ResultStatus.FAIL.getMsg());
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String failWithMsg(String msg) {
        WebResult<T> webBody = new WebResult<>(ResultStatus.FAIL.getCode(), msg);
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String error() {
        WebResult<T> webBody = new WebResult<>(ResultStatus.ERROR.getCode(), ResultStatus.ERROR.getMsg());
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String errorWithMsg(String msg) {
        WebResult<T> webBody = new WebResult<>(ResultStatus.ERROR.getCode(), msg);
        return JsonConverter.objectToJson(webBody);
    }
}
