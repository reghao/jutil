package cn.reghao.jutil.jdk.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
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
