package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {

    String format(long i);

    String format(Date date);

    String format(Calendar calendar);

    StringBuffer format(long i, StringBuffer stringbuffer);

    StringBuffer format(Date date, StringBuffer stringbuffer);

    StringBuffer format(Calendar calendar, StringBuffer stringbuffer);

    String getPattern();

    TimeZone getTimeZone();

    Locale getLocale();

    StringBuffer format(Object object, StringBuffer stringbuffer, FieldPosition fieldposition);
}
