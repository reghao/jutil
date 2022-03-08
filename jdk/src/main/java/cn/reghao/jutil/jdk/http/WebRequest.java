package cn.reghao.jutil.jdk.http;

import java.util.Map;

/**
 * HTTP 请求
 *
 * @author reghao
 * @date 2019-08-01 16:27:55
 */
public interface WebRequest {
    int head(String url);
    WebResponse get(String url);
    WebResponse postFormData(String url, Map<String, String> formData);
    WebResponse postJson(String url, String json);
    WebResponse upload(String url, UploadParam uploadParam);
}
