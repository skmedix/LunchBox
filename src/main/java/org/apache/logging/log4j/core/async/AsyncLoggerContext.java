package org.apache.logging.log4j.core.async;

import java.net.URI;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.MessageFactory;

public class AsyncLoggerContext extends LoggerContext {

    public AsyncLoggerContext(String s) {
        super(s);
    }

    public AsyncLoggerContext(String s, Object object) {
        super(s, object);
    }

    public AsyncLoggerContext(String s, Object object, URI uri) {
        super(s, object, uri);
    }

    public AsyncLoggerContext(String s, Object object, String s1) {
        super(s, object, s1);
    }

    protected Logger newInstance(LoggerContext loggercontext, String s, MessageFactory messagefactory) {
        return new AsyncLogger(loggercontext, s, messagefactory);
    }

    public void stop() {
        AsyncLogger.stop();
        super.stop();
    }
}
