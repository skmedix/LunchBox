package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public interface Logger {

    void catching(Level level, Throwable throwable);

    void catching(Throwable throwable);

    void debug(Marker marker, Message message);

    void debug(Marker marker, Message message, Throwable throwable);

    void debug(Marker marker, Object object);

    void debug(Marker marker, Object object, Throwable throwable);

    void debug(Marker marker, String s);

    void debug(Marker marker, String s, Object... aobject);

    void debug(Marker marker, String s, Throwable throwable);

    void debug(Message message);

    void debug(Message message, Throwable throwable);

    void debug(Object object);

    void debug(Object object, Throwable throwable);

    void debug(String s);

    void debug(String s, Object... aobject);

    void debug(String s, Throwable throwable);

    void entry();

    void entry(Object... aobject);

    void error(Marker marker, Message message);

    void error(Marker marker, Message message, Throwable throwable);

    void error(Marker marker, Object object);

    void error(Marker marker, Object object, Throwable throwable);

    void error(Marker marker, String s);

    void error(Marker marker, String s, Object... aobject);

    void error(Marker marker, String s, Throwable throwable);

    void error(Message message);

    void error(Message message, Throwable throwable);

    void error(Object object);

    void error(Object object, Throwable throwable);

    void error(String s);

    void error(String s, Object... aobject);

    void error(String s, Throwable throwable);

    void exit();

    Object exit(Object object);

    void fatal(Marker marker, Message message);

    void fatal(Marker marker, Message message, Throwable throwable);

    void fatal(Marker marker, Object object);

    void fatal(Marker marker, Object object, Throwable throwable);

    void fatal(Marker marker, String s);

    void fatal(Marker marker, String s, Object... aobject);

    void fatal(Marker marker, String s, Throwable throwable);

    void fatal(Message message);

    void fatal(Message message, Throwable throwable);

    void fatal(Object object);

    void fatal(Object object, Throwable throwable);

    void fatal(String s);

    void fatal(String s, Object... aobject);

    void fatal(String s, Throwable throwable);

    MessageFactory getMessageFactory();

    String getName();

    void info(Marker marker, Message message);

    void info(Marker marker, Message message, Throwable throwable);

    void info(Marker marker, Object object);

    void info(Marker marker, Object object, Throwable throwable);

    void info(Marker marker, String s);

    void info(Marker marker, String s, Object... aobject);

    void info(Marker marker, String s, Throwable throwable);

    void info(Message message);

    void info(Message message, Throwable throwable);

    void info(Object object);

    void info(Object object, Throwable throwable);

    void info(String s);

    void info(String s, Object... aobject);

    void info(String s, Throwable throwable);

    boolean isDebugEnabled();

    boolean isDebugEnabled(Marker marker);

    boolean isEnabled(Level level);

    boolean isEnabled(Level level, Marker marker);

    boolean isErrorEnabled();

    boolean isErrorEnabled(Marker marker);

    boolean isFatalEnabled();

    boolean isFatalEnabled(Marker marker);

    boolean isInfoEnabled();

    boolean isInfoEnabled(Marker marker);

    boolean isTraceEnabled();

    boolean isTraceEnabled(Marker marker);

    boolean isWarnEnabled();

    boolean isWarnEnabled(Marker marker);

    void log(Level level, Marker marker, Message message);

    void log(Level level, Marker marker, Message message, Throwable throwable);

    void log(Level level, Marker marker, Object object);

    void log(Level level, Marker marker, Object object, Throwable throwable);

    void log(Level level, Marker marker, String s);

    void log(Level level, Marker marker, String s, Object... aobject);

    void log(Level level, Marker marker, String s, Throwable throwable);

    void log(Level level, Message message);

    void log(Level level, Message message, Throwable throwable);

    void log(Level level, Object object);

    void log(Level level, Object object, Throwable throwable);

    void log(Level level, String s);

    void log(Level level, String s, Object... aobject);

    void log(Level level, String s, Throwable throwable);

    void printf(Level level, Marker marker, String s, Object... aobject);

    void printf(Level level, String s, Object... aobject);

    Throwable throwing(Level level, Throwable throwable);

    Throwable throwing(Throwable throwable);

    void trace(Marker marker, Message message);

    void trace(Marker marker, Message message, Throwable throwable);

    void trace(Marker marker, Object object);

    void trace(Marker marker, Object object, Throwable throwable);

    void trace(Marker marker, String s);

    void trace(Marker marker, String s, Object... aobject);

    void trace(Marker marker, String s, Throwable throwable);

    void trace(Message message);

    void trace(Message message, Throwable throwable);

    void trace(Object object);

    void trace(Object object, Throwable throwable);

    void trace(String s);

    void trace(String s, Object... aobject);

    void trace(String s, Throwable throwable);

    void warn(Marker marker, Message message);

    void warn(Marker marker, Message message, Throwable throwable);

    void warn(Marker marker, Object object);

    void warn(Marker marker, Object object, Throwable throwable);

    void warn(Marker marker, String s);

    void warn(Marker marker, String s, Object... aobject);

    void warn(Marker marker, String s, Throwable throwable);

    void warn(Message message);

    void warn(Message message, Throwable throwable);

    void warn(Object object);

    void warn(Object object, Throwable throwable);

    void warn(String s);

    void warn(String s, Object... aobject);

    void warn(String s, Throwable throwable);
}
