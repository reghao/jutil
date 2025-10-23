package cn.reghao.jutil.jdk.string;

import java.util.regex.Pattern;

/**
 * @author reghao
 * @date 2023-02-20 13:53:17
 */
public class StringRegexp {
    private static final Pattern mobilePattern = Pattern.compile("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$");
    private static final Pattern emailPattern = Pattern.compile("^s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
    private static final Pattern ipv4Pattern = Pattern.compile("^((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))(\\.((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))){3}$ ");
    private static final Pattern passwordPattern = Pattern.compile("^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{10,32}$");

    public static boolean matchMobile(String str) {
        return mobilePattern.matcher(str).matches();
    }

    public static boolean matchEmail(String str) {
        return emailPattern.matcher(str).matches();
    }

    public static boolean matchIPv4Address(String str) {
        return ipv4Pattern.matcher(str).matches();
    }

    /**
     * 密码正则表达式, 密码必须包含大写字母, 数字, 特殊字符(四种里至少三种，且至少 10 个字符)
     *
     * @param
     * @return
     * @date 2025-10-23 11:46:10
     */
    public static boolean matchPassword(String str) {
        return passwordPattern.matcher(str).matches();
    }
}
