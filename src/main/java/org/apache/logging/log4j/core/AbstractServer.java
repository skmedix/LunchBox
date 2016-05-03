package org.apache.logging.log4j.core;

import org.apache.logging.log4j.LogManager;

public class AbstractServer {

    private final LoggerContext context = (LoggerContext) LogManager.getContext(false);

    protected void log(LogEvent logevent) {
        Logger logger = this.context.getLogger(logevent.getLoggerName());

        if (logger.config.filter(logevent.getLevel(), logevent.getMarker(), logevent.getMessage(), logevent.getThrown())) {
            logger.config.logEvent(logevent);
        }

    }
}
