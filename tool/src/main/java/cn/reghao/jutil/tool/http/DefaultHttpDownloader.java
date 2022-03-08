package cn.reghao.jutil.tool.http;

import cn.reghao.jutil.jdk.http.DlResponse;
import cn.reghao.jutil.jdk.http.HttpDownloader;
import cn.reghao.jutil.jdk.http.util.UrlFormatter;
import cn.reghao.jutil.jdk.http.util.UserAgents;
import cn.reghao.jutil.jdk.http.proxy.RequestProxy;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author reghao
 * @date 2019-11-29 10:03:18
 */
public class DefaultHttpDownloader extends BaseWebRequest implements HttpDownloader {
    public DefaultHttpDownloader() {
    }

    public DefaultHttpDownloader(boolean enableProxy) {
        super("utf8", enableProxy);
    }

    @Override
    public int head(String url) {
        HttpHead head = new HttpHead(url);
        try (CloseableHttpResponse response = client.execute(head)) {
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            System.out.format("%s head 请求失败 -> %s%n", url, e.getMessage());
        }

        // 资源无法访问
        return 600;
    }

    @Override
    public DlResponse download(String url) {
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", UserAgents.getDesktopAgent());
        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = client.execute(get)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return dlResponse(response, statusCode, start);
            } else if (statusCode == 206) {
                return dlResponse(response, statusCode, start);
            } else if (statusCode == 302) {
                // 请求重定向
                String location = response.getFirstHeader("Location").getValue();
            } else if (statusCode == 404) {
                System.out.format("%s 资源不存在", url);
                return new DlResponse(statusCode, 0, 0, null, null);
            }
        } catch (IOException e) {
            System.out.format("%s 下载失败 -> %s%n", url, e.getMessage());
        }
        return null;
    }

    private DlResponse dlResponse(HttpResponse response, int statusCode, long start) throws IOException {
        Header header = response.getFirstHeader("Content-Length");
        long contentLength;
        if (header == null) {
            contentLength = 0;
        } else {
            contentLength = Long.parseLong(header.getValue());
        }

        Header contentRangeHeader = response.getFirstHeader("Content-Range");
        String contentRange = null;
        if (contentRangeHeader != null) {
            contentRange = contentRangeHeader.getValue();
        }

        HttpEntity entity = response.getEntity();
        int avail = entity.getContent().available();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        entity.writeTo(byteArrayOutputStream);
        long costTime = System.currentTimeMillis()-start;
        return new DlResponse(statusCode, contentLength, costTime, contentRange, byteArrayOutputStream);
    }

    @Override
    public boolean download(String url, String dir) {
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", UserAgents.getDesktopAgent());
        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = client.execute(get)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = response.getEntity();
                String contentType = httpEntity.getContentType().getValue();
                String filename = UrlFormatter.getFilename(url);
                File file = new File(dir + File.separator + filename);
                FileOutputStream fout = new FileOutputStream(file);
                // 持续写到本地文件，直到服务器没有数据
                httpEntity.writeTo(fout);
                return true;
            }
        } catch (IOException e) {
            System.out.format("%s 下载失败 -> %s%n", url, e.getMessage());
        }
        return false;
    }

    public boolean download(String url, RequestProxy requestProxy, String dir) {
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", UserAgents.getDesktopAgent());
        HttpContext context = httpContext(Collections.emptyList(), requestProxy);
        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = client.execute(get, context)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = response.getEntity();
                String contentType = httpEntity.getContentType().getValue();
                String filename = UrlFormatter.getFilename(url);
                File file = new File(dir + File.separator + filename);
                FileOutputStream fout = new FileOutputStream(file);
                // 持续写到本地文件，直到服务器没有数据
                httpEntity.writeTo(fout);
                return true;
            }
        } catch (IOException e) {
            System.out.format("%s 下载失败 -> %s%n", url, e.getMessage());
        }
        return false;
    }

    /**
     * 将字节数组流保存到文件
     *
     * @param
     * @return
     * @date 2021-03-15 下午10:13
     */
    private void saveFile(ByteArrayOutputStream byteArrayOutputStream, File file) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(byteArrayOutputStream.toByteArray());
        fout.flush();
        fout.close();
    }

    public long acceptRanges(String url) throws IOException {
        HttpHead httpHead = new HttpHead(url);
        HttpResponse response = client.execute(httpHead);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            if (response.getFirstHeader("Accept-Ranges") != null) {
                Header contentLengthHeader = response.getFirstHeader("Content-Length");
                return Long.parseLong(contentLengthHeader.getValue());
            }
        }

        return 0;
    }

    public List<String> splitContent(long contentLength) {
        // 100KiB
        long maxFragment = 1024*100;
        // 10MiB
        //long maxFragment = 1024*1024*10;
        List<String> list = new ArrayList<>();
        if (contentLength < maxFragment) {
            list.add("bytes=0-" + (contentLength - 1));
        } else {
            long i = 0;
            for (;i + maxFragment < contentLength; i += maxFragment) {
                list.add("bytes=" + i + "-" + (i+maxFragment-1));
            }
            list.add("bytes=" + i + "-" + (contentLength-1));
        }
        return list;
    }

    public DlResponse download(HttpGet httpGet) {
        httpGet.setHeader("User-Agent", UserAgents.getDesktopAgent());
        HttpContext context = httpContext(new ArrayList<>(), null);

        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = client.execute(httpGet, context)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return dlResponse(response, statusCode, start);
            } else if (statusCode == 206) {
                return dlResponse(response, statusCode, start);
            } else if (statusCode == 302) {
                //
                String location = response.getFirstHeader("Location").getValue();
            } else if (statusCode == 404) {
                System.out.format("资源下载失败 -> %s%n", httpGet.getURI());
                return new DlResponse(statusCode, 0, 0, null, null);
            }
        } catch (Exception e) {
            System.out.format("资源下载失败 -> %s%n", httpGet.getURI());
            e.printStackTrace();
        }
        return null;
    }
}
