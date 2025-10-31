package cn.reghao.jutil.jdk.security.crypto;

import cn.reghao.jutil.jdk.io.TextFile;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 非对称加密
 *
 * @author reghao
 * @date 2022-01-16 00:21:15
 */
public class RsaEncrypt {
    private static final String ALGORITHM = "RSA";
    private static final Integer KEY_LENGTH = 2048;
    private static final String CHARSET = "utf-8";

    private static Map<String, String> genKeyPair() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_LENGTH, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        String prikey = "rsa.prikey";
        String pubkey = "rsa.pubkey";
        TextFile textFile = new TextFile();
        textFile.write(new File(prikey), privateKeyStr);
        textFile.write(new File(pubkey), publicKeyStr);
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("privateKey", privateKeyStr);
        keyMap.put("publicKey", publicKeyStr);
        return keyMap;
    }

    public static String encrypt(String pText, String publicKey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cBytes = cipher.doFinal(pText.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(cBytes);
    }

    public static String encrypt(byte[] pText, String publicKey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cBytes = cipher.doFinal(pText);
        return Base64.getEncoder().encodeToString(cBytes);
    }

    public static String decrypt(String cText, String privateKey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(decoded);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] cBytes = Base64.getDecoder().decode(cText);
        return new String(cipher.doFinal(cBytes));
    }

    public static String decrypt(byte[] cText, String privateKey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(decoded);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] cBytes = Base64.getDecoder().decode(cText);
        return new String(cipher.doFinal(cBytes));
    }
}
