package com.yusute.common.codec.rsa;

import com.yusute.common.codec.base64.YusuteBase64;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.StringUtils;

/**
 * @author yusutehot
 */
public class YusuteRSA {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public static final int KEY_PAIR_SIZE = 1024;

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p> 生成密钥对(公钥和私钥) </p>
     */
    public static RSAKeyPair genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_PAIR_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKeyPair(publicKey, privateKey);
    }

    /**
     * <p> 用私钥对信息生成数字签名 </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static String signature(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = YusuteBase64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        byte[] encode = YusuteBase64.encode(signature.sign());
        return StringUtils.newStringUtf8(encode);
    }

    /**
     * <p> 校验数字签名 </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
        throws Exception {
        byte[] keyBytes = YusuteBase64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(YusuteBase64.decode(sign));
    }

    /**
     * <P> 私钥解密 </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
        throws Exception {
        byte[] keyBytes = YusuteBase64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        return getDecryptBytes(cipher, encryptedData);
    }

    /**
     * <p> 公钥解密 </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
        throws Exception {
        byte[] keyBytes = YusuteBase64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);

        return getDecryptBytes(cipher, encryptedData);
    }

    private static byte[] getDecryptBytes(Cipher cipher, byte[] encrypt) throws Exception{
        return getTextBytes(cipher, encrypt, MAX_DECRYPT_BLOCK);
    }

    private static byte[] getEncryptBytes(Cipher cipher, byte[] clear) throws Exception{
        return getTextBytes(cipher, clear, MAX_ENCRYPT_BLOCK);
    }

    private static byte[] getTextBytes(Cipher cipher, byte[] bytes, int size) throws Exception{
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > size) {
                cache = cipher.doFinal(bytes, offSet, size);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * size;
        }
        byte[] b = out.toByteArray();
        out.close();
        return b;
    }

    /**
     * <p> 公钥加密 </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
        throws Exception {
        byte[] keyBytes = YusuteBase64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        return getEncryptBytes(cipher, data);
    }

    /**
     * <p> 私钥加密 </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
        throws Exception {
        byte[] keyBytes = YusuteBase64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        return getEncryptBytes(cipher, data);
    }

    /**
     * <p> 获取私钥 </p>
     *
     * @param keyPair 密钥对
     */
    public static String getPrivateKey(RSAKeyPair keyPair)
        throws Exception {
        Key key = keyPair.getPrivateKey();
        byte[] encode = YusuteBase64.encode(key.getEncoded());
        return StringUtils.newStringUtf8(encode);
    }

    /**
     * <p> 获取公钥 </p>
     *
     * @param keyPair 密钥对
     */
    public static String getPublicKey(RSAKeyPair keyPair)
        throws Exception {
        Key key = keyPair.getPublicKey();
        byte[] encode = YusuteBase64.encode(key.getEncoded());
        return StringUtils.newStringUtf8(encode);
    }

}
