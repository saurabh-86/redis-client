package com.flipkart.redis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saurabh.agrawal on 19/10/14.
 */
public class IOUtils {

    private static final String EMPTY_STR = "";

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Read from Redis HashMap
    public static Date parseDate(String input) {
        try {
            return df.parse(input);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String parseString(String input) {
        return input;
    }

    public static Boolean parseBoolean(String input) {
        return Boolean.parseBoolean(input);
    }

    public static Integer parseInt(String input) {
        return Integer.parseInt(input);
    }

    public static Long parseLong(String input) {
        return Long.parseLong(input);
    }

    // Write to Redis HashMap
    public static String format(String input) {
        return input != null ? input : EMPTY_STR;
    }

    public static String format(Boolean input) {
        return input != null ? String.valueOf(input) : EMPTY_STR;
    }

    public static String format(Date input) {
        return input != null ? df.format(input) : EMPTY_STR;
    }

    public static String format(Long input) {
        return input != null ? String.valueOf(input) : EMPTY_STR;
    }

    public static String format(Integer input) {
        return input != null ? String.valueOf(input) : EMPTY_STR;
    }

}
