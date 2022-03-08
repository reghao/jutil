package cn.reghao.jutil.jdk.http.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User-Agent 池
 *
 * @author reghao
 * @date 2019-07-23 10:44:45
 */
public class UserAgents {
    private static List<String> mobileAgents = new ArrayList<>();
    private static List<String> desktopAgents = new ArrayList<>();

    static {
        mobileAgents.add("Mozilla/5.0 (Linux; U; Android 9; zh-CN; NX629J Build/PKQ1.190321.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.9.1.1071 Mobile Safari/537.36");
        mobileAgents.add("Mozilla/5.0 (Linux; U; Android 9; zh-cn; Redmi Note 8 Pro Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.141 Mobile Safari/537.36 XiaoMi/MiuiBrowser/11.10.8");
        mobileAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/13.0 MQQBrowser/8.9.1 Mobile/15B87 Safari/604.1 MttCustomUA/2 QBWebViewType/1 WKType/1");

        desktopAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169");
        desktopAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0Safari/537.36");
        desktopAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
    }

    public static String getMobileAgent() {
        int len = mobileAgents.size();
        Random rand = new Random();
        int index = rand.nextInt(len);
        return mobileAgents.get(index);
    }

    public static String getDesktopAgent() {
        int len = desktopAgents.size();
        Random rand = new Random();
        int index = rand.nextInt(len);
        return desktopAgents.get(0);
    }
}
