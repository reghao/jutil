package cn.reghao.jutil.jdk.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author reghao
 * @date 2019-03-26 14:46:57
 */
public class Md5Cryptor implements Cryptor {
    private MessageDigest md5;

    public Md5Cryptor() throws NoSuchAlgorithmException {
        this.md5 = MessageDigest.getInstance("MD5");
    }

    @Override
    public String encrypt(String str) {
        byte[] bytes = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String str) {
        return null;
    }
}
