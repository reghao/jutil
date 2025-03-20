package cn.reghao.jutil.jdk.security;

import cn.reghao.jutil.jdk.text.TextFile;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加密
 *
 * @author reghao
 * @date 2021-11-18 18:06:50
 */
public class RsaCryptor {
    private static final String ALGORITHM = "RSA";
    private static final Integer KEY_LENGTH = 2048;
    private static final String CHARSET = "utf-8";
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_LENGTH, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String privateKeyStr = new String(encoder.encode(privateKey.getEncoded()));
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyStr = new String(encoder.encode(publicKey.getEncoded()));

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("prikey", privateKeyStr);
        keyMap.put("pubkey", publicKeyStr);
        return keyMap;
    }

    public static Map<String, Object> getRsaPair(String baseDir) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_LENGTH, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String privateKeyStr = new String(encoder.encode(privateKey.getEncoded()));
        String privateKeyPath = String.format("%s/private.pem", baseDir);
        savePemFile(privateKeyStr, privateKeyPath);

        String publicKeyStr = new String(encoder.encode(publicKey.getEncoded()));
        String publicKeyPath = String.format("%s/public.pem", baseDir);
        savePemFile(publicKeyStr, publicKeyPath);

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("privateKey", privateKey);
        keyMap.put("publicKey", publicKey);
        return keyMap;
    }

    private static void savePemFile(String keyStr, String filePath) {
        try {
            textFile.write(new File(filePath), keyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static TextFile textFile = new TextFile();
    public static Map<String, Object> loadRsaPair(String baseDir) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPath = String.format("%s/public.pem", baseDir);
        String publicKeyStr = textFile.readFile(publicKeyPath);
        byte[] decoded = decoder.decode(publicKeyStr.getBytes(StandardCharsets.UTF_8));
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);

        String privateKeyPath = String.format("%s/private.pem", baseDir);
        String privateKeyStr = textFile.readFile(privateKeyPath);
        byte[] decoded1 = decoder.decode(privateKeyStr.getBytes(StandardCharsets.UTF_8));
        EncodedKeySpec encodedKeySpec1 = new PKCS8EncodedKeySpec(decoded1);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(encodedKeySpec1);

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("privateKey", rsaPrivateKey);
        keyMap.put("publicKey", rsaPublicKey);
        return keyMap;
    }

    public static RSAPublicKey getRSAPublicKey(File pemFile) {
        String publicKeyStr = textFile.readFile(pemFile.getAbsolutePath());
        return getRSAPublicKey(publicKeyStr);
    }

    public static RSAPublicKey getRSAPublicKey(String publicKeyStr) {
        byte[] decoded = decoder.decode(publicKeyStr.getBytes(StandardCharsets.UTF_8));
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
        try {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);
            return rsaPublicKey;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RSAPrivateKey getRSAPrivateKey(File pemFile) {
        String privateKeyStr = textFile.readFile(pemFile.getAbsolutePath());
        return getRSAPrivateKey(privateKeyStr);
    }

    public static RSAPrivateKey getRSAPrivateKey(String privateKeyStr) {
        byte[] decoded1 = decoder.decode(privateKeyStr.getBytes(StandardCharsets.UTF_8));
        EncodedKeySpec encodedKeySpec1 = new PKCS8EncodedKeySpec(decoded1);
        try {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(encodedKeySpec1);
            return rsaPrivateKey;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String encrypt(String pText, String publicKey) throws Exception {
        byte[] decoded = decoder.decode(publicKey.getBytes(StandardCharsets.UTF_8));
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cBytes = cipher.doFinal(pText.getBytes(CHARSET));
        return new String(encoder.encode(cBytes));
    }

    public static String decrypt(String cText, String privateKey) throws Exception {
        byte[] decoded = decoder.decode(privateKey);
        EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(decoded);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(encodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] cBytes = decoder.decode(cText);
        return new String(cipher.doFinal(cBytes));
    }
}
