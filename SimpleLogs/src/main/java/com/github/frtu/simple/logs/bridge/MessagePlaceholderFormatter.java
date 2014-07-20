package com.github.frtu.simple.logs.bridge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MessagePlaceholderFormatter {
    private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static String dateFormat = DEFAULT_DATE_FORMAT;

    public static String format(String messageFormat, Object[] parameters) {
        // Edge case. {} is length=2 so no need to treat lower than this. If no argument, return the messageFormat.
        if (messageFormat == null || messageFormat.length() <= 1 || parameters == null || parameters.length == 0) {
            return messageFormat;
        }

        // Start parsing
        StringBuilder stringBuilder = new StringBuilder();

        int startCursor = 0;
        int cursor = 1;
        int endCursor = messageFormat.length();

        int argCursor = 0;

        char[] charArray = messageFormat.toCharArray();
        while (cursor < endCursor) {
            if (charArray[cursor] == '}') {
                if (charArray[cursor - 1] == '{') {
                    // Append buffer
                    stringBuilder.append(messageFormat.substring(startCursor, cursor - 1));
                    startCursor = cursor + 1;

                    // Append value
                    Object parameter = parameters[argCursor];
                    if (parameter instanceof Calendar) {
                        stringBuilder.append(format((Calendar) parameter));
                    } else
                        if (parameter instanceof Date) {
                            stringBuilder.append(format((Date) parameter));
                        } else {
                            stringBuilder.append(parameter);
                        }

                    if (argCursor >= parameters.length - 1) {
                        // If no more argument, exit and append remaining buffer
                        break;
                    }
                    argCursor++;
                }
            }
            cursor++;
        }
        stringBuilder.append(messageFormat.substring(startCursor, endCursor));
        return stringBuilder.toString();
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    public static void setDateFormat(String dateFormat) {
        MessagePlaceholderFormatter.dateFormat = dateFormat;
    }

    public static String format(Calendar calendar) {
        return format(calendar.getTime());
    }

    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(false);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = simpleDateFormat.format(date);
        return result;
    }
}
