package org.apache.logging.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.AbstractLoggerWrapper;

public final class EventLogger {

    private static final String NAME = "EventLogger";
    public static final Marker EVENT_MARKER = MarkerManager.getMarker("EVENT");
    private static final String FQCN = EventLogger.class.getName();
    private static AbstractLoggerWrapper loggerWrapper;

    public static void logEvent(StructuredDataMessage structureddatamessage) {
        EventLogger.loggerWrapper.log(EventLogger.EVENT_MARKER, EventLogger.FQCN, Level.OFF, structureddatamessage, (Throwable) null);
    }

    public static void logEvent(StructuredDataMessage structureddatamessage, Level level) {
        EventLogger.loggerWrapper.log(EventLogger.EVENT_MARKER, EventLogger.FQCN, level, structureddatamessage, (Throwable) null);
    }

    static {
        Logger logger = LogManager.getLogger("EventLogger");

        if (!(logger instanceof AbstractLogger)) {
            throw new LoggingException("Logger returned must be based on AbstractLogger");
        } else {
            EventLogger.loggerWrapper = new AbstractLoggerWrapper((AbstractLogger) logger, "EventLogger", (MessageFactory) null);
        }
    }
}
