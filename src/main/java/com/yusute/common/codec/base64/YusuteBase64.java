package com.yusute.common.codec.base64;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/**
 * @author yusutehot
 */
public final class YusuteBase64 {

    private YusuteBase64() {
    }

    public static byte[] encode(byte[] bytes) {
        byte[] b = Base64.encodeBase64(bytes, true);
        return b;
    }

    public static String encode(String text) {
        byte[] bytes = text.getBytes();
        byte[] b = encode(bytes);
        return StringUtils.newStringUtf8(b);
    }

    public static byte[] decode(byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    public static byte[] decode(String text) {
        byte[] b = text.getBytes();
        return decode(b);
    }
}
