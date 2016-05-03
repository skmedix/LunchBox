package org.apache.commons.lang3.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class BasicThreadFactory implements ThreadFactory {

    private final AtomicLong threadCounter;
    private final ThreadFactory wrappedFactory;
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    private final String namingPattern;
    private final Integer priority;
    private final Boolean daemonFlag;

    private BasicThreadFactory(BasicThreadFactory.Builder basicthreadfactory_builder) {
        if (basicthreadfactory_builder.wrappedFactory == null) {
            this.wrappedFactory = Executors.defaultThreadFactory();
        } else {
            this.wrappedFactory = basicthreadfactory_builder.wrappedFactory;
        }

        this.namingPattern = basicthreadfactory_builder.namingPattern;
        this.priority = basicthreadfactory_builder.priority;
        this.daemonFlag = basicthreadfactory_builder.daemonFlag;
        this.uncaughtExceptionHandler = basicthreadfactory_builder.exceptionHandler;
        this.threadCounter = new AtomicLong();
    }

    public final ThreadFactory getWrappedFactory() {
        return this.wrappedFactory;
    }

    public final String getNamingPattern() {
        return this.namingPattern;
    }

    public final Boolean getDaemonFlag() {
        return this.daemonFlag;
    }

    public final Integer getPriority() {
        return this.priority;
    }

    public final UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.uncaughtExceptionHandler;
    }

    public long getThreadCount() {
        return this.threadCounter.get();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = this.getWrappedFactory().newThread(runnable);

        this.initializeThread(thread);
        return thread;
    }

    private void initializeThread(Thread thread) {
        if (this.getNamingPattern() != null) {
            Long olong = Long.valueOf(this.threadCounter.incrementAndGet());

            thread.setName(String.format(this.getNamingPattern(), new Object[] { olong}));
        }

        if (this.getUncaughtExceptionHandler() != null) {
            thread.setUncaughtExceptionHandler(this.getUncaughtExceptionHandler());
        }

        if (this.getPriority() != null) {
            thread.setPriority(this.getPriority().intValue());
        }

        if (this.getDaemonFlag() != null) {
            thread.setDaemon(this.getDaemonFlag().booleanValue());
        }

    }

    BasicThreadFactory(BasicThreadFactory.Builder basicthreadfactory_builder, BasicThreadFactory.SyntheticClass_1 basicthreadfactory_syntheticclass_1) {
        this(basicthreadfactory_builder);
    }

    static class SyntheticClass_1 {    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder {

        private ThreadFactory wrappedFactory;
        private UncaughtExceptionHandler exceptionHandler;
        private String namingPattern;
        private Integer priority;
        private Boolean daemonFlag;

        public BasicThreadFactory.Builder wrappedFactory(ThreadFactory threadfactory) {
            if (threadfactory == null) {
                throw new NullPointerException("Wrapped ThreadFactory must not be null!");
            } else {
                this.wrappedFactory = threadfactory;
                return this;
            }
        }

        public BasicThreadFactory.Builder namingPattern(String s) {
            if (s == null) {
                throw new NullPointerException("Naming pattern must not be null!");
            } else {
                this.namingPattern = s;
                return this;
            }
        }

        public BasicThreadFactory.Builder daemon(boolean flag) {
            this.daemonFlag = Boolean.valueOf(flag);
            return this;
        }

        public BasicThreadFactory.Builder priority(int i) {
            this.priority = Integer.valueOf(i);
            return this;
        }

        public BasicThreadFactory.Builder uncaughtExceptionHandler(UncaughtExceptionHandler uncaughtexceptionhandler) {
            if (uncaughtexceptionhandler == null) {
                throw new NullPointerException("Uncaught exception handler must not be null!");
            } else {
                this.exceptionHandler = uncaughtexceptionhandler;
                return this;
            }
        }

        public void reset() {
            this.wrappedFactory = null;
            this.exceptionHandler = null;
            this.namingPattern = null;
            this.priority = null;
            this.daemonFlag = null;
        }

        public BasicThreadFactory build() {
            BasicThreadFactory basicthreadfactory = new BasicThreadFactory(this, (BasicThreadFactory.SyntheticClass_1) null);

            this.reset();
            return basicthreadfactory;
        }
    }
}
