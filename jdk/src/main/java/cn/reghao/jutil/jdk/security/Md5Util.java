package cn.reghao.jutil.jdk.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author reghao
 * @date 2020-01-15 17:09:07
 */
public class Md5Util {
    public static String md5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());

        StringBuilder sb = new StringBuilder();
        byte[] bytes = md.digest();
        for (int i = 0; i < bytes.length; i++) {
            int a = bytes[i];
            if (a < 0) {
                a += 256;
            }

            if (a < 16) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(a));
        }

        return sb.toString();
    }
}
