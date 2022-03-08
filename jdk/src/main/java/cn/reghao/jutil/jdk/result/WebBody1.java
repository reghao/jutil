package cn.reghao.jutil.jdk.result;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;

/**
 * HTTP 响应 body 数据
 *
 * @author reghao
 * @date 2022-01-07 14:05:35
 */
public class WebBody1<T> {
    private int code;
    private String msg;
    private String timestamp;
    private T data;

    private WebBody1(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> String success(T data) {
        WebBody1<T> webBody = new WebBody1<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String fail(T data) {
        WebBody1<T> webBody = new WebBody1<>(ResultStatus.FAIL.getCode(), ResultStatus.FAIL.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static <T> String error(T data) {
        WebBody1<T> webBody = new WebBody1<>(ResultStatus.ERROR.getCode(), ResultStatus.ERROR.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }
}
