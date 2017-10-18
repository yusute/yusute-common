package test;

import com.yusute.common.codec.rsa.RSAKeyPair;
import com.yusute.common.codec.rsa.YusuteRSA;
import java.util.Map;

/**
 * @author yusutehot
 */
public class RSATest {
    static String publicKey;
    static String privateKey;

    static {
        try {
            RSAKeyPair rsaKeyPair = YusuteRSA.genKeyPair();
            publicKey = YusuteRSA.getPublicKey(rsaKeyPair);
            privateKey = YusuteRSA.getPrivateKey(rsaKeyPair);
            //System.err.println("公钥: \n\r" + publicKey);
            //System.err.println("私钥： \n\r" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        //test();
        testSign();
        //testHttpSign();
    }

    static void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        System.out.println("加密前文字：" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = YusuteRSA.encryptByPublicKey(data, publicKey);
        System.out.println("加密后文字：" + new String(encodedData));
        byte[] decodedData = YusuteRSA.decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: " + target);
    }

    static void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
        String source = "这是一行测试RSA数字签名的无意义文字";
        System.out.println("原文字：" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = YusuteRSA.encryptByPrivateKey(data, privateKey);
        System.out.println("加密后：" + new String(encodedData));
        byte[] decodedData = YusuteRSA.decryptByPublicKey(encodedData, publicKey);
        String target = new String(decodedData);
        System.out.println("解密后: " + target);
        System.err.println("私钥签名——公钥验证签名");
        String sign = YusuteRSA.signature(encodedData, privateKey);
        System.err.println("签名:" + sign);
        boolean status = YusuteRSA.verify(encodedData, publicKey, sign);
        System.err.println("验证结果:" + status);
    }

    static void testHttpSign() throws Exception {
        String param = "id=1&name=张三";
        byte[] encodedData = YusuteRSA.encryptByPrivateKey(param.getBytes(), privateKey);
        System.out.println("加密后：" + encodedData);

        byte[] decodedData = YusuteRSA.decryptByPublicKey(encodedData, publicKey);
        System.out.println("解密后：" + new String(decodedData));

        String sign = YusuteRSA.signature(encodedData, privateKey);
        System.err.println("签名：" + sign);

        boolean status = YusuteRSA.verify(encodedData, publicKey, sign);
        System.err.println("签名验证结果：" + status);
    }
}
