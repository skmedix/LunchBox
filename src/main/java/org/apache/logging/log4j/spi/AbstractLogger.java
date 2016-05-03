package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLogger implements Logger {

    public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
    public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTRY", AbstractLogger.FLOW_MARKER);
    public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT", AbstractLogger.FLOW_MARKER);
    public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
    public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING", AbstractLogger.EXCEPTION_MARKER);
    public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING", AbstractLogger.EXCEPTION_MARKER);
    public static final Class DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
    private static final String FQCN = AbstractLogger.class.getName();
    private static final String THROWING = "throwing";
    private static final String CATCHING = "catching";
    private final String name;
    private final MessageFactory messageFactory;

    public AbstractLogger() {
        this.name = this.getClass().getName();
        this.messageFactory = this.createDefaultMessageFactory();
    }

    public AbstractLogger(String s) {
        this.name = s;
        this.messageFactory = this.createDefaultMessageFactory();
    }

    public AbstractLogger(String s, MessageFactory messagefactory) {
        this.name = s;
        this.messageFactory = messagefactory == null ? this.createDefaultMessageFactory() : messagefactory;
    }

    public static void checkMessageFactory(Logger logger, MessageFactory messagefactory) {
        String s = logger.getName();
        MessageFactory messagefactory1 = logger.getMessageFactory();

        if (messagefactory != null && !messagefactory1.equals(messagefactory)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", new Object[] { s, messagefactory1, messagefactory});
        } else if (messagefactory == null && !messagefactory1.getClass().equals(AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", new Object[] { s, messagefactory1, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName()});
        }

    }

    public void catching(Level level, Throwable throwable) {
        this.catching(AbstractLogger.FQCN, level, throwable);
    }

    public void catching(Throwable throwable) {
        this.catching(AbstractLogger.FQCN, Level.ERROR, throwable);
    }

    protected void catching(String s, Level level, Throwable throwable) {
        if (this.isEnabled(level, AbstractLogger.CATCHING_MARKER, (Object) null, (Throwable) null)) {
            this.log(AbstractLogger.CATCHING_MARKER, s, level, this.messageFactory.newMessage("catching"), throwable);
        }

    }

    private MessageFactory createDefaultMessageFactory() {
        try {
            return (MessageFactory) AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
        } catch (InstantiationException instantiationexception) {
            throw new IllegalStateException(instantiationexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalStateException(illegalaccessexception);
        }
    }

    public void debug(Marker marker, Message message) {
        if (this.isEnabled(Level.DEBUG, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, message, (Throwable) null);
        }

    }

    public void debug(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, message, throwable);
        }

    }

    public void debug(Marker marker, Object object) {
        if (this.isEnabled(Level.DEBUG, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void debug(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void debug(Marker marker, String s) {
        if (this.isEnabled(Level.DEBUG, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void debug(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.DEBUG, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, message, message.getThrowable());
        }

    }

    public void debug(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void debug(Message message) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, message, (Throwable) null);
        }

    }

    public void debug(Message message, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, message, throwable);
        }

    }

    public void debug(Object object) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void debug(Object object, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void debug(String s) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void debug(String s, Object... aobject) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, message, message.getThrowable());
        }

    }

    public void debug(String s, Throwable throwable) {
        if (this.isEnabled(Level.DEBUG, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void entry() {
        this.entry(AbstractLogger.FQCN, new Object[0]);
    }

    public void entry(Object... aobject) {
        this.entry(AbstractLogger.FQCN, aobject);
    }

    protected void entry(String s, Object... aobject) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object) null, (Throwable) null)) {
            this.log(AbstractLogger.ENTRY_MARKER, s, Level.TRACE, this.entryMsg(aobject.length, aobject), (Throwable) null);
        }

    }

    private Message entryMsg(int i, Object... aobject) {
        if (i == 0) {
            return this.messageFactory.newMessage("entry");
        } else {
            StringBuilder stringbuilder = new StringBuilder("entry params(");
            int j = 0;
            Object[] aobject1 = aobject;
            int k = aobject.length;

            for (int l = 0; l < k; ++l) {
                Object object = aobject1[l];

                if (object != null) {
                    stringbuilder.append(object.toString());
                } else {
                    stringbuilder.append("null");
                }

                ++j;
                if (j < aobject.length) {
                    stringbuilder.append(", ");
                }
            }

            stringbuilder.append(")");
            return this.messageFactory.newMessage(stringbuilder.toString());
        }
    }

    public void error(Marker marker, Message message) {
        if (this.isEnabled(Level.ERROR, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, message, (Throwable) null);
        }

    }

    public void error(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, message, throwable);
        }

    }

    public void error(Marker marker, Object object) {
        if (this.isEnabled(Level.ERROR, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void error(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void error(Marker marker, String s) {
        if (this.isEnabled(Level.ERROR, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void error(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.ERROR, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.ERROR, message, message.getThrowable());
        }

    }

    public void error(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void error(Message message) {
        if (this.isEnabled(Level.ERROR, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, message, (Throwable) null);
        }

    }

    public void error(Message message, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, message, throwable);
        }

    }

    public void error(Object object) {
        if (this.isEnabled(Level.ERROR, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void error(Object object, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void error(String s) {
        if (this.isEnabled(Level.ERROR, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void error(String s, Object... aobject) {
        if (this.isEnabled(Level.ERROR, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, message, message.getThrowable());
        }

    }

    public void error(String s, Throwable throwable) {
        if (this.isEnabled(Level.ERROR, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void exit() {
        this.exit(AbstractLogger.FQCN, (Object) null);
    }

    public Object exit(Object object) {
        return this.exit(AbstractLogger.FQCN, object);
    }

    protected Object exit(String s, Object object) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, (Object) null, (Throwable) null)) {
            this.log(AbstractLogger.EXIT_MARKER, s, Level.TRACE, this.toExitMsg(object), (Throwable) null);
        }

        return object;
    }

    public void fatal(Marker marker, Message message) {
        if (this.isEnabled(Level.FATAL, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, message, (Throwable) null);
        }

    }

    public void fatal(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, message, throwable);
        }

    }

    public void fatal(Marker marker, Object object) {
        if (this.isEnabled(Level.FATAL, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void fatal(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void fatal(Marker marker, String s) {
        if (this.isEnabled(Level.FATAL, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void fatal(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.FATAL, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.FATAL, message, message.getThrowable());
        }

    }

    public void fatal(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void fatal(Message message) {
        if (this.isEnabled(Level.FATAL, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, message, (Throwable) null);
        }

    }

    public void fatal(Message message, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, message, throwable);
        }

    }

    public void fatal(Object object) {
        if (this.isEnabled(Level.FATAL, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void fatal(Object object, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void fatal(String s) {
        if (this.isEnabled(Level.FATAL, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void fatal(String s, Object... aobject) {
        if (this.isEnabled(Level.FATAL, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, message, message.getThrowable());
        }

    }

    public void fatal(String s, Throwable throwable) {
        if (this.isEnabled(Level.FATAL, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(s), throwable);
        }

    }

    public MessageFactory getMessageFactory() {
        return this.messageFactory;
    }

    public String getName() {
        return this.name;
    }

    public void info(Marker marker, Message message) {
        if (this.isEnabled(Level.INFO, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, message, (Throwable) null);
        }

    }

    public void info(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.INFO, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, message, throwable);
        }

    }

    public void info(Marker marker, Object object) {
        if (this.isEnabled(Level.INFO, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void info(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.INFO, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void info(Marker marker, String s) {
        if (this.isEnabled(Level.INFO, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void info(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.INFO, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.INFO, message, message.getThrowable());
        }

    }

    public void info(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.INFO, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void info(Message message) {
        if (this.isEnabled(Level.INFO, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, message, (Throwable) null);
        }

    }

    public void info(Message message, Throwable throwable) {
        if (this.isEnabled(Level.INFO, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, message, throwable);
        }

    }

    public void info(Object object) {
        if (this.isEnabled(Level.INFO, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void info(Object object, Throwable throwable) {
        if (this.isEnabled(Level.INFO, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void info(String s) {
        if (this.isEnabled(Level.INFO, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void info(String s, Object... aobject) {
        if (this.isEnabled(Level.INFO, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, message, message.getThrowable());
        }

    }

    public void info(String s, Throwable throwable) {
        if (this.isEnabled(Level.INFO, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(s), throwable);
        }

    }

    public boolean isDebugEnabled() {
        return this.isEnabled(Level.DEBUG, (Marker) null, (String) null);
    }

    public boolean isDebugEnabled(Marker marker) {
        return this.isEnabled(Level.DEBUG, marker, (Object) null, (Throwable) null);
    }

    public boolean isEnabled(Level level) {
        return this.isEnabled(level, (Marker) null, (Object) null, (Throwable) null);
    }

    protected abstract boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable);

    protected abstract boolean isEnabled(Level level, Marker marker, Object object, Throwable throwable);

    protected abstract boolean isEnabled(Level level, Marker marker, String s);

    protected abstract boolean isEnabled(Level level, Marker marker, String s, Object... aobject);

    protected abstract boolean isEnabled(Level level, Marker marker, String s, Throwable throwable);

    public boolean isErrorEnabled() {
        return this.isEnabled(Level.ERROR, (Marker) null, (Object) null, (Throwable) null);
    }

    public boolean isErrorEnabled(Marker marker) {
        return this.isEnabled(Level.ERROR, marker, (Object) null, (Throwable) null);
    }

    public boolean isFatalEnabled() {
        return this.isEnabled(Level.FATAL, (Marker) null, (Object) null, (Throwable) null);
    }

    public boolean isFatalEnabled(Marker marker) {
        return this.isEnabled(Level.FATAL, marker, (Object) null, (Throwable) null);
    }

    public boolean isInfoEnabled() {
        return this.isEnabled(Level.INFO, (Marker) null, (Object) null, (Throwable) null);
    }

    public boolean isInfoEnabled(Marker marker) {
        return this.isEnabled(Level.INFO, marker, (Object) null, (Throwable) null);
    }

    public boolean isTraceEnabled() {
        return this.isEnabled(Level.TRACE, (Marker) null, (Object) null, (Throwable) null);
    }

    public boolean isTraceEnabled(Marker marker) {
        return this.isEnabled(Level.TRACE, marker, (Object) null, (Throwable) null);
    }

    public boolean isWarnEnabled() {
        return this.isEnabled(Level.WARN, (Marker) null, (Object) null, (Throwable) null);
    }

    public boolean isWarnEnabled(Marker marker) {
        return this.isEnabled(Level.WARN, marker, (Object) null, (Throwable) null);
    }

    public boolean isEnabled(Level level, Marker marker) {
        return this.isEnabled(level, marker, (Object) null, (Throwable) null);
    }

    public void log(Level level, Marker marker, Message message) {
        if (this.isEnabled(level, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, level, message, (Throwable) null);
        }

    }

    public void log(Level level, Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, level, message, throwable);
        }

    }

    public void log(Level level, Marker marker, Object object) {
        if (this.isEnabled(level, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void log(Level level, Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(level, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void log(Level level, Marker marker, String s) {
        if (this.isEnabled(level, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void log(Level level, Marker marker, String s, Object... aobject) {
        if (this.isEnabled(level, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, level, message, message.getThrowable());
        }

    }

    public void log(Level level, Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(level, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void log(Level level, Message message) {
        if (this.isEnabled(level, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, message, (Throwable) null);
        }

    }

    public void log(Level level, Message message, Throwable throwable) {
        if (this.isEnabled(level, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, message, throwable);
        }

    }

    public void log(Level level, Object object) {
        if (this.isEnabled(level, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void log(Level level, Object object, Throwable throwable) {
        if (this.isEnabled(level, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void log(Level level, String s) {
        if (this.isEnabled(level, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void log(Level level, String s, Object... aobject) {
        if (this.isEnabled(level, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, level, message, message.getThrowable());
        }

    }

    public void log(Level level, String s, Throwable throwable) {
        if (this.isEnabled(level, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void printf(Level level, String s, Object... aobject) {
        if (this.isEnabled(level, (Marker) null, s, aobject)) {
            StringFormattedMessage stringformattedmessage = new StringFormattedMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, level, stringformattedmessage, stringformattedmessage.getThrowable());
        }

    }

    public void printf(Level level, Marker marker, String s, Object... aobject) {
        if (this.isEnabled(level, marker, s, aobject)) {
            StringFormattedMessage stringformattedmessage = new StringFormattedMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, level, stringformattedmessage, stringformattedmessage.getThrowable());
        }

    }

    public abstract void log(Marker marker, String s, Level level, Message message, Throwable throwable);

    public Throwable throwing(Level level, Throwable throwable) {
        return this.throwing(AbstractLogger.FQCN, level, throwable);
    }

    public Throwable throwing(Throwable throwable) {
        return this.throwing(AbstractLogger.FQCN, Level.ERROR, throwable);
    }

    protected Throwable throwing(String s, Level level, Throwable throwable) {
        if (this.isEnabled(level, AbstractLogger.THROWING_MARKER, (Object) null, (Throwable) null)) {
            this.log(AbstractLogger.THROWING_MARKER, s, level, this.messageFactory.newMessage("throwing"), throwable);
        }

        return throwable;
    }

    private Message toExitMsg(Object object) {
        return object == null ? this.messageFactory.newMessage("exit") : this.messageFactory.newMessage("exit with(" + object + ")");
    }

    public String toString() {
        return this.name;
    }

    public void trace(Marker marker, Message message) {
        if (this.isEnabled(Level.TRACE, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, message, (Throwable) null);
        }

    }

    public void trace(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, message, throwable);
        }

    }

    public void trace(Marker marker, Object object) {
        if (this.isEnabled(Level.TRACE, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void trace(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void trace(Marker marker, String s) {
        if (this.isEnabled(Level.TRACE, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void trace(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.TRACE, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.TRACE, message, message.getThrowable());
        }

    }

    public void trace(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void trace(Message message) {
        if (this.isEnabled(Level.TRACE, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, message, (Throwable) null);
        }

    }

    public void trace(Message message, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, message, throwable);
        }

    }

    public void trace(Object object) {
        if (this.isEnabled(Level.TRACE, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void trace(Object object, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void trace(String s) {
        if (this.isEnabled(Level.TRACE, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void trace(String s, Object... aobject) {
        if (this.isEnabled(Level.TRACE, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, message, message.getThrowable());
        }

    }

    public void trace(String s, Throwable throwable) {
        if (this.isEnabled(Level.TRACE, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void warn(Marker marker, Message message) {
        if (this.isEnabled(Level.WARN, marker, message, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, message, (Throwable) null);
        }

    }

    public void warn(Marker marker, Message message, Throwable throwable) {
        if (this.isEnabled(Level.WARN, marker, message, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, message, throwable);
        }

    }

    public void warn(Marker marker, Object object) {
        if (this.isEnabled(Level.WARN, marker, object, (Throwable) null)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void warn(Marker marker, Object object, Throwable throwable) {
        if (this.isEnabled(Level.WARN, marker, object, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void warn(Marker marker, String s) {
        if (this.isEnabled(Level.WARN, marker, s)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void warn(Marker marker, String s, Object... aobject) {
        if (this.isEnabled(Level.WARN, marker, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log(marker, AbstractLogger.FQCN, Level.WARN, message, message.getThrowable());
        }

    }

    public void warn(Marker marker, String s, Throwable throwable) {
        if (this.isEnabled(Level.WARN, marker, s, throwable)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(s), throwable);
        }

    }

    public void warn(Message message) {
        if (this.isEnabled(Level.WARN, (Marker) null, message, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, message, (Throwable) null);
        }

    }

    public void warn(Message message, Throwable throwable) {
        if (this.isEnabled(Level.WARN, (Marker) null, message, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, message, throwable);
        }

    }

    public void warn(Object object) {
        if (this.isEnabled(Level.WARN, (Marker) null, object, (Throwable) null)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(object), (Throwable) null);
        }

    }

    public void warn(Object object, Throwable throwable) {
        if (this.isEnabled(Level.WARN, (Marker) null, object, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(object), throwable);
        }

    }

    public void warn(String s) {
        if (this.isEnabled(Level.WARN, (Marker) null, s)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(s), (Throwable) null);
        }

    }

    public void warn(String s, Object... aobject) {
        if (this.isEnabled(Level.WARN, (Marker) null, s, aobject)) {
            Message message = this.messageFactory.newMessage(s, aobject);

            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, message, message.getThrowable());
        }

    }

    public void warn(String s, Throwable throwable) {
        if (this.isEnabled(Level.WARN, (Marker) null, s, throwable)) {
            this.log((Marker) null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(s), throwable);
        }

    }
}
