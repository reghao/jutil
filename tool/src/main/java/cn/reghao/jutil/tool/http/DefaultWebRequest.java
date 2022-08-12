package cn.reghao.jutil.tool.http;

import cn.reghao.jutil.jdk.http.UploadParam;
import cn.reghao.jutil.jdk.http.WebRequest;
import cn.reghao.jutil.jdk.http.WebResponse;
import cn.reghao.jutil.jdk.http.util.UserAgents;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author reghao
 * @date 2019-11-29 10:03:18
 */
public class DefaultWebRequest extends BaseWebRequest implements WebRequest {
    private Map<String, String> headers;

    public DefaultWebRequest() {
        super();
    }

    public DefaultWebRequest(Map<String, String> headers) {
        super();
        this.headers = headers;
    }

    @Override
    public int head(String url) {
        HttpHead head = new HttpHead(url);
        WebResponse webResponse = execRequest(head);
        return webResponse.getStatusCode();
    }

    @Override
    public WebResponse get(String url) {
        HttpGet get = new HttpGet(url);
        return execRequest(get);
    }

    @Override
    public WebResponse postFormData(String url, Map<String, String> formData) {
        List<NameValuePair> params = new ArrayList<>();
        formData.forEach((k, v) -> {
            params.add(new BasicNameValuePair(k, v));
        });
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);

        HttpPost post = new HttpPost(url);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.setEntity(urlEncodedFormEntity);
        return execRequest(post);
    }

    public WebResponse postFormData1(String url, Map<String, String> formData, String token) {
        List<NameValuePair> params = new ArrayList<>();
        formData.forEach((k, v) -> {
            params.add(new BasicNameValuePair(k, v));
        });
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);

        HttpPost post = new HttpPost(url);
        post.addHeader("Authorization", "Bearer " + token);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.setEntity(urlEncodedFormEntity);
        return execRequest(post);
    }

    @Override
    public WebResponse postJson(String url, String json) {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");

        HttpPost post = new HttpPost(url);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(entity);
        return execRequest(post);
    }

    public WebResponse postJson1(String url, String json, String token) {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");

        HttpPost post = new HttpPost(url);
        post.addHeader("Authorization", "Bearer " + token);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(entity);
        return execRequest(post);
    }

    @Override
    public WebResponse upload(String url, UploadParam uploadParam) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(StandardCharsets.UTF_8);

        ContentBody contentBody;
        if (uploadParam.getFile() != null) {
            contentBody = new FileBody(uploadParam.getFile(), uploadParam.getMimeType());
        } else if (uploadParam.getBytes() != null) {
            contentBody = new ByteArrayBody(uploadParam.getBytes(), uploadParam.getMimeType());
        } else {
            return new WebResponse(600, "not data in UploadParam");
        }

        builder.addPart("file", contentBody);
        Map<String, String> map = uploadParam.getTextParams();
        if (map != null) {
            map.forEach(builder::addTextBody);
        }

        HttpPost post = new HttpPost(url);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.setEntity(builder.build());
        try (CloseableHttpResponse response = client.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);;
            return new WebResponse(statusCode, body);
        } catch (Exception e) {
            return new WebResponse(600, e.getMessage());
        }
    }

    public WebResponse upload1(String url, UploadParam uploadParam, String token) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(StandardCharsets.UTF_8);

        ContentBody contentBody;
        if (uploadParam.getFile() != null) {
            contentBody = new FileBody(uploadParam.getFile(), uploadParam.getMimeType());
        } else if (uploadParam.getBytes() != null) {
            contentBody = new ByteArrayBody(uploadParam.getBytes(), uploadParam.getMimeType());
        } else {
            return new WebResponse(600, "not data in UploadParam");
        }

        builder.addPart("file", contentBody);
        Map<String, String> map = uploadParam.getTextParams();
        if (map != null) {
            map.forEach(builder::addTextBody);
        }

        HttpPost post = new HttpPost(url);
        post.addHeader("Authorization", "Bearer " + token);
        if (headers != null) {
            headers.forEach(post::addHeader);
        }
        post.setEntity(builder.build());
        try (CloseableHttpResponse response = client.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);;
            return new WebResponse(statusCode, body);
        } catch (Exception e) {
            return new WebResponse(600, e.getMessage());
        }
    }

    private WebResponse execRequest(HttpRequestBase request) {
        request.setHeader("User-Agent", UserAgents.getDesktopAgent());
        try (CloseableHttpResponse response = client.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            //String body = EntityUtils.toString(response.getEntity(), charset);
            String body = EntityUtils.toString(response.getEntity(), charset);
            return new WebResponse(statusCode, body);
        } catch (Exception e) {
            // TODO 是否应该放在 finally 块中？
            return new WebResponse(600, e.getMessage());
        }
    }

    public void download(String url, String filePath) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", UserAgents.getDesktopAgent());
        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = client.execute(get)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = response.getEntity();
                String contentType = httpEntity.getContentType().getValue();
                File file = new File(filePath);
                FileOutputStream fout = new FileOutputStream(file);
                // 持续写到本地文件，直到服务器没有数据
                httpEntity.writeTo(fout);
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
