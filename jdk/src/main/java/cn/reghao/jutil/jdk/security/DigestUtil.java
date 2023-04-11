package cn.reghao.jutil.jdk.security;

import cn.reghao.jutil.jdk.converter.ByteHex;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author reghao
 * @date 2022-04-27 16:37:07
 */
public class DigestUtil {
    public static byte[] md5sum(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    public static String md5sumStr(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        return ByteHex.bytes2Hex(messageDigest.digest());
    }

    public static String sha256sum(byte[] bytes) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes);
        return ByteHex.bytes2Hex(messageDigest.digest());
    }

    public static String sha256sum(InputStream in) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        // 16MiB
        int len = 1024*1024*16;
        byte[] buf = new byte[len];
        int readByes;
        while ((readByes = in.read(buf, 0, len)) != -1) {
            messageDigest.update(buf, 0, readByes);
        }

        return ByteHex.bytes2Hex(messageDigest.digest());
    }
}
