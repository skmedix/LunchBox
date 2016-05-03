package org.apache.logging.log4j;

import java.net.URI;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public class LogManager {

    private static LoggerContextFactory factory;
    private static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
    private static final Logger LOGGER = StatusLogger.getLogger();
    public static final String ROOT_LOGGER_NAME = "";

    private static String getClassName(int i) {
        return (new Throwable()).getStackTrace()[i].getClassName();
    }

    public static LoggerContext getContext() {
        return LogManager.factory.getContext(LogManager.class.getName(), (ClassLoader) null, true);
    }

    public static LoggerContext getContext(boolean flag) {
        return LogManager.factory.getContext(LogManager.class.getName(), (ClassLoader) null, flag);
    }

    public static LoggerContext getContext(ClassLoader classloader, boolean flag) {
        return LogManager.factory.getContext(LogManager.class.getName(), classloader, flag);
    }

    public static LoggerContext getContext(ClassLoader classloader, boolean flag, URI uri) {
        return LogManager.factory.getContext(LogManager.class.getName(), classloader, flag, uri);
    }

    protected static LoggerContext getContext(String s, boolean flag) {
        return LogManager.factory.getContext(s, (ClassLoader) null, flag);
    }

    protected static LoggerContext getContext(String s, ClassLoader classloader, boolean flag) {
        return LogManager.factory.getContext(s, classloader, flag);
    }

    public static LoggerContextFactory getFactory() {
        return LogManager.factory;
    }

    public static Logger getFormatterLogger(Class oclass) {
        return getLogger(oclass != null ? oclass.getName() : getClassName(2), (MessageFactory) StringFormatterMessageFactory.INSTANCE);
    }

    public static Logger getFormatterLogger(Object object) {
        return getLogger(object != null ? object.getClass().getName() : getClassName(2), (MessageFactory) StringFormatterMessageFactory.INSTANCE);
    }

    public static Logger getFormatterLogger(String s) {
        return getLogger(s != null ? s : getClassName(2), (MessageFactory) StringFormatterMessageFactory.INSTANCE);
    }

    public static Logger getLogger() {
        return getLogger(getClassName(2));
    }

    public static Logger getLogger(Class oclass) {
        return getLogger(oclass != null ? oclass.getName() : getClassName(2));
    }

    public static Logger getLogger(Class oclass, MessageFactory messagefactory) {
        return getLogger(oclass != null ? oclass.getName() : getClassName(2), messagefactory);
    }

    public static Logger getLogger(MessageFactory messagefactory) {
        return getLogger(getClassName(2), messagefactory);
    }

    public static Logger getLogger(Object object) {
        return getLogger(object != null ? object.getClass().getName() : getClassName(2));
    }

    public static Logger getLogger(Object object, MessageFactory messagefactory) {
        return getLogger(object != null ? object.getClass().getName() : getClassName(2), messagefactory);
    }

    public static Logger getLogger(String s) {
        String s1 = s != null ? s : getClassName(2);

        return LogManager.factory.getContext(LogManager.class.getName(), (ClassLoader) null, false).getLogger(s1);
    }

    public static Logger getLogger(String s, MessageFactory messagefactory) {
        String s1 = s != null ? s : getClassName(2);

        return LogManager.factory.getContext(LogManager.class.getName(), (ClassLoader) null, false).getLogger(s1, messagefactory);
    }

    protected static Logger getLogger(String s, String s1) {
        return LogManager.factory.getContext(s, (ClassLoader) null, false).getLogger(s1);
    }

    public static Logger getRootLogger() {
        return getLogger("");
    }

    static {
        PropertiesUtil propertiesutil = PropertiesUtil.getProperties();
        String s = propertiesutil.getStringProperty("log4j2.loggerContextFactory");
        ClassLoader classloader = ProviderUtil.findClassLoader();

        if (s != null) {
            try {
                Class oclass = classloader.loadClass(s);

                if (LoggerContextFactory.class.isAssignableFrom(oclass)) {
                    LogManager.factory = (LoggerContextFactory) oclass.newInstance();
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                LogManager.LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { s});
            } catch (Exception exception) {
                LogManager.LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { s, exception});
            }
        }

        if (LogManager.factory == null) {
            TreeMap treemap = new TreeMap();

            if (ProviderUtil.hasProviders()) {
                Iterator iterator = ProviderUtil.getProviders();

                while (iterator.hasNext()) {
                    Provider provider = (Provider) iterator.next();
                    String s1 = provider.getClassName();

                    if (s1 != null) {
                        try {
                            Class oclass1 = classloader.loadClass(s1);

                            if (LoggerContextFactory.class.isAssignableFrom(oclass1)) {
                                treemap.put(provider.getPriority(), (LoggerContextFactory) oclass1.newInstance());
                            } else {
                                LogManager.LOGGER.error(s1 + " does not implement " + LoggerContextFactory.class.getName());
                            }
                        } catch (ClassNotFoundException classnotfoundexception1) {
                            LogManager.LOGGER.error("Unable to locate class " + s1 + " specified in " + provider.getURL().toString(), (Throwable) classnotfoundexception1);
                        } catch (IllegalAccessException illegalaccessexception) {
                            LogManager.LOGGER.error("Unable to create class " + s1 + " specified in " + provider.getURL().toString(), (Throwable) illegalaccessexception);
                        } catch (Exception exception1) {
                            LogManager.LOGGER.error("Unable to create class " + s1 + " specified in " + provider.getURL().toString(), (Throwable) exception1);
                            exception1.printStackTrace();
                        }
                    }
                }

                if (treemap.size() == 0) {
                    LogManager.LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
                    LogManager.factory = new SimpleLoggerContextFactory();
                } else {
                    StringBuilder stringbuilder = new StringBuilder("Multiple logging implementations found: \n");
                    Iterator iterator1 = treemap.entrySet().iterator();

                    while (iterator1.hasNext()) {
                        Entry entry = (Entry) iterator1.next();

                        stringbuilder.append("Factory: ").append(((LoggerContextFactory) entry.getValue()).getClass().getName());
                        stringbuilder.append(", Weighting: ").append(entry.getKey()).append("\n");
                    }

                    LogManager.factory = (LoggerContextFactory) treemap.get(treemap.lastKey());
                    stringbuilder.append("Using factory: ").append(LogManager.factory.getClass().getName());
                    LogManager.LOGGER.warn(stringbuilder.toString());
                }
            } else {
                LogManager.LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
                LogManager.factory = new SimpleLoggerContextFactory();
            }
        }

    }
}
