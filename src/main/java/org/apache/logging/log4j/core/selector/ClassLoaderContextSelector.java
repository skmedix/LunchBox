package org.apache.logging.log4j.core.selector;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
import org.apache.logging.log4j.status.StatusLogger;

public class ClassLoaderContextSelector implements ContextSelector {

    private static final AtomicReference CONTEXT = new AtomicReference();
    private static final ClassLoaderContextSelector.PrivateSecurityManager SECURITY_MANAGER;
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private static final ConcurrentMap CONTEXT_MAP = new ConcurrentHashMap();

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag) {
        return this.getContext(s, classloader, flag, (URI) null);
    }

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri) {
        if (flag) {
            LoggerContext loggercontext = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

            return loggercontext != null ? loggercontext : this.getDefault();
        } else if (classloader != null) {
            return this.locateContext(classloader, uri);
        } else {
            Class oclass;
            boolean flag1;

            if (ReflectiveCallerClassUtility.isSupported()) {
                try {
                    oclass = Class.class;
                    flag1 = false;

                    for (int i = 2; oclass != null; ++i) {
                        oclass = ReflectiveCallerClassUtility.getCaller(i);
                        if (oclass == null) {
                            break;
                        }

                        if (oclass.getName().equals(s)) {
                            flag1 = true;
                        } else if (flag1) {
                            break;
                        }
                    }

                    if (oclass != null) {
                        return this.locateContext(oclass.getClassLoader(), uri);
                    }
                } catch (Exception exception) {
                    ;
                }
            }

            if (ClassLoaderContextSelector.SECURITY_MANAGER != null) {
                oclass = ClassLoaderContextSelector.SECURITY_MANAGER.getCaller(s);
                if (oclass != null) {
                    ClassLoader classloader1 = oclass.getClassLoader() != null ? oclass.getClassLoader() : ClassLoader.getSystemClassLoader();

                    return this.locateContext(classloader1, uri);
                }
            }

            Throwable throwable = new Throwable();

            flag1 = false;
            String s1 = null;
            StackTraceElement[] astacktraceelement = throwable.getStackTrace();
            int j = astacktraceelement.length;

            for (int k = 0; k < j; ++k) {
                StackTraceElement stacktraceelement = astacktraceelement[k];

                if (stacktraceelement.getClassName().equals(s)) {
                    flag1 = true;
                } else if (flag1) {
                    s1 = stacktraceelement.getClassName();
                    break;
                }
            }

            if (s1 != null) {
                try {
                    return this.locateContext(Loader.loadClass(s1).getClassLoader(), uri);
                } catch (ClassNotFoundException classnotfoundexception) {
                    ;
                }
            }

            LoggerContext loggercontext1 = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

            return loggercontext1 != null ? loggercontext1 : this.getDefault();
        }
    }

    public void removeContext(LoggerContext loggercontext) {
        Iterator iterator = ClassLoaderContextSelector.CONTEXT_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            LoggerContext loggercontext1 = (LoggerContext) ((WeakReference) ((AtomicReference) entry.getValue()).get()).get();

            if (loggercontext1 == loggercontext) {
                ClassLoaderContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }

    }

    public List getLoggerContexts() {
        ArrayList arraylist = new ArrayList();
        Collection collection = ClassLoaderContextSelector.CONTEXT_MAP.values();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            AtomicReference atomicreference = (AtomicReference) iterator.next();
            LoggerContext loggercontext = (LoggerContext) ((WeakReference) atomicreference.get()).get();

            if (loggercontext != null) {
                arraylist.add(loggercontext);
            }
        }

        return Collections.unmodifiableList(arraylist);
    }

    private LoggerContext locateContext(ClassLoader classloader, URI uri) {
        String s = classloader.toString();
        AtomicReference atomicreference = (AtomicReference) ClassLoaderContextSelector.CONTEXT_MAP.get(s);

        if (atomicreference == null) {
            if (uri == null) {
                for (ClassLoader classloader1 = classloader.getParent(); classloader1 != null; classloader1 = classloader1.getParent()) {
                    atomicreference = (AtomicReference) ClassLoaderContextSelector.CONTEXT_MAP.get(classloader1.toString());
                    if (atomicreference != null) {
                        WeakReference weakreference = (WeakReference) atomicreference.get();
                        LoggerContext loggercontext = (LoggerContext) weakreference.get();

                        if (loggercontext != null) {
                            return loggercontext;
                        }
                    }
                }
            }

            LoggerContext loggercontext1 = new LoggerContext(s, (Object) null, uri);
            AtomicReference atomicreference1 = new AtomicReference();

            atomicreference1.set(new WeakReference(loggercontext1));
            ClassLoaderContextSelector.CONTEXT_MAP.putIfAbsent(classloader.toString(), atomicreference1);
            loggercontext1 = (LoggerContext) ((WeakReference) ((AtomicReference) ClassLoaderContextSelector.CONTEXT_MAP.get(s)).get()).get();
            return loggercontext1;
        } else {
            WeakReference weakreference1 = (WeakReference) atomicreference.get();
            LoggerContext loggercontext2 = (LoggerContext) weakreference1.get();

            if (loggercontext2 == null) {
                loggercontext2 = new LoggerContext(s, (Object) null, uri);
                atomicreference.compareAndSet(weakreference1, new WeakReference(loggercontext2));
                return loggercontext2;
            } else {
                if (loggercontext2.getConfigLocation() == null && uri != null) {
                    ClassLoaderContextSelector.LOGGER.debug("Setting configuration to {}", new Object[] { uri});
                    loggercontext2.setConfigLocation(uri);
                } else if (loggercontext2.getConfigLocation() != null && uri != null && !loggercontext2.getConfigLocation().equals(uri)) {
                    ClassLoaderContextSelector.LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", new Object[] { uri, loggercontext2.getConfigLocation()});
                }

                return loggercontext2;
            }
        }
    }

    private LoggerContext getDefault() {
        LoggerContext loggercontext = (LoggerContext) ClassLoaderContextSelector.CONTEXT.get();

        if (loggercontext != null) {
            return loggercontext;
        } else {
            ClassLoaderContextSelector.CONTEXT.compareAndSet((Object) null, new LoggerContext("Default"));
            return (LoggerContext) ClassLoaderContextSelector.CONTEXT.get();
        }
    }

    static {
        if (ReflectiveCallerClassUtility.isSupported()) {
            SECURITY_MANAGER = null;
        } else {
            ClassLoaderContextSelector.PrivateSecurityManager classloadercontextselector_privatesecuritymanager;

            try {
                classloadercontextselector_privatesecuritymanager = new ClassLoaderContextSelector.PrivateSecurityManager((ClassLoaderContextSelector.SyntheticClass_1) null);
                if (classloadercontextselector_privatesecuritymanager.getCaller(ClassLoaderContextSelector.class.getName()) == null) {
                    classloadercontextselector_privatesecuritymanager = null;
                    ClassLoaderContextSelector.LOGGER.error("Unable to obtain call stack from security manager.");
                }
            } catch (Exception exception) {
                classloadercontextselector_privatesecuritymanager = null;
                ClassLoaderContextSelector.LOGGER.debug("Unable to install security manager", (Throwable) exception);
            }

            SECURITY_MANAGER = classloadercontextselector_privatesecuritymanager;
        }

    }

    static class SyntheticClass_1 {    }

    private static class PrivateSecurityManager extends SecurityManager {

        private PrivateSecurityManager() {}

        public Class getCaller(String s) {
            Class[] aclass = this.getClassContext();
            boolean flag = false;
            Class[] aclass1 = aclass;
            int i = aclass.length;

            for (int j = 0; j < i; ++j) {
                Class oclass = aclass1[j];

                if (oclass.getName().equals(s)) {
                    flag = true;
                } else if (flag) {
                    return oclass;
                }
            }

            return null;
        }

        PrivateSecurityManager(ClassLoaderContextSelector.SyntheticClass_1 classloadercontextselector_syntheticclass_1) {
            this();
        }
    }
}
