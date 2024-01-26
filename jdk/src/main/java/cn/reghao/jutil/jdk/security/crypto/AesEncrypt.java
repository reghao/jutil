package cn.reghao.jutil.jdk.security.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 对称加密
 *
 * @author reghao
 * @date 2022-01-16 00:21:15
 */
public class AesEncrypt {
    private static final String ALGORITHM = "AES";
    private static final Integer KEY_LENGTH = 128;
    private static final String CHARSET = "utf-8";

    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));

        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_LENGTH, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encFormat = secretKey.getEncoded();
        return new SecretKeySpec(encFormat, ALGORITHM);
    }

    public static String encrypt(String pText, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] pBytes = pText.getBytes(CHARSET);
        //IvParameterSpec iv = new IvParameterSpec(password.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cBytes = cipher.doFinal(pBytes);
        return bytes2Hex(cBytes);
    }

    public static byte[] encrypt(byte[] pBytes, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //IvParameterSpec iv = new IvParameterSpec(password.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cBytes = cipher.doFinal(pBytes);
        return cBytes;
    }

    private static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xff);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    public static String decrypt(String cText, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //IvParameterSpec iv = new IvParameterSpec(password.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] cBytes = hex2Bytes(cText);
        byte[] pBytes = cipher.doFinal(cBytes);
        return new String(pBytes, CHARSET);
    }

    public static byte[] decrypt(byte[] cBytes, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //IvParameterSpec iv = new IvParameterSpec(password.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] pBytes = cipher.doFinal(cBytes);
        return pBytes;
    }

    private static byte[] hex2Bytes(String hex) {
        byte[] bytes = new byte[hex.length()/2];
        for (int i = 0;i < hex.length()/2; i++) {
            int high = Integer.parseInt(hex.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hex.substring(i*2+1, i*2+2), 16);
            bytes[i] = (byte) (high * 16 + low);
        }
        return bytes;
    }
}
