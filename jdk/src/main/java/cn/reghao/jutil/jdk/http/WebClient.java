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
    @Override
    public int head(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
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
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
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
