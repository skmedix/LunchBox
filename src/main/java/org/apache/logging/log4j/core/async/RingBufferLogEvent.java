package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

public class RingBufferLogEvent implements LogEvent {

    private static final long serialVersionUID = 8462119088943934758L;
    public static final RingBufferLogEvent.Factory FACTORY = new RingBufferLogEvent.Factory((RingBufferLogEvent.SyntheticClass_1) null);
    private AsyncLogger asyncLogger;
    private String loggerName;
    private Marker marker;
    private String fqcn;
    private Level level;
    private Message message;
    private Throwable thrown;
    private Map contextMap;
    private ThreadContext.ContextStack contextStack;
    private String threadName;
    private StackTraceElement location;
    private long currentTimeMillis;
    private boolean endOfBatch;
    private boolean includeLocation;

    public void setValues(AsyncLogger asynclogger, String s, Marker marker, String s1, Level level, Message message, Throwable throwable, Map map, ThreadContext.ContextStack threadcontext_contextstack, String s2, StackTraceElement stacktraceelement, long i) {
        this.asyncLogger = asynclogger;
        this.loggerName = s;
        this.marker = marker;
        this.fqcn = s1;
        this.level = level;
        this.message = message;
        this.thrown = throwable;
        this.contextMap = map;
        this.contextStack = threadcontext_contextstack;
        this.threadName = s2;
        this.location = stacktraceelement;
        this.currentTimeMillis = i;
    }

    public void execute(boolean flag) {
        this.endOfBatch = flag;
        this.asyncLogger.actualAsyncLog(this);
    }

    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }

    public void setEndOfBatch(boolean flag) {
        this.endOfBatch = flag;
    }

    public boolean isIncludeLocation() {
        return this.includeLocation;
    }

    public void setIncludeLocation(boolean flag) {
        this.includeLocation = flag;
    }

    public String getLoggerName() {
        return this.loggerName;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public String getFQCN() {
        return this.fqcn;
    }

    public Level getLevel() {
        return this.level;
    }

    public Message getMessage() {
        if (this.message == null) {
            this.message = new SimpleMessage("");
        }

        return this.message;
    }

    public Throwable getThrown() {
        return this.thrown;
    }

    public Map getContextMap() {
        return this.contextMap;
    }

    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public StackTraceElement getSource() {
        return this.location;
    }

    public long getMillis() {
        return this.currentTimeMillis;
    }

    public void mergePropertiesIntoContextMap(Map map, StrSubstitutor strsubstitutor) {
        if (map != null) {
            HashMap hashmap = this.contextMap == null ? new HashMap() : new HashMap(this.contextMap);
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Property property = (Property) entry.getKey();

                if (!hashmap.containsKey(property.getName())) {
                    String s = ((Boolean) entry.getValue()).booleanValue() ? strsubstitutor.replace(property.getValue()) : property.getValue();

                    hashmap.put(property.getName(), s);
                }
            }

            this.contextMap = hashmap;
        }
    }

    public void clear() {
        this.setValues((AsyncLogger) null, (String) null, (Marker) null, (String) null, (Level) null, (Message) null, (Throwable) null, (Map) null, (ThreadContext.ContextStack) null, (String) null, (StackTraceElement) null, 0L);
    }

    static class SyntheticClass_1 {    }

    private static class Factory implements EventFactory {

        private Factory() {}

        public RingBufferLogEvent newInstance() {
            return new RingBufferLogEvent();
        }

        Factory(RingBufferLogEvent.SyntheticClass_1 ringbufferlogevent_syntheticclass_1) {
            this();
        }
    }
}
