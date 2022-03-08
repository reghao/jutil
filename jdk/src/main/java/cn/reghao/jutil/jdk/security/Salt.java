package cn.reghao.jutil.jdk.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author reghao
 * @date 2019-04-05 12:23:47
 */
public class Salt {
    private static SecureRandom random = new SecureRandom();

    /**
     * @return 盐值
     * @date 2019-04-05 12:28:08
     */
    public static String get(int len) {
        byte[] seed = random.generateSeed(len);
        return Base64.getEncoder().encodeToString(seed);
    }
}
