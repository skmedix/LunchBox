package org.apache.logging.log4j.core.impl;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.TimestampMessage;

public class Log4jLogEvent implements LogEvent {

    private static final long serialVersionUID = -1351367343806656055L;
    private static final String NOT_AVAIL = "?";
    private final String fqcnOfLogger;
    private final Marker marker;
    private final Level level;
    private final String name;
    private final Message message;
    private final long timestamp;
    private final ThrowableProxy throwable;
    private final Map mdc;
    private final ThreadContext.ContextStack ndc;
    private String threadName;
    private StackTraceElement location;
    private boolean includeLocation;
    private boolean endOfBatch;

    public Log4jLogEvent(long i) {
        this("", (Marker) null, "", (Level) null, (Message) null, (ThrowableProxy) null, (Map) null, (ThreadContext.ContextStack) null, (String) null, (StackTraceElement) null, i);
    }

    public Log4jLogEvent(String s, Marker marker, String s1, Level level, Message message, Throwable throwable) {
        this(s, marker, s1, level, message, (List) null, throwable);
    }

    public Log4jLogEvent(String s, Marker marker, String s1, Level level, Message message, List list, Throwable throwable) {
        this(s, marker, s1, level, message, throwable, createMap(list), ThreadContext.getDepth() == 0 ? null : ThreadContext.cloneStack(), (String) null, (StackTraceElement) null, System.currentTimeMillis());
    }

    public Log4jLogEvent(String s, Marker marker, String s1, Level level, Message message, Throwable throwable, Map map, ThreadContext.ContextStack threadcontext_contextstack, String s2, StackTraceElement stacktraceelement, long i) {
        this(s, marker, s1, level, message, throwable == null ? null : new ThrowableProxy(throwable), map, threadcontext_contextstack, s2, stacktraceelement, i);
    }

    public static Log4jLogEvent createEvent(String s, Marker marker, String s1, Level level, Message message, ThrowableProxy throwableproxy, Map map, ThreadContext.ContextStack threadcontext_contextstack, String s2, StackTraceElement stacktraceelement, long i) {
        return new Log4jLogEvent(s, marker, s1, level, message, throwableproxy, map, threadcontext_contextstack, s2, stacktraceelement, i);
    }

    private Log4jLogEvent(String s, Marker marker, String s1, Level level, Message message, ThrowableProxy throwableproxy, Map map, ThreadContext.ContextStack threadcontext_contextstack, String s2, StackTraceElement stacktraceelement, long i) {
        this.threadName = null;
        this.endOfBatch = false;
        this.name = s;
        this.marker = marker;
        this.fqcnOfLogger = s1;
        this.level = level;
        this.message = message;
        this.throwable = throwableproxy;
        this.mdc = map;
        this.ndc = threadcontext_contextstack;
        this.timestamp = message instanceof TimestampMessage ? ((TimestampMessage) message).getTimestamp() : i;
        this.threadName = s2;
        this.location = stacktraceelement;
        if (message != null && message instanceof LoggerNameAwareMessage) {
            ((LoggerNameAwareMessage) message).setLoggerName(this.name);
        }

    }

