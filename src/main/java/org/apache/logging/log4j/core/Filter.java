package org.apache.logging.log4j.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.EnglishEnums;

public interface Filter {

    Filter.Result getOnMismatch();

    Filter.Result getOnMatch();

    Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject);

    Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable);

    Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable);

    Filter.Result filter(LogEvent logevent);

    public static enum Result {

        ACCEPT, NEUTRAL, DENY;

        public static Filter.Result toResult(String s) {
            return toResult(s, (Filter.Result) null);
        }

        public static Filter.Result toResult(String s, Filter.Result filter_result) {
            return (Filter.Result) EnglishEnums.valueOf(Filter.Result.class, s, filter_result);
        }
    }
}
