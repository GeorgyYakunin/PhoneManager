package com.fara.phonemanager.utils;

/**
 * Created by Hesam Aldin Ahani on 5/4/2017.
 */

public class StringUtils {

    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    public static String setEmptyValueForNullValue(String value) {
        if (value == null || value.equals("null"))
            value = "";
        return value;
    }

    public static String toPersianNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }

        return out;
    }
}
