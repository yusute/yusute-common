package com.yusute.common.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.tools.ant.taskdefs.Sleep;

/**
 * @author yusutehot
 */
public final class YusuteDateTime {


    // 默认时间格式
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = YusuteDateTimeFormatter.DEFAULT_DATE_TIME_PATTERN.formatter;

    // 无参数的构造函数,防止被实例化
    private YusuteDateTime() {};

    public static LocalDateTime dateTimeParse(String text) {
        return dateTimeParse(text, DEFAULT_DATETIME_FORMATTER);

    }

    public static LocalDateTime dateTimeParse(String text, DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(text, dateTimeFormatter);
    }


    public static String convert(LocalDateTime time) {
        return convert(time, DEFAULT_DATETIME_FORMATTER);

    }

    public static String convert(LocalDateTime time, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(time);

    }

    public static LocalDateTime getDateTime(){
        return LocalDateTime.now();
    }

    public static int compareTo(LocalDateTime time1, LocalDateTime time2){
        if (time1.isAfter(time2)){
            return 1;
        }else if (time1.isBefore(time2)){
            return -1;
        }
        return 0;
    }
}
