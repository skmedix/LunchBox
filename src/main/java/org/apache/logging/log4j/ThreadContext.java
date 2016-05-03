package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public final class ThreadContext {

    public static final Map EMPTY_MAP = Collections.emptyMap();
    public static final ThreadContextStack EMPTY_STACK = new MutableThreadContextStack(new ArrayList());
    private static final String DISABLE_MAP = "disableThreadContextMap";
    private static final String DISABLE_STACK = "disableThreadContextStack";
    private static final String DISABLE_ALL = "disableThreadContext";
    private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
    private static boolean all;
    private static boolean useMap;
    private static boolean useStack;
    private static ThreadContextMap contextMap;
    private static ThreadContextStack contextStack;
    private static final Logger LOGGER = StatusLogger.getLogger();

    public static void put(String s, String s1) {
        ThreadContext.contextMap.put(s, s1);
    }

    public static String get(String s) {
        return ThreadContext.contextMap.get(s);
    }

    public static void remove(String s) {
        ThreadContext.contextMap.remove(s);
    }

    public static void clear() {
        ThreadContext.contextMap.clear();
    }

    public static boolean containsKey(String s) {
        return ThreadContext.contextMap.containsKey(s);
    }

    public static Map getContext() {
        return ThreadContext.contextMap.getCopy();
    }

    public static Map getImmutableContext() {
        Map map = ThreadContext.contextMap.getImmutableMapOrNull();

        return map == null ? ThreadContext.EMPTY_MAP : map;
    }

    public static boolean isEmpty() {
        return ThreadContext.contextMap.isEmpty();
    }

    public static void clearStack() {
        ThreadContext.contextStack.clear();
    }

    public static ThreadContext.ContextStack cloneStack() {
        return ThreadContext.contextStack.copy();
    }

    public static ThreadContext.ContextStack getImmutableStack() {
        return ThreadContext.contextStack;
    }

    public static void setStack(Collection collection) {
        if (collection.size() != 0 && ThreadContext.useStack) {
            ThreadContext.contextStack.clear();
            ThreadContext.contextStack.addAll(collection);
        }
    }

    public static int getDepth() {
        return ThreadContext.contextStack.getDepth();
    }

    public static String pop() {
        return ThreadContext.contextStack.pop();
    }

    public static String peek() {
        return ThreadContext.contextStack.peek();
    }

    public static void push(String s) {
        ThreadContext.contextStack.push(s);
    }

    public static void push(String s, Object... aobject) {
        ThreadContext.contextStack.push(ParameterizedMessage.format(s, aobject));
    }

    public static void removeStack() {
        ThreadContext.contextStack.clear();
    }

    public static void trim(int i) {
        ThreadContext.contextStack.trim(i);
    }

    static {
        PropertiesUtil propertiesutil = PropertiesUtil.getProperties();

        ThreadContext.all = propertiesutil.getBooleanProperty("disableThreadContext");
        ThreadContext.useStack = !propertiesutil.getBooleanProperty("disableThreadContextStack") && !ThreadContext.all;
        ThreadContext.contextStack = new DefaultThreadContextStack(ThreadContext.useStack);
        ThreadContext.useMap = !propertiesutil.getBooleanProperty("disableThreadContextMap") && !ThreadContext.all;
        String s = propertiesutil.getStringProperty("log4j2.threadContextMap");
        ClassLoader classloader = ProviderUtil.findClassLoader();

        if (s != null) {
            try {
                Class oclass = classloader.loadClass(s);

                if (ThreadContextMap.class.isAssignableFrom(oclass)) {
                    ThreadContext.contextMap = (ThreadContextMap) oclass.newInstance();
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                ThreadContext.LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { s});
            } catch (Exception exception) {
                ThreadContext.LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { s, exception});
            }
        }

        if (ThreadContext.contextMap == null && ProviderUtil.hasProviders()) {
            LoggerContextFactory loggercontextfactory = LogManager.getFactory();
            Iterator iterator = ProviderUtil.getProviders();

            while (iterator.hasNext()) {
                Provider provider = (Provider) iterator.next();

                s = provider.getThreadContextMap();
                String s1 = provider.getClassName();

                if (s != null && loggercontextfactory.getClass().getName().equals(s1)) {
                    try {
                        Class oclass1 = classloader.loadClass(s);

                        if (ThreadContextMap.class.isAssignableFrom(oclass1)) {
                            ThreadContext.contextMap = (ThreadContextMap) oclass1.newInstance();
                            break;
                        }
                    } catch (ClassNotFoundException classnotfoundexception1) {
                        ThreadContext.LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { s});
                        ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
                    } catch (Exception exception1) {
                        ThreadContext.LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { s, exception1});
                        ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
                    }
                }
            }
        }

        if (ThreadContext.contextMap == null) {
            ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
        }

    }

    public interface ContextStack extends Serializable {

        void clear();

        String pop();

        String peek();

        void push(String s);

        int getDepth();

        List asList();

        void trim(int i);

        ThreadContext.ContextStack copy();
    }
}
