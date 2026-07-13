package cn.reghao.jutil.jdk.http;

import cn.reghao.jutil.jdk.http.util.UserAgents;

import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-10-16 13:30:58
 */
public class WebClient implements WebRequest {
    private final HttpClient client;
    private final int timeout = 60;

    public WebClient() {
        this.client = HttpClient.newHttpClient();
    }

    public WebClient(String cookie, String domain) {
        CookieManager cookieManager = getCookieManager(cookie, domain);
        this.client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
    }

    private CookieManager getCookieManager(String cookieText, String domain) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        if (cookieText == null || cookieText.isBlank()) {
            return cookieManager;
        }

        // 1. 修复：必须使用 replaceAll 才能用正则去除所有空格
        String cleanCookie = cookieText.replaceAll("\\s+", "");

        // 2. 按照分号切割
        String[] pairs = cleanCookie.split(";");
        for (String pair : pairs) {
            if (pair.isBlank()) continue; // 跳过空片段

            // 3. 修复：限制 split 次数，并做防御性校验，防止特殊 Cookie 导致越界
            String[] strs = pair.split("=", 2);
            String name = strs[0];
            // 如果只有键没有值（比如 "flag="），给个空字符串，防止 strs[1] 崩溃
            String value = (strs.length > 1) ? strs[1] : "";

            HttpCookie myCookie = new HttpCookie(name, value);
            myCookie.setDomain(domain);
            myCookie.setPath("/");

            // 传入 null 在指定了 domain 的情况下可以正常工作
            cookieManager.getCookieStore().add(null, myCookie);
        }

        return cookieManager;
    }

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
                .header("User-Agent", UserAgents.getDesktopAgent())
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
    public WebResponse upload(String url, UploadParam uploadParam, String token) {
        return null;
    }

    @Override
    public void download(String url, String referer, String savedPath) {
        try {
            // 1. 构建请求，并注入防盗链 Referer
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();

            // 如果传入了 Referer，则带上（B 站的资源服务器如 akamaized.net、bilivideo.com 强校验此请求头）
            if (referer != null && !referer.isBlank()) {
                requestBuilder.header("Referer", referer);
            }

            // 建议加上通用的浏览器 User-Agent，防止被常规防火墙拦截
            requestBuilder.header("User-Agent", UserAgents.getDesktopAgent());

            HttpRequest request = requestBuilder.build();
            // 2. 发送请求，注意：这里使用 ofInputStream() 开启流式下载
            HttpResponse<InputStream> response = this.client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 3. 检查状态码
            int statusCode = response.statusCode();
            if (statusCode >= 300) {
                System.err.printf("下载失败，状态码: %d, URL: %s%n", statusCode, url);
                return;
            }

            // 4. 处理保存路径，自动创建不存在的父目录
            Path targetPath = Paths.get(savedPath);
            if (targetPath.getParent() != null) {
                Files.createDirectories(targetPath.getParent());
            }

            // 5. 核心：将输入流直接复制到目标文件（REPLACE_EXISTING 表示如果文件存在则覆盖）
            try (InputStream inputStream = response.body()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("下载完成: " + savedPath);
        } catch (Exception e) {
            System.err.println("下载过程中发生异常: " + e.getMessage());
        }
    }
}
