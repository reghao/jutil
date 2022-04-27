package cn.reghao.jutil.tool.http;

import cn.reghao.jutil.jdk.http.WebResponse;
import cn.reghao.jutil.jdk.http.util.UrlFormatter;
import cn.reghao.jutil.jdk.http.util.UserAgents;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * 爬虫请求
 *
 * @author reghao
 * @date 2022-02-28 15:27:55
 */
public class JdkCrawlRequest {
    private static final Logger log = Logger.getLogger(JdkCrawlRequest.class.getName());

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    
    public int head(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .GET();
        builder.setHeader("User-Agent", UserAgents.getDesktopAgent());

        try {
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            log.info(MessageFormat.format("{0} 请求失败 -> {1}", url, e.getMessage()));
            return 600;
        }
    }

    public WebResponse get(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .GET();
        builder.setHeader("User-Agent", UserAgents.getDesktopAgent());

        try {
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String body = response.body();
            return new WebResponse(statusCode, body);
        } catch (Exception e) {
            log.info(MessageFormat.format("{0} 请求失败 -> {1}", url, e.getMessage()));
            return new WebResponse(600, e.getMessage());
        }
    }

    public void download(String url, String dir) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET();
        builder.setHeader("User-Agent", UserAgents.getDesktopAgent());
        try {
            HttpResponse<InputStream> in = client.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
            String filename = UrlFormatter.getFilename(url);
            File file = new File(dir + File.separator + filename);
            saveFile(in.body(), file);
        } catch (Exception e) {
            throw e;
        }
    }

    private void saveFile(InputStream in, File file) throws IOException {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            FileUtils.forceMkdir(parentDir);
        }

        FileOutputStream fos = new FileOutputStream(file);
        // 1MiB
        int len = 1024*1024;
        byte[] buf = new byte[len];
        int readLen;
        while ((readLen = in.read(buf, 0, len)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.close();
    }
}
