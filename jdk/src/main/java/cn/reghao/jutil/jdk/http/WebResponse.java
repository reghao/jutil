package cn.reghao.jutil.jdk.http;

import java.net.http.HttpResponse;

/**
 * HTTP 响应
 *
 * @author reghao
 * @date 2019-08-01 16:27:55
 */
public class WebResponse {
    private final int statusCode;
    private final String body;

    public WebResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static WebResponse of(HttpResponse<String> response) {
        return new WebResponse(response.statusCode(), response.body());
    }

    public static WebResponse error(String error) {
        return new WebResponse(600, error);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
