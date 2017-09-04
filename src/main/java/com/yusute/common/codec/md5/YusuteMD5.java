package com.yusute.common.codec.md5;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author yusutehot
 */
public final class YusuteMD5 {

    private YusuteMD5(){}

    public static String encode(String text){
        return DigestUtils.md5Hex(text);
    }

}
