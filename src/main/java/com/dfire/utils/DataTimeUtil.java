package com.dfire.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTimeUtil {

    /**
     * 直接把当前时间转换为年月日时分秒字符串
     *
     * @return
     */
    public static String date2String() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
    }

    public static int interceptionTimestamp() {
        String timestamp = String.valueOf(currentTimeMillis());
        return Integer.parseInt(timestamp.substring(timestamp.length() - 8));
    }

    /**
     * 每次取时间戳延迟1ms
     *
     * @return
     */
    public static synchronized long currentTimeMillis() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

}
