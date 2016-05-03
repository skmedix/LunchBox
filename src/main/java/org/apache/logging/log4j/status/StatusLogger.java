package org.apache.logging.log4j.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class StatusLogger extends AbstractLogger {

    public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
    private static final String NOT_AVAIL = "?";
    private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
    private static final int MAX_ENTRIES = StatusLogger.PROPS.getIntegerProperty("log4j2.status.entries", 200);
    private static final String DEFAULT_STATUS_LEVEL = StatusLogger.PROPS.getStringProperty("log4j2.StatusLogger.level");
    private static final StatusLogger STATUS_LOGGER = new StatusLogger();
    private final SimpleLogger logger;
    private final CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
    private final ReentrantReadWriteLock listenersLock = new ReentrantReadWriteLock();
    private final Queue messages;
    private final ReentrantLock msgLock;
    private int listenersLevel;

    private StatusLogger() {
        this.messages = new StatusLogger.BoundedQueue(StatusLogger.MAX_ENTRIES);
        this.msgLock = new ReentrantLock();
        this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", (MessageFactory) null, StatusLogger.PROPS, System.err);
        this.listenersLevel = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
    }

    public static StatusLogger getLogger() {
        return StatusLogger.STATUS_LOGGER;
    }

    public Level getLevel() {
        return this.logger.getLevel();
    }

    public void setLevel(Level level) {
        this.logger.setLevel(level);
    }

    public void registerListener(StatusListener statuslistener) {
        this.listenersLock.writeLock().lock();

        try {
            this.listeners.add(statuslistener);
            Level level = statuslistener.getStatusLevel();

            if (this.listenersLevel < level.intLevel()) {
                this.listenersLevel = level.intLevel();
            }
        } finally {
            this.listenersLock.writeLock().unlock();
        }

    }

    public void removeListener(StatusListener statuslistener) {
        this.listenersLock.writeLock().lock();

        try {
            this.listeners.remove(statuslistener);
            int i = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
            Iterator iterator = this.listeners.iterator();

            while (iterator.hasNext()) {
                StatusListener statuslistener1 = (StatusListener) iterator.next();
                int j = statuslistener1.getStatusLevel().intLevel();

                if (i < j) {
                    i = j;
                }
            }

            this.listenersLevel = i;
        } finally {
            this.listenersLock.writeLock().unlock();
        }
    }

    public Iterator getListeners() {
        return this.listeners.iterator();
    }

    public void reset() {
        this.listeners.clear();
        this.clear();
    }

    public List getStatusData() {
        this.msgLock.lock();

        ArrayList arraylist;

        try {
            arraylist = new ArrayList(this.messages);
        } finally {
            this.msgLock.unlock();
        }

        return arraylist;
    }

    public void clear() {
        this.msgLock.lock();

        try {
            this.messages.clear();
        } finally {
            this.msgLock.unlock();
        }

    }

    public void log(Marker marker, String s, Level level, Message message, Throwable throwable) {
        StackTraceElement stacktraceelement = null;

        if (s != null) {
            stacktraceelement = this.getStackTraceElement(s, Thread.currentThread().getStackTrace());
        }

        StatusData statusdata = new StatusData(stacktraceelement, level, message, throwable);

        this.msgLock.lock();

        try {
            this.messages.add(statusdata);
        } finally {
            this.msgLock.unlock();
        }

        if (this.listeners.size() > 0) {
            Iterator iterator = this.listeners.iterator();

            while (iterator.hasNext()) {
                StatusListener statuslistener = (StatusListener) iterator.next();

                if (statusdata.getLevel().isAtLeastAsSpecificAs(statuslistener.getStatusLevel())) {
                    statuslistener.log(statusdata);
                }
            }
        } else {
            this.logger.log(marker, s, level, message, throwable);
        }

    }

    private StackTraceElement getStackTraceElement(String s, StackTraceElement[] astacktraceelement) {
        if (s == null) {
            return null;
        } else {
            boolean flag = false;
            StackTraceElement[] astacktraceelement1 = astacktraceelement;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement1[j];

                if (flag) {
                    return stacktraceelement;
                }

                String s1 = stacktraceelement.getClassName();

                if (s.equals(s1)) {
                    flag = true;
                } else if ("?".equals(s1)) {
                    break;
                }
            }

            return null;
        }
    }

    protected boolean isEnabled(Level level, Marker marker, String s) {
        return this.isEnabled(level, marker);
    }

    protected boolean isEnabled(Level level, Marker marker, String s, Throwable throwable) {
        return this.isEnabled(level, marker);
    }

    protected boolean isEnabled(Level level, Marker marker, String s, Object... aobject) {
        return this.isEnabled(level, marker);
    }

    protected boolean isEnabled(Level level, Marker marker, Object object, Throwable throwable) {
        return this.isEnabled(level, marker);
    }

    protected boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable) {
        return this.isEnabled(level, marker);
    }

    public boolean isEnabled(Level level, Marker marker) {
        if (this.listeners.size() > 0) {
            return this.listenersLevel >= level.intLevel();
        } else {
            switch (StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[level.ordinal()]) {
            case 1:
                return this.logger.isFatalEnabled(marker);

            case 2:
                return this.logger.isTraceEnabled(marker);

            case 3:
                return this.logger.isDebugEnabled(marker);

            case 4:
                return this.logger.isInfoEnabled(marker);

            case 5:
                return this.logger.isWarnEnabled(marker);

            case 6:
                return this.logger.isErrorEnabled(marker);

            default:
                return false;
            }
        }
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$Level = new int[Level.values().length];

        static {
            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.FATAL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.TRACE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.DEBUG.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.INFO.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.WARN.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

        }
    }

    private class BoundedQueue extends ConcurrentLinkedQueue {

        private static final long serialVersionUID = -3945953719763255337L;
        private final int size;

        public BoundedQueue(int i) {
            this.size = i;
        }

        public boolean add(Object object) {
            while (StatusLogger.this.messages.size() > this.size) {
                StatusLogger.this.messages.poll();
            }

            return super.add(object);
        }
    }
}
