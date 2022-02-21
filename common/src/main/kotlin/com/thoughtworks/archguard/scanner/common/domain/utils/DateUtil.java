package com.thoughtworks.archguard.scanner.common.domain.utils;

import java.util.Date;

public class DateUtil {

    private DateUtil() {

    }

    public static String getCurrentTime() {
        Date dt = new Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

}
