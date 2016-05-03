package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventTranslator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;

public class RingBufferLogEventTranslator implements EventTranslator {

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

    public void translateTo(RingBufferLogEvent ringbufferlogevent, long i) {
        ringbufferlogevent.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.contextMap, this.contextStack, this.threadName, this.location, this.currentTimeMillis);
    }

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
}
