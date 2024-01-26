package cn.reghao.jutil.jdk.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author reghao
 * @date 2019-03-26 14:46:57
 */
public class Sha256Cryptor implements Cryptor {
    private MessageDigest sha256;

    public Sha256Cryptor() throws NoSuchAlgorithmException {
        this.sha256 = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public String encrypt(String str) {
        byte[] bytes = sha256.digest(str.getBytes());
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
