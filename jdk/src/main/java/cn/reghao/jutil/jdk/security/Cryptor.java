package cn.reghao.jutil.jdk.security;

/**
 * 加密/解密
 *
 * @author reghao
 * @date 2020-04-27 14:03:49
 */
public interface Cryptor {
    String encrypt(String str);
    String decrypt(String str);
}
