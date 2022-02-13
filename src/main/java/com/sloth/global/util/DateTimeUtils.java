package com.sloth.global.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String convertToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return format.format(date);
    }

    public static Date convertToDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static String convertToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate convertToDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
    }

    public static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
