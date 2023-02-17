package cn.reghao.jutil.jdk.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 随机字符串
 *
 * @author reghao
 * @date 2023-02-17 09:42:56
 */
public class RandomString {
    static final SecureRandom sr = new SecureRandom();
    static final String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUTWXYZ";
    static String num = "0123456789";

    /**
     * 获取盐
     *
     * @param len 长度
     * @return Base64 编码的字符串
     * @date 2023-02-17 09:44:02
     */
    public static String getSalt(int len) {
        byte[] seed = sr.generateSeed(len);
        return Base64.getEncoder().encodeToString(seed);
    }

    public static String getNumber(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = sr.nextInt(num.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getString(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = sr.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
