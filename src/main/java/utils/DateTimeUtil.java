package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {

    public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd",
            DATE_AND_TIME_FORMAT = "yyyy-MMM-dd_HH-mm-ss";

    public static String getTodayDateByFormat(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
    }
}
