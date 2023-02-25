package cn.reghao.jutil.jdk.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-10-16 13:30:58
 */
public class WebClient implements WebRequest {
    private final HttpClient client = HttpClient.newHttpClient();
    private final int timeout = 30;

    @Override
    public int head(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public WebResponse get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeout))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return WebResponse.of(response);
        } catch (Exception e) {
            return WebResponse.error(e.getMessage());
        }
    }

    @Override
    public WebResponse postFormData(String url, Map<String, String> formData) {
        return null;
    }

    @Override
    public WebResponse postJson(String url, String json) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(timeout))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return WebResponse.of(response);
        } catch (Exception e) {
            return WebResponse.error(e.getMessage());
        }
    }

    @Override
    public WebResponse upload(String url, UploadParam uploadParam) {
        return null;
    }

    @Override
    public void download(String url, String dir) throws IOException {
    }
}
