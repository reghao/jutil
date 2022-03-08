package cn.reghao.jutil.jdk.http;

/**
 * @author reghao
 * @date 2021-07-31 22:38:08
 */
public interface HttpDownloader {
    int head(String url);
    DlResponse download(String url);
    boolean download(String url, String dir);
}
