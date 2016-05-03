package org.apache.logging.log4j.core.appender.db.jpa;

import java.util.Map;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;

@MappedSuperclass
@Inheritance(
    strategy = InheritanceType.SINGLE_TABLE
)
public abstract class AbstractLogEventWrapperEntity implements LogEvent {

    private static final long serialVersionUID = 1L;
    private final LogEvent wrappedEvent;

    protected AbstractLogEventWrapperEntity() {
        this(new AbstractLogEventWrapperEntity.NullLogEvent((AbstractLogEventWrapperEntity.SyntheticClass_1) null));
    }

    protected AbstractLogEventWrapperEntity(LogEvent logevent) {
        if (logevent == null) {
            throw new IllegalArgumentException("The wrapped event cannot be null.");
        } else {
            this.wrappedEvent = logevent;
        }
    }

    @Transient
    protected final LogEvent getWrappedEvent() {
        return this.wrappedEvent;
    }

    public void setLevel(Level level) {}

    public void setLoggerName(String s) {}

    public void setSource(StackTraceElement stacktraceelement) {}

    public void setMessage(Message message) {}

    public void setMarker(Marker marker) {}

    public void setThreadName(String s) {}

    public void setMillis(long i) {}

    public void setThrown(Throwable throwable) {}

    public void setContextMap(Map map) {}

    public void setContextStack(ThreadContext.ContextStack threadcontext_contextstack) {}

    public void setFQCN(String s) {}

    @Transient
    public final boolean isIncludeLocation() {
        return this.getWrappedEvent().isIncludeLocation();
    }

    public final void setIncludeLocation(boolean flag) {
        this.getWrappedEvent().setIncludeLocation(flag);
    }

    @Transient
    public final boolean isEndOfBatch() {
        return this.getWrappedEvent().isEndOfBatch();
    }

    public final void setEndOfBatch(boolean flag) {
        this.getWrappedEvent().setEndOfBatch(flag);
    }

    static class SyntheticClass_1 {    }

    private static class NullLogEvent implements LogEvent {

        private static final long serialVersionUID = 1L;

        private NullLogEvent() {}

        public Level getLevel() {
            return null;
        }

        public String getLoggerName() {
            return null;
        }

        public StackTraceElement getSource() {
            return null;
        }

        public Message getMessage() {
            return null;
        }

        public Marker getMarker() {
            return null;
        }

        public String getThreadName() {
            return null;
        }

        public long getMillis() {
            return 0L;
        }

        public Throwable getThrown() {
            return null;
        }

        public Map getContextMap() {
            return null;
        }

        public ThreadContext.ContextStack getContextStack() {
            return null;
        }

        public String getFQCN() {
            return null;
        }

        public boolean isIncludeLocation() {
            return false;
        }

        public void setIncludeLocation(boolean flag) {}

        public boolean isEndOfBatch() {
            return false;
        }

        public void setEndOfBatch(boolean flag) {}

        NullLogEvent(AbstractLogEventWrapperEntity.SyntheticClass_1 abstractlogeventwrapperentity_syntheticclass_1) {
            this();
        }
    }
}
