package com.jobhunter.jobhunter_be.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static String nowAsIso() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static boolean isExpired(Date date) {
        return date.before(new Date());
    }
}
