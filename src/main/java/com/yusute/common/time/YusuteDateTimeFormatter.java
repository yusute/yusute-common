package com.yusute.common.time;

import java.time.format.DateTimeFormatter;

/**
 * @author yusutehot
 */
public enum  YusuteDateTimeFormatter {
    /**
     *时间格式：yyyy-MM-dd
     */
    DEFAULT_DATE_PATTERN("yyyy-MM-dd"),
    /**
     *时间格式：yyyy/MM/dd
     */
    SLASH_DATE_PATTERN("yyyy/MM/dd"),

    /**
     *时间格式：yyyy-MM-dd HH:mm:ss
     */
    DEFAULT_DATE_TIME_PATTERN("yyyy-MM-dd HH:mm:ss"),

    /**
     *时间格式：yyyy/MM/dd HH:mm:ss
     */
    SLASH_DATE_TIME_PATTERN("yyyy/MM/dd HH:mm:ss"),

    /**
     *时间格式：HH:mm:ss
     */
    DEFAULT_TIME_PATTERN("HH:mm:ss");

    public final transient DateTimeFormatter formatter;

    YusuteDateTimeFormatter(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }
}
