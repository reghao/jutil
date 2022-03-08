package cn.reghao.jutil.jdk.http.util;

/**
 * @author reghao
 * @date 2021-03-15 12:33:44
 */
public class UrlFormatter {
    /**
     * 从 http/https url 中提取出域名
     * https://twitter.com/Milkytutu/status/1264213127396114433 -> https://twitter.com
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
     * https://twitter.com/Milkytutu/status/1264213127396114433 -> twitter.com
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
     * https://cdn.91p07.com//m3u8/442614/442614.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818
     * -> https://cdn.91p07.com//m3u8/442614
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
     * https://cdn.91p07.com//m3u8/442614/442614.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818
     * -> https://cdn.91p07.com//m3u8/442614/442614.m3u8
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
     * http://91porn.com/uprofile.php?UID=b583fKPwaC8HuUDsIRbfEq9scGe1J90POTljHJwUJ998WRZq -> UID
     *
     * @param
     * @return
     * @date 2021-03-15 下午12:38
     */
    public static String getParamValue(String url, String name) {
        String params = url.toLowerCase().split("\\?")[1];
        String lname = name.toLowerCase();
        for (String kv : params.split("&")) {
            if (kv.startsWith(lname)) {
                return kv.split("=")[1];
            }
        }

        return null;
    }

    /**
     * https://video.twimg.com/ext_tw_video/1351933827732525056/pu/vid/3000/6000/320x544/RTM9oDMcLYnTfg_k.ts?name=value
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
     * https://cdn.91p07.com//m3u8/442614/442614.m3u8?st=xHX459guiAmx5CsRJbLXyQ&e=1615796818 ->
     * https://cdn.91p07.com//m3u8/442614/442614.m3u8
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
     * http://static.reghao.icu/tnb/vid/img/vidcover/eY9G0Zpnqe.jpg -> /tnb/vid/img/vidcover/eY9G0Zpnqe.jpg
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
