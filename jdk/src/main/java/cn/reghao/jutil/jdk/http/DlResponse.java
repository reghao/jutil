package cn.reghao.jutil.jdk.http;

import java.io.ByteArrayOutputStream;

/**
 * @author reghao
 * @date 2021-03-21 03:32:11
 */
public class DlResponse {
    private final int statusCode;
    // byte
    private final long contentLength;
    // ms
    private final long costTime;
    private final String contentRange;
    private final ByteArrayOutputStream result;

    public DlResponse(int statusCode, long contentLength, long costTime,
                      String contentRange, ByteArrayOutputStream result) {
        this.statusCode = statusCode;
        this.contentLength = contentLength;
        this.costTime = costTime;
        this.contentRange = contentRange;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getCostTime() {
        return costTime;
    }

    public String getContentRange() {
        return contentRange;
    }

    public ByteArrayOutputStream getResult() {
        return result;
    }
}
