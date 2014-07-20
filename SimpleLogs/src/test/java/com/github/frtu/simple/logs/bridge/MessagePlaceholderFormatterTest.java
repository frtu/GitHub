package com.github.frtu.simple.logs.bridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class MessagePlaceholderFormatterTest {
    @Test
    public void testFormat() {
        String messageFormat = "arg1={} arg2={} final text";
        Object[] arguments = new Object[] { 1, "2" };
        String format = MessagePlaceholderFormatter.format(messageFormat, arguments);
        assertEquals("arg1=1 arg2=2 final text", format);
    }

    @Test
    public void testFormatWithDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(2014, 11, 31, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        String date = "2014-12-31T23:59:59.999Z";

        String messageFormat = "arg1={} arg2={} final text";
        Object[] arguments = new Object[] { 1, calendar };

        String format = MessagePlaceholderFormatter.format(messageFormat, arguments);
        assertEquals("arg1=1 arg2=" + date + " final text", format);
    }

    @Test
    public void testFormatWithPlaceholderAtEdges() {
        String messageFormat = "{} arg2={}";
        Object[] arguments = new Object[] { 1, "2" };
        String format = MessagePlaceholderFormatter.format(messageFormat, arguments);
        assertEquals("1 arg2=2", format);
    }

    @Test
    public void testFormatNullOrEmpty() {
        String messageFormat = "{} arg2={}";
        Object[] arguments = new Object[] { 1, "2" };

        assertNull(MessagePlaceholderFormatter.format(null, arguments));
        assertEquals(messageFormat, MessagePlaceholderFormatter.format(messageFormat, null));
        assertEquals(messageFormat, MessagePlaceholderFormatter.format(messageFormat, new Object[0]));
    }

    @Test
    public void testFormatAsymetric() {
        String messageFormat = "arg1={} arg2={} final text";
        Object[] arguments = new Object[] { 1 };
        String format = MessagePlaceholderFormatter.format(messageFormat, arguments);
        assertEquals("arg1=1 arg2={} final text", format);
    }
}
