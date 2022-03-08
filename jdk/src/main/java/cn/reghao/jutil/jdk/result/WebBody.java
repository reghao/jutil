package cn.reghao.jutil.jdk.result;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;

/**
 * HTTP 响应 body 数据
 *
 * @author reghao
 * @date 2019-09-28 10:05:35
 */
public class WebBody {
    private int code;
    private String msg;
    private String timestamp;
    private Object data;

    private WebBody(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private void setData(Object data) {
        this.data = data;
    }

    public static String success() {
        WebBody webBody = new WebBody(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        return JsonConverter.objectToJson(webBody);
    }

    public static String successWithMsg(String msg) {
        WebBody webBody = new WebBody(ResultStatus.SUCCESS.getCode(), msg);
        webBody.setTimestamp(DateTimeConverter.now());
        return JsonConverter.objectToJson(webBody);
    }

    public static String result(Result result) {
        WebBody webBody = new WebBody(result.getCode(), result.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        return JsonConverter.objectToJson(webBody);
    }

    public static String success(Object data) {
        WebBody webBody = new WebBody(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static String fail(Object data) {
        WebBody webBody = new WebBody(ResultStatus.FAIL.getCode(), ResultStatus.FAIL.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static String failWithMsg(String msg) {
        WebBody webBody = new WebBody(ResultStatus.FAIL.getCode(), msg);
        webBody.setTimestamp(DateTimeConverter.now());
        return JsonConverter.objectToJson(webBody);
    }

    public static String error(Object data) {
        WebBody webBody = new WebBody(ResultStatus.ERROR.getCode(), ResultStatus.ERROR.getMsg());
        webBody.setTimestamp(DateTimeConverter.now());
        webBody.setData(data);
        return JsonConverter.objectToJson(webBody);
    }

    public static String errorMsg(String msg) {
        WebBody webBody = new WebBody(ResultStatus.ERROR.getCode(), msg);
        webBody.setTimestamp(DateTimeConverter.now());
        return JsonConverter.objectToJson(webBody);
    }
}
