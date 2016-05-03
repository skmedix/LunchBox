package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

class AsyncLoggerConfigHelper {

    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
    private static final int HALF_A_SECOND = 500;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static ThreadFactory threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
    private static volatile Disruptor disruptor;
    private static ExecutorService executor;
    private static volatile int count = 0;
    private static final EventFactory FACTORY = new EventFactory() {
        public AsyncLoggerConfigHelper.Log4jEventWrapper newInstance() {
            return new AsyncLoggerConfigHelper.Log4jEventWrapper(null);
        }
    };
    private final EventTranslator translator = new EventTranslator() {
        public void translateTo(AsyncLoggerConfigHelper.Log4jEventWrapper asyncloggerconfighelper_log4jeventwrapper, long i) {
            asyncloggerconfighelper_log4jeventwrapper.event = (LogEvent) AsyncLoggerConfigHelper.this.currentLogEvent.get();
            asyncloggerconfighelper_log4jeventwrapper.loggerConfig = AsyncLoggerConfigHelper.this.asyncLoggerConfig;
        }
    };
    private final ThreadLocal currentLogEvent = new ThreadLocal();
    private final AsyncLoggerConfig asyncLoggerConfig;

    public AsyncLoggerConfigHelper(AsyncLoggerConfig asyncloggerconfig) {
        this.asyncLoggerConfig = asyncloggerconfig;
        claim();
    }

    private static synchronized void initDisruptor() {
        if (AsyncLoggerConfigHelper.disruptor != null) {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.count)});
        } else {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.count)});
            int i = calculateRingBufferSize();
            WaitStrategy waitstrategy = createWaitStrategy();

            AsyncLoggerConfigHelper.executor = Executors.newSingleThreadExecutor(AsyncLoggerConfigHelper.threadFactory);
            AsyncLoggerConfigHelper.disruptor = new Disruptor(AsyncLoggerConfigHelper.FACTORY, i, AsyncLoggerConfigHelper.executor, ProducerType.MULTI, waitstrategy);
            AsyncLoggerConfigHelper.Log4jEventWrapperHandler[] aasyncloggerconfighelper_log4jeventwrapperhandler = new AsyncLoggerConfigHelper.Log4jEventWrapperHandler[] { new AsyncLoggerConfigHelper.Log4jEventWrapperHandler(null)};
            ExceptionHandler exceptionhandler = getExceptionHandler();

            AsyncLoggerConfigHelper.disruptor.handleExceptionsWith(exceptionhandler);
            AsyncLoggerConfigHelper.disruptor.handleEventsWith(aasyncloggerconfighelper_log4jeventwrapperhandler);
            AsyncLoggerConfigHelper.LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.disruptor.getRingBuffer().getBufferSize()), waitstrategy.getClass().getSimpleName(), exceptionhandler});
            AsyncLoggerConfigHelper.disruptor.start();
        }
    }

    private static WaitStrategy createWaitStrategy() {
        String s = System.getProperty("AsyncLoggerConfig.WaitStrategy");

        AsyncLoggerConfigHelper.LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", new Object[] { s});
        return (WaitStrategy) ("Sleep".equals(s) ? new SleepingWaitStrategy() : ("Yield".equals(s) ? new YieldingWaitStrategy() : ("Block".equals(s) ? new BlockingWaitStrategy() : new SleepingWaitStrategy())));
    }

    private static int calculateRingBufferSize() {
        int i = 262144;
        String s = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(i));

        try {
            int j = Integer.parseInt(s);

            if (j < 128) {
                j = 128;
                AsyncLoggerConfigHelper.LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[] { s, Integer.valueOf(128)});
            }

            i = j;
        } catch (Exception exception) {
            AsyncLoggerConfigHelper.LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[] { s, Integer.valueOf(i)});
        }

        return Util.ceilingNextPowerOfTwo(i);
    }

    private static ExceptionHandler getExceptionHandler() {
        String s = System.getProperty("AsyncLoggerConfig.ExceptionHandler");

        if (s == null) {
            return null;
        } else {
            try {
                Class oclass = Class.forName(s);
                ExceptionHandler exceptionhandler = (ExceptionHandler) oclass.newInstance();

                return exceptionhandler;
            } catch (Exception exception) {
                AsyncLoggerConfigHelper.LOGGER.debug("AsyncLoggerConfig.ExceptionHandler not set: error creating " + s + ": ", (Throwable) exception);
                return null;
            }
        }
    }

    static synchronized void claim() {
        ++AsyncLoggerConfigHelper.count;
        initDisruptor();
    }

    static synchronized void release() {
        if (--AsyncLoggerConfigHelper.count > 0) {
            AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.count)});
        } else {
            Disruptor disruptor = AsyncLoggerConfigHelper.disruptor;

            if (disruptor == null) {
                AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}.", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.count)});
            } else {
                AsyncLoggerConfigHelper.LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(AsyncLoggerConfigHelper.count)});
                AsyncLoggerConfigHelper.disruptor = null;
                disruptor.shutdown();
                RingBuffer ringbuffer = disruptor.getRingBuffer();

                for (int i = 0; i < 20 && !ringbuffer.hasAvailableCapacity(ringbuffer.getBufferSize()); ++i) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                }

                AsyncLoggerConfigHelper.executor.shutdown();
                AsyncLoggerConfigHelper.executor = null;
            }
        }
    }

    public void callAppendersFromAnotherThread(LogEvent logevent) {
        this.currentLogEvent.set(logevent);
        AsyncLoggerConfigHelper.disruptor.publishEvent(this.translator);
    }

    private static class Log4jEventWrapperHandler implements SequenceReportingEventHandler {

        private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
        private Sequence sequenceCallback;
        private int counter;

        private Log4jEventWrapperHandler() {}

        public void setSequenceCallback(Sequence sequence) {
            this.sequenceCallback = sequence;
        }

        public void onEvent(AsyncLoggerConfigHelper.Log4jEventWrapper asyncloggerconfighelper_log4jeventwrapper, long i, boolean flag) throws Exception {
            asyncloggerconfighelper_log4jeventwrapper.event.setEndOfBatch(flag);
            asyncloggerconfighelper_log4jeventwrapper.loggerConfig.asyncCallAppenders(asyncloggerconfighelper_log4jeventwrapper.event);
            asyncloggerconfighelper_log4jeventwrapper.clear();
            if (++this.counter > 50) {
                this.sequenceCallback.set(i);
                this.counter = 0;
            }

        }

        Log4jEventWrapperHandler(Object object) {
            this();
        }
    }

    private static class Log4jEventWrapper {

        private AsyncLoggerConfig loggerConfig;
        private LogEvent event;

        private Log4jEventWrapper() {}

        public void clear() {
            this.loggerConfig = null;
            this.event = null;
        }

        Log4jEventWrapper(Object object) {
            this();
        }
    }
}
