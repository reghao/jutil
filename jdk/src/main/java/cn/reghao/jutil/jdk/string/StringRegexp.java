package cn.reghao.jutil.jdk.string;

import java.util.regex.Pattern;

/**
 * @author reghao
 * @date 2023-02-20 13:53:17
 */
public class StringRegexp {
    private static final Pattern mobilePattern = Pattern.compile("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$");
    private static final Pattern emailPattern1 = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+. [a-zA-Z]{2,}$");
    private static final Pattern ipv4Pattern = Pattern.compile("^((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))(\\.((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))){3}$ ");

    public static boolean matchMobile(String str) {
        return mobilePattern.matcher(str).matches();
    }

    public static boolean matchEmail(String str) {
        return emailPattern.matcher(str).matches();
    }

    public static boolean matchIPv4Address(String str) {
        return ipv4Pattern.matcher(str).matches();
    }
}
