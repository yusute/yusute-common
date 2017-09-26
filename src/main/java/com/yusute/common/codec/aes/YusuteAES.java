package com.yusute.common.codec.aes;

import com.yusute.common.codec.base64.YusuteBase64;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.StringUtils;

/**
 * @author yusutehot
 */
public class YusuteAES {

    public static final String ALG = "AES";

    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    /**
     * AES必须16位字符串
     */
    public static final String IV = "abcdefghijklmnop";

    private String key;

    public YusuteAES(String key) {
        this.key = key;
    }

    public static byte[] encode(byte[] content, byte[] key) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALG);
        keyGenerator.init(128, new SecureRandom(key));
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV.getBytes()));
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public String encode(String content) throws Exception {
        byte[] encode = encode(content.getBytes(), key.getBytes());
        return StringUtils.newStringUtf8(YusuteBase64.encode(encode));
    }

    public static byte[] decode(byte[] content, byte[] key) throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALG);
        //key长可设为128，192，256位，这里只能设为128
        keyGenerator.init(128, new SecureRandom(key));
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV.getBytes()));
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public String decode(String content) throws Exception {
        byte[] decode = YusuteBase64.decode(content);
        byte[] base = decode(decode, key.getBytes());
        return StringUtils.newStringUtf8(base);
    }

}
