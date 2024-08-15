package cn.reghao.jutil.jdk.http.util;

/**
 * @author reghao
 * @date 2021-03-15 12:33:44
 */
public class UrlFormatter {
    /**
     * 从 http/https url 中提取出域名
     * https://rehgao.cn/hao/status/1264213127396114433 -> https://reghao.cn
     *
     * @param
     * @return
     * @date 2021-03-15 下午12:13
     */
    public static String getDomain(String url) {
        if (url.startsWith("https://")) {
            String host = url.split("https://")[1].split("/")[0];
            return "https://" + host;
        } else {
            String host = url.split("http://")[1].split("/")[0];
            return "http://" + host;
        }
    }

    /**
     * 从 http/https url 中提取出主机
     * https://rehgao.cn/hao/status/1264213127396114433 -> reghao.cn
     *
     * @param
     * @return
     * @date 2021-03-15 下午12:13
     */
    public static String getHost(String url) {
        if (url.startsWith("https://")) {
            return url.split("https://")[1].split("/")[0];
        } else {
            return url.split("http://")[1].split("/")[0];
        }
    }

    /**
     * https://cdn.reghao.cn//m3u8/442/442.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818
     * -> https://cdn.reghao.cn//m3u8/442
     *
     * @param
     * @return
     * @date 2021-03-15 下午2:31
     */
    public static String getPrefix(String url) {
        String noParamsUrl = url.split("\\?")[0];
        return noParamsUrl.substring(0, noParamsUrl.lastIndexOf("/"));
    }

    /**
     * https://cdn.reghao.cn//m3u8/442/442.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818
     * -> https://cdn.reghao.cn//m3u8/442/442.m3u8
     *
     * @param
     * @return
     * @date 2021-03-15 下午2:33
     */
    public static String getUrlWithNoParams(String url) {
        return url.split("\\?")[0];
    }

    /**
     * 从 url 中取出指定参数的值
     * http://reghao.cn/user/profile?uid=b583fKPwaC8HuUDsIRbfEq9scGe1J90POTljHJwUJ998WRZq -> uid
     *
     * @param
     * @return
     * @date 2021-03-15 下午12:38
     */
    public static String getParamValue(String url, String name) {
        String params = url.split("\\?")[1];
        for (String kv : params.split("&")) {
            String[] arr = kv.split("=");
            if (arr.length != 2) {
                return null;
            }

            if (name.equals(arr[0])) {
                return arr[1];
            }
        }

        return null;
    }

    /**
     * https://oss.reghao.cn/video/m3u8/3000/6000/320x544/RTM9oDMcLYnTfg_k.ts?name=value
     * -> RTM9oDMcLYnTfg_k.ts
     *
     * @param
     * @return
     * @date 2021-03-15 下午9:36
     */
    public static String getFilename(String url) {
        String noParamsUrl = url.split("\\?")[0];
        int index = noParamsUrl.lastIndexOf("/");
        return noParamsUrl.substring(index+1);
    }

    /**
     * https://cdn.reghao.cn//m3u8/442/442.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818 ->
     * https://cdn.reghao.cn//m3u8/442/442.m3u8
     *
     * @param
     * @return
     * @date 2021-11-26 上午11:17
     */
    public static String getUrlWithoutParam(String url) {
        return url.split("\\?")[0];
    }

    /**
     * 提取 uri
     * http://oss.reghao.cn/tnb/vid/img/vidcover/eY9G0Zpnqe.jpg -> /tnb/vid/img/vidcover/eY9G0Zpnqe.jpg
     *
     * @param
     * @return
     * @date 2021-11-24 上午9:47
     */
    public static String getUri(String url) {
        String url1 = url.replaceFirst("^(http|https)://", "");
        int idx = url1.indexOf("/");
        return url1.substring(idx);
    }
}
