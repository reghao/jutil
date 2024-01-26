package cn.reghao.jutil.tool.http.util;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author reghao
 * @date 2021-03-23 22:22:35
 */
public class MyRetryHandler implements HttpRequestRetryHandler {
    private final int MAX_FAIL_RETRY_COUNT = 3;

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        String uri = request.getRequestLine().getUri();

        if (executionCount >= MAX_FAIL_RETRY_COUNT) {
            System.out.format("%s-%s重试次数大于等于3次%n", uri, executionCount);
            return false;
        }

        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            // 如果请求被认为是幂等的，则重试
            System.out.format("幂等接口重试：%s,次数：%s%n", uri, executionCount);
            return true;
        }

        //NoHttpResponseException 重试
        if (exception instanceof NoHttpResponseException) {
            System.out.format("NoHttpResponseException 异常重试，接口：%s,次数：%s%n ", uri, executionCount);
            return true;
        }

        //连接超时重试
        if (exception instanceof ConnectTimeoutException) {
            System.out.format("ConnectTimeoutException异常重试 ，接口：%s,次数：%s%n", uri, executionCount);
            return true;
        }

        // 响应超时不重试，避免造成业务数据不一致
        if (exception instanceof SocketTimeoutException) {
            return false;
        }

        if (exception instanceof InterruptedIOException) {
            // 超时
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // 未知主机
            return false;
        }

        if (exception instanceof SSLException) {
            // SSL handshake exception
            return false;
        }

        return false;
    }
}
