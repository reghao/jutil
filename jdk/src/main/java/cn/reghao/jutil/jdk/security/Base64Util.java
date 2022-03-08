package cn.reghao.jutil.jdk.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author reghao
 * @date 2020-01-13 22:54:32
 */
public class Base64Util {
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String encode(String json) {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }
}
