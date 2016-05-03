package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Clock;
import org.apache.logging.log4j.core.helpers.ClockFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class AsyncLogger extends Logger {

    private static final int HALF_A_SECOND = 500;
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private static volatile Disruptor disruptor;
    private static Clock clock = ClockFactory.getClock();
    private static ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncLogger-"));
    private final ThreadLocal threadlocalInfo = new ThreadLocal();

    private static int calculateRingBufferSize() {
        int i = 262144;
        String s = System.getProperty("AsyncLogger.RingBufferSize", String.valueOf(i));

        try {
            int j = Integer.parseInt(s);

            if (j < 128) {
                j = 128;
                AsyncLogger.LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[] { s, Integer.valueOf(128)});
            }

            i = j;
        } catch (Exception exception) {
            AsyncLogger.LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[] { s, Integer.valueOf(i)});
        }

        return Util.ceilingNextPowerOfTwo(i);
    }

    private static WaitStrategy createWaitStrategy() {
        String s = System.getProperty("AsyncLogger.WaitStrategy");

        AsyncLogger.LOGGER.debug("property AsyncLogger.WaitStrategy={}", new Object[] { s});
        if ("Sleep".equals(s)) {
            AsyncLogger.LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
            return new SleepingWaitStrategy();
        } else if ("Yield".equals(s)) {
            AsyncLogger.LOGGER.debug("disruptor event handler uses YieldingWaitStrategy");
            return new YieldingWaitStrategy();
        } else if ("Block".equals(s)) {
            AsyncLogger.LOGGER.debug("disruptor event handler uses BlockingWaitStrategy");
            return new BlockingWaitStrategy();
        } else {
            AsyncLogger.LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
            return new SleepingWaitStrategy();
        }
    }

    private static ExceptionHandler getExceptionHandler() {
        String s = System.getProperty("AsyncLogger.ExceptionHandler");

        if (s == null) {
            AsyncLogger.LOGGER.debug("No AsyncLogger.ExceptionHandler specified");
            return null;
        } else {
            try {
                Class oclass = Class.forName(s);
                ExceptionHandler exceptionhandler = (ExceptionHandler) oclass.newInstance();

                AsyncLogger.LOGGER.debug("AsyncLogger.ExceptionHandler=" + exceptionhandler);
                return exceptionhandler;
            } catch (Exception exception) {
                AsyncLogger.LOGGER.debug("AsyncLogger.ExceptionHandler not set: error creating " + s + ": ", (Throwable) exception);
                return null;
            }
        }
    }

    public AsyncLogger(LoggerContext loggercontext, String s, MessageFactory messagefactory) {
        super(loggercontext, s, messagefactory);
    }

    public void log(Marker marker, String s, Level level, Message message, Throwable throwable) {
        AsyncLogger.Info asynclogger_info = (AsyncLogger.Info) this.threadlocalInfo.get();

        if (asynclogger_info == null) {
            asynclogger_info = new AsyncLogger.Info((AsyncLogger.SyntheticClass_1) null);
            asynclogger_info.translator = new RingBufferLogEventTranslator();
            asynclogger_info.cachedThreadName = Thread.currentThread().getName();
            this.threadlocalInfo.set(asynclogger_info);
        }

        boolean flag = this.config.loggerConfig.isIncludeLocation();

        asynclogger_info.translator.setValues(this, this.getName(), marker, s, level, message, throwable, ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack(), asynclogger_info.cachedThreadName, flag ? this.location(s) : null, AsyncLogger.clock.currentTimeMillis());
        AsyncLogger.disruptor.publishEvent(asynclogger_info.translator);
    }

    private StackTraceElement location(String s) {
        return Log4jLogEvent.calcLocation(s);
    }

    public void actualAsyncLog(RingBufferLogEvent ringbufferlogevent) {
        Map map = this.config.loggerConfig.getProperties();

        ringbufferlogevent.mergePropertiesIntoContextMap(map, this.config.config.getStrSubstitutor());
        this.config.logEvent(ringbufferlogevent);
    }

    public static void stop() {
        Disruptor disruptor = AsyncLogger.disruptor;

        AsyncLogger.disruptor = null;
        disruptor.shutdown();
        RingBuffer ringbuffer = disruptor.getRingBuffer();

        for (int i = 0; i < 20 && !ringbuffer.hasAvailableCapacity(ringbuffer.getBufferSize()); ++i) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }

        AsyncLogger.executor.shutdown();
    }

    static {
        int i = calculateRingBufferSize();
        WaitStrategy waitstrategy = createWaitStrategy();

        AsyncLogger.disruptor = new Disruptor(RingBufferLogEvent.FACTORY, i, AsyncLogger.executor, ProducerType.MULTI, waitstrategy);
        RingBufferLogEventHandler[] aringbufferlogeventhandler = new RingBufferLogEventHandler[] { new RingBufferLogEventHandler()};

        AsyncLogger.disruptor.handleExceptionsWith(getExceptionHandler());
        AsyncLogger.disruptor.handleEventsWith(aringbufferlogeventhandler);
        AsyncLogger.LOGGER.debug("Starting AsyncLogger disruptor with ringbuffer size {}...", new Object[] { Integer.valueOf(AsyncLogger.disruptor.getRingBuffer().getBufferSize())});
        AsyncLogger.disruptor.start();
    }

    static class SyntheticClass_1 {    }

    private static class Info {

        private RingBufferLogEventTranslator translator;
        private String cachedThreadName;

        private Info() {}

        Info(AsyncLogger.SyntheticClass_1 asynclogger_syntheticclass_1) {
            this();
        }
    }
}