    private static Map createMap(List list) {
        Map map = ThreadContext.getImmutableContext();

        if (map == null && (list == null || list.size() == 0)) {
            return null;
        } else if (list != null && list.size() != 0) {
            HashMap hashmap = new HashMap(map);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Property property = (Property) iterator.next();

                if (!hashmap.containsKey(property.getName())) {
                    hashmap.put(property.getName(), property.getValue());
                }
            }

            return Collections.unmodifiableMap(hashmap);
        } else {
            return map;
        }
    }

    public Level getLevel() {
        return this.level;
    }

    public String getLoggerName() {
        return this.name;
    }

    public Message getMessage() {
        return this.message;
    }

    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = Thread.currentThread().getName();
        }

        return this.threadName;
    }

    public long getMillis() {
        return this.timestamp;
    }

    public Throwable getThrown() {
        return this.throwable == null ? null : this.throwable.getThrowable();
    }

    public ThrowableProxy getThrownProxy() {
        return this.throwable;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public String getFQCN() {
        return this.fqcnOfLogger;
    }

    public Map getContextMap() {
        return this.mdc == null ? ThreadContext.EMPTY_MAP : this.mdc;
    }

    public ThreadContext.ContextStack getContextStack() {
        return (ThreadContext.ContextStack) (this.ndc == null ? ThreadContext.EMPTY_STACK : this.ndc);
    }

    public StackTraceElement getSource() {
        if (this.location != null) {
            return this.location;
        } else if (this.fqcnOfLogger != null && this.includeLocation) {
            this.location = calcLocation(this.fqcnOfLogger);
            return this.location;
        } else {
            return null;
        }
    }

    public static StackTraceElement calcLocation(String s) {
        if (s == null) {
            return null;
        } else {
            StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
            boolean flag = false;
            StackTraceElement[] astacktraceelement1 = astacktraceelement;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement1[j];
                String s1 = stacktraceelement.getClassName();

                if (flag) {
                    if (!s.equals(s1)) {
                        return stacktraceelement;
                    }
                } else if (s.equals(s1)) {
                    flag = true;
                } else if ("?".equals(s1)) {
                    break;
                }
            }

            return null;
        }
    }

    public boolean isIncludeLocation() {
        return this.includeLocation;
    }

    public void setIncludeLocation(boolean flag) {
        this.includeLocation = flag;
    }

    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }

    public void setEndOfBatch(boolean flag) {
        this.endOfBatch = flag;
    }

    protected Object writeReplace() {
        return new Log4jLogEvent.LogEventProxy(this, this.includeLocation);
    }

    public static Serializable serialize(Log4jLogEvent log4jlogevent, boolean flag) {
        return new Log4jLogEvent.LogEventProxy(log4jlogevent, flag);
    }

    public static Log4jLogEvent deserialize(Serializable serializable) {
        if (serializable == null) {
            throw new NullPointerException("Event cannot be null");
        } else if (serializable instanceof Log4jLogEvent.LogEventProxy) {
            Log4jLogEvent.LogEventProxy log4jlogevent_logeventproxy = (Log4jLogEvent.LogEventProxy) serializable;
            Log4jLogEvent log4jlogevent = new Log4jLogEvent(log4jlogevent_logeventproxy.name, log4jlogevent_logeventproxy.marker, log4jlogevent_logeventproxy.fqcnOfLogger, log4jlogevent_logeventproxy.level, log4jlogevent_logeventproxy.message, log4jlogevent_logeventproxy.throwable, log4jlogevent_logeventproxy.mdc, log4jlogevent_logeventproxy.ndc, log4jlogevent_logeventproxy.threadName, log4jlogevent_logeventproxy.location, log4jlogevent_logeventproxy.timestamp);

            log4jlogevent.setEndOfBatch(log4jlogevent_logeventproxy.isEndOfBatch);
            log4jlogevent.setIncludeLocation(log4jlogevent_logeventproxy.isLocationRequired);
            return log4jlogevent;
        } else {
            throw new IllegalArgumentException("Event is not a serialized LogEvent: " + serializable.toString());
        }
    }

    private void readObject(ObjectInputStream objectinputstream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        String s = this.name.isEmpty() ? "root" : this.name;

        stringbuilder.append("Logger=").append(s);
        stringbuilder.append(" Level=").append(this.level.name());
        stringbuilder.append(" Message=").append(this.message.getFormattedMessage());
        return stringbuilder.toString();
    }

    Log4jLogEvent(String s, Marker marker, String s1, Level level, Message message, ThrowableProxy throwableproxy, Map map, ThreadContext.ContextStack threadcontext_contextstack, String s2, StackTraceElement stacktraceelement, long i, Log4jLogEvent.SyntheticClass_1 log4jlogevent_syntheticclass_1) {
        this(s, marker, s1, level, message, throwableproxy, map, threadcontext_contextstack, s2, stacktraceelement, i);
    }

    static class SyntheticClass_1 {    }

    private static class LogEventProxy implements Serializable {

        private static final long serialVersionUID = -7139032940312647146L;
        private final String fqcnOfLogger;
        private final Marker marker;
        private final Level level;
        private final String name;
        private final Message message;
        private final long timestamp;
        private final ThrowableProxy throwable;
        private final Map mdc;
        private final ThreadContext.ContextStack ndc;
        private final String threadName;
        private final StackTraceElement location;
        private final boolean isLocationRequired;
        private final boolean isEndOfBatch;

        public LogEventProxy(Log4jLogEvent log4jlogevent, boolean flag) {
            this.fqcnOfLogger = log4jlogevent.fqcnOfLogger;
            this.marker = log4jlogevent.marker;
            this.level = log4jlogevent.level;
            this.name = log4jlogevent.name;
            this.message = log4jlogevent.message;
            this.timestamp = log4jlogevent.timestamp;
            this.throwable = log4jlogevent.throwable;
            this.mdc = log4jlogevent.mdc;
            this.ndc = log4jlogevent.ndc;
            this.location = flag ? log4jlogevent.getSource() : null;
            this.threadName = log4jlogevent.getThreadName();
            this.isLocationRequired = flag;
            this.isEndOfBatch = log4jlogevent.endOfBatch;
        }

        protected Object readResolve() {
            Log4jLogEvent log4jlogevent = new Log4jLogEvent(this.name, this.marker, this.fqcnOfLogger, this.level, this.message, this.throwable, this.mdc, this.ndc, this.threadName, this.location, this.timestamp, (Log4jLogEvent.SyntheticClass_1) null);

            log4jlogevent.setEndOfBatch(this.isEndOfBatch);
            log4jlogevent.setIncludeLocation(this.isLocationRequired);
            return log4jlogevent;
        }
    }
}
