package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache {

    static final int NONE = -1;
    private final ConcurrentMap cInstanceCache = new ConcurrentHashMap(7);
    private static final ConcurrentMap cDateTimeInstanceCache = new ConcurrentHashMap(7);

    public Format getInstance() {
        return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
    }

    public Format getInstance(String s, TimeZone timezone, Locale locale) {
        if (s == null) {
            throw new NullPointerException("pattern must not be null");
        } else {
            if (timezone == null) {
                timezone = TimeZone.getDefault();
            }

            if (locale == null) {
                locale = Locale.getDefault();
            }

            FormatCache.MultipartKey formatcache_multipartkey = new FormatCache.MultipartKey(new Object[] { s, timezone, locale});
            Format format = (Format) this.cInstanceCache.get(formatcache_multipartkey);

            if (format == null) {
                format = this.createInstance(s, timezone, locale);
                Format format1 = (Format) this.cInstanceCache.putIfAbsent(formatcache_multipartkey, format);

                if (format1 != null) {
                    format = format1;
                }
            }

            return format;
        }
    }

    protected abstract Format createInstance(String s, TimeZone timezone, Locale locale);

    private Format getDateTimeInstance(Integer integer, Integer integer1, TimeZone timezone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        String s = getPatternForStyle(integer, integer1, locale);

        return this.getInstance(s, timezone, locale);
    }

    Format getDateTimeInstance(int i, int j, TimeZone timezone, Locale locale) {
        return this.getDateTimeInstance(Integer.valueOf(i), Integer.valueOf(j), timezone, locale);
    }

    Format getDateInstance(int i, TimeZone timezone, Locale locale) {
        return this.getDateTimeInstance(Integer.valueOf(i), (Integer) null, timezone, locale);
    }

    Format getTimeInstance(int i, TimeZone timezone, Locale locale) {
        return this.getDateTimeInstance((Integer) null, Integer.valueOf(i), timezone, locale);
    }

    static String getPatternForStyle(Integer integer, Integer integer1, Locale locale) {
        FormatCache.MultipartKey formatcache_multipartkey = new FormatCache.MultipartKey(new Object[] { integer, integer1, locale});
        String s = (String) FormatCache.cDateTimeInstanceCache.get(formatcache_multipartkey);

        if (s == null) {
            try {
                DateFormat dateformat;

                if (integer == null) {
                    dateformat = DateFormat.getTimeInstance(integer1.intValue(), locale);
                } else if (integer1 == null) {
                    dateformat = DateFormat.getDateInstance(integer.intValue(), locale);
                } else {
                    dateformat = DateFormat.getDateTimeInstance(integer.intValue(), integer1.intValue(), locale);
                }

                s = ((SimpleDateFormat) dateformat).toPattern();
                String s1 = (String) FormatCache.cDateTimeInstanceCache.putIfAbsent(formatcache_multipartkey, s);

                if (s1 != null) {
                    s = s1;
                }
            } catch (ClassCastException classcastexception) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }

        return s;
    }

    private static class MultipartKey {

        private final Object[] keys;
        private int hashCode;

        public MultipartKey(Object... aobject) {
            this.keys = aobject;
        }

        public boolean equals(Object object) {
            return Arrays.equals(this.keys, ((FormatCache.MultipartKey) object).keys);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int i = 0;
                Object[] aobject = this.keys;
                int j = aobject.length;

                for (int k = 0; k < j; ++k) {
                    Object object = aobject[k];

                    if (object != null) {
                        i = i * 7 + object.hashCode();
                    }
                }

                this.hashCode = i;
            }

            return this.hashCode;
        }
    }
}
