package org.apache.logging.log4j.core.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class ThrowableProxy implements Serializable {

    private static final long serialVersionUID = -2752771578252251910L;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final ThrowableProxy.PrivateSecurityManager SECURITY_MANAGER;
    private static final Method GET_SUPPRESSED;
    private static final Method ADD_SUPPRESSED;
    private final ThrowableProxy proxyCause;
    private final Throwable throwable;
    private final String name;
    private final StackTracePackageElement[] callerPackageData;
    private int commonElementCount;

    public ThrowableProxy(Throwable throwable) {
        this.throwable = throwable;
        this.name = throwable.getClass().getName();
        HashMap hashmap = new HashMap();
        Stack stack = this.getCurrentStack();

        this.callerPackageData = this.resolvePackageData(stack, hashmap, (StackTraceElement[]) null, throwable.getStackTrace());
        this.proxyCause = throwable.getCause() == null ? null : new ThrowableProxy(throwable, stack, hashmap, throwable.getCause());
        this.setSuppressed(throwable);
    }

    private ThrowableProxy(Throwable throwable, Stack stack, Map map, Throwable throwable1) {
        this.throwable = throwable1;
        this.name = throwable1.getClass().getName();
        this.callerPackageData = this.resolvePackageData(stack, map, throwable.getStackTrace(), throwable1.getStackTrace());
        this.proxyCause = throwable1.getCause() == null ? null : new ThrowableProxy(throwable, stack, map, throwable1.getCause());
        this.setSuppressed(throwable1);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public ThrowableProxy getCause() {
        return this.proxyCause;
    }

    public String getName() {
        return this.name;
    }

    public int getCommonElementCount() {
        return this.commonElementCount;
    }

    public StackTracePackageElement[] getPackageData() {
        return this.callerPackageData;
    }

    public String toString() {
        String s = this.throwable.getMessage();

        return s != null ? this.name + ": " + s : this.name;
    }

    public String getRootCauseStackTrace() {
        return this.getRootCauseStackTrace((List) null);
    }

    public String getRootCauseStackTrace(List list) {
        StringBuilder stringbuilder = new StringBuilder();

        if (this.proxyCause != null) {
            this.formatWrapper(stringbuilder, this.proxyCause);
            stringbuilder.append("Wrapped by: ");
        }

        stringbuilder.append(this.toString());
        stringbuilder.append("\n");
        this.formatElements(stringbuilder, 0, this.throwable.getStackTrace(), this.callerPackageData, list);
        return stringbuilder.toString();
    }

    public void formatWrapper(StringBuilder stringbuilder, ThrowableProxy throwableproxy) {
        this.formatWrapper(stringbuilder, throwableproxy, (List) null);
    }

    public void formatWrapper(StringBuilder stringbuilder, ThrowableProxy throwableproxy, List list) {
        Throwable throwable = throwableproxy.getCause() != null ? throwableproxy.getCause().getThrowable() : null;

        if (throwable != null) {
            this.formatWrapper(stringbuilder, throwableproxy.proxyCause);
            stringbuilder.append("Wrapped by: ");
        }

        stringbuilder.append(throwableproxy).append("\n");
        this.formatElements(stringbuilder, throwableproxy.commonElementCount, throwableproxy.getThrowable().getStackTrace(), throwableproxy.callerPackageData, list);
    }

    public String getExtendedStackTrace() {
        return this.getExtendedStackTrace((List) null);
    }

    public String getExtendedStackTrace(List list) {
        StringBuilder stringbuilder = new StringBuilder(this.name);
        String s = this.throwable.getMessage();

        if (s != null) {
            stringbuilder.append(": ").append(this.throwable.getMessage());
        }

        stringbuilder.append("\n");
        this.formatElements(stringbuilder, 0, this.throwable.getStackTrace(), this.callerPackageData, list);
        if (this.proxyCause != null) {
            this.formatCause(stringbuilder, this.proxyCause, list);
        }

        return stringbuilder.toString();
    }

    public String getSuppressedStackTrace() {
        ThrowableProxy[] athrowableproxy = this.getSuppressed();

        if (athrowableproxy != null && athrowableproxy.length != 0) {
            StringBuilder stringbuilder = new StringBuilder("Suppressed Stack Trace Elements:\n");
            ThrowableProxy[] athrowableproxy1 = athrowableproxy;
            int i = athrowableproxy.length;

            for (int j = 0; j < i; ++j) {
                ThrowableProxy throwableproxy = athrowableproxy1[j];

                stringbuilder.append(throwableproxy.getExtendedStackTrace());
            }

            return stringbuilder.toString();
        } else {
            return "";
        }
    }

    private void formatCause(StringBuilder stringbuilder, ThrowableProxy throwableproxy, List list) {
        stringbuilder.append("Caused by: ").append(throwableproxy).append("\n");
        this.formatElements(stringbuilder, throwableproxy.commonElementCount, throwableproxy.getThrowable().getStackTrace(), throwableproxy.callerPackageData, list);
        if (throwableproxy.getCause() != null) {
            this.formatCause(stringbuilder, throwableproxy.proxyCause, list);
        }

    }

    private void formatElements(StringBuilder stringbuilder, int i, StackTraceElement[] astacktraceelement, StackTracePackageElement[] astacktracepackageelement, List list) {
        int j;

        if (list != null && list.size() != 0) {
            j = 0;

            for (int k = 0; k < astacktracepackageelement.length; ++k) {
                if (!this.isSuppressed(astacktraceelement[k], list)) {
                    if (j > 0) {
                        if (j == 1) {
                            stringbuilder.append("\t....\n");
                        } else {
                            stringbuilder.append("\t... suppressed ").append(j).append(" lines\n");
                        }

                        j = 0;
                    }

                    this.formatEntry(astacktraceelement[k], astacktracepackageelement[k], stringbuilder);
                } else {
                    ++j;
                }
            }

            if (j > 0) {
                if (j == 1) {
                    stringbuilder.append("\t...\n");
                } else {
                    stringbuilder.append("\t... suppressed ").append(j).append(" lines\n");
                }
            }
        } else {
            for (j = 0; j < astacktracepackageelement.length; ++j) {
                this.formatEntry(astacktraceelement[j], astacktracepackageelement[j], stringbuilder);
            }
        }

        if (i != 0) {
            stringbuilder.append("\t... ").append(i).append(" more").append("\n");
        }

    }

    private void formatEntry(StackTraceElement stacktraceelement, StackTracePackageElement stacktracepackageelement, StringBuilder stringbuilder) {
        stringbuilder.append("\tat ");
        stringbuilder.append(stacktraceelement);
        stringbuilder.append(" ");
        stringbuilder.append(stacktracepackageelement);
        stringbuilder.append("\n");
    }

    private boolean isSuppressed(StackTraceElement stacktraceelement, List list) {
        String s = stacktraceelement.getClassName();
        Iterator iterator = list.iterator();

        String s1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            s1 = (String) iterator.next();
        } while (!s.startsWith(s1));

        return true;
    }

    private Stack getCurrentStack() {
        if (ReflectiveCallerClassUtility.isSupported()) {
            Stack stack = new Stack();
            int i = 1;

            for (Class oclass = ReflectiveCallerClassUtility.getCaller(i); oclass != null; oclass = ReflectiveCallerClassUtility.getCaller(i)) {
                stack.push(oclass);
                ++i;
            }

            return stack;
        } else if (ThrowableProxy.SECURITY_MANAGER == null) {
            return new Stack();
        } else {
            Class[] aclass = ThrowableProxy.SECURITY_MANAGER.getClasses();
            Stack stack1 = new Stack();
            Class[] aclass1 = aclass;
            int j = aclass.length;

            for (int k = 0; k < j; ++k) {
                Class oclass1 = aclass1[k];

                stack1.push(oclass1);
            }

            return stack1;
        }
    }

    StackTracePackageElement[] resolvePackageData(Stack stack, Map map, StackTraceElement[] astacktraceelement, StackTraceElement[] astacktraceelement1) {
        int i;

        if (astacktraceelement != null) {
            int j = astacktraceelement.length - 1;

            int k;

            for (k = astacktraceelement1.length - 1; j >= 0 && k >= 0 && astacktraceelement[j].equals(astacktraceelement1[k]); --k) {
                --j;
            }

            this.commonElementCount = astacktraceelement1.length - 1 - k;
            i = k + 1;
        } else {
            this.commonElementCount = 0;
            i = astacktraceelement1.length;
        }

        StackTracePackageElement[] astacktracepackageelement = new StackTracePackageElement[i];
        Class oclass = stack.isEmpty() ? null : (Class) stack.peek();
        ClassLoader classloader = null;

        for (int l = i - 1; l >= 0; --l) {
            String s = astacktraceelement1[l].getClassName();
            ThrowableProxy.CacheEntry throwableproxy_cacheentry;

            if (oclass != null && s.equals(oclass.getName())) {
                throwableproxy_cacheentry = this.resolvePackageElement(oclass, true);
                astacktracepackageelement[l] = throwableproxy_cacheentry.element;
                classloader = throwableproxy_cacheentry.loader;
                stack.pop();
                oclass = stack.isEmpty() ? null : (Class) stack.peek();
            } else if (map.containsKey(s)) {
                throwableproxy_cacheentry = (ThrowableProxy.CacheEntry) map.get(s);
                astacktracepackageelement[l] = throwableproxy_cacheentry.element;
                if (throwableproxy_cacheentry.loader != null) {
                    classloader = throwableproxy_cacheentry.loader;
                }
            } else {
                throwableproxy_cacheentry = this.resolvePackageElement(this.loadClass(classloader, s), false);
                astacktracepackageelement[l] = throwableproxy_cacheentry.element;
                map.put(s, throwableproxy_cacheentry);
                if (throwableproxy_cacheentry.loader != null) {
                    classloader = throwableproxy_cacheentry.loader;
                }
            }
        }

        return astacktracepackageelement;
    }

    private ThrowableProxy.CacheEntry resolvePackageElement(Class oclass, boolean flag) {
        String s = "?";
        String s1 = "?";
        ClassLoader classloader = null;

        if (oclass != null) {
            try {
                CodeSource codesource = oclass.getProtectionDomain().getCodeSource();

                if (codesource != null) {
                    URL url = codesource.getLocation();

                    if (url != null) {
                        String s2 = url.toString().replace('\\', '/');
                        int i = s2.lastIndexOf("/");

                        if (i >= 0 && i == s2.length() - 1) {
                            i = s2.lastIndexOf("/", i - 1);
                            s = s2.substring(i + 1);
                        } else {
                            s = s2.substring(i + 1);
                        }
                    }
                }
            } catch (Exception exception) {
                ;
            }

            Package package = oclass.getPackage();

            if (package != null) {
                String s3 = package.getImplementationVersion();

                if (s3 != null) {
                    s1 = s3;
                }
            }

            classloader = oclass.getClassLoader();
        }

        return new ThrowableProxy.CacheEntry(new StackTracePackageElement(s, s1, flag), classloader);
    }

    private Class loadClass(ClassLoader classloader, String s) {
        Class oclass;

        if (classloader != null) {
            try {
                oclass = classloader.loadClass(s);
                if (oclass != null) {
                    return oclass;
                }
            } catch (Exception exception) {
                ;
            }
        }

        try {
            oclass = Thread.currentThread().getContextClassLoader().loadClass(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            try {
                oclass = Class.forName(s);
            } catch (ClassNotFoundException classnotfoundexception1) {
                try {
                    oclass = this.getClass().getClassLoader().loadClass(s);
                } catch (ClassNotFoundException classnotfoundexception2) {
                    return null;
                }
            }
        }

        return oclass;
    }

    public ThrowableProxy[] getSuppressed() {
        if (ThrowableProxy.GET_SUPPRESSED != null) {
            try {
                return (ThrowableProxy[]) ((ThrowableProxy[]) ThrowableProxy.GET_SUPPRESSED.invoke(this.throwable, new Object[0]));
            } catch (Exception exception) {
                return null;
            }
        } else {
            return null;
        }
    }

    private void setSuppressed(Throwable throwable) {
        if (ThrowableProxy.GET_SUPPRESSED != null && ThrowableProxy.ADD_SUPPRESSED != null) {
            try {
                Throwable[] athrowable = (Throwable[]) ((Throwable[]) ThrowableProxy.GET_SUPPRESSED.invoke(throwable, new Object[0]));
                Throwable[] athrowable1 = athrowable;
                int i = athrowable.length;

                for (int j = 0; j < i; ++j) {
                    Throwable throwable1 = athrowable1[j];

                    ThrowableProxy.ADD_SUPPRESSED.invoke(this, new Object[] { new ThrowableProxy(throwable1)});
                }
            } catch (Exception exception) {
                ;
            }
        }

    }

    static {
        if (ReflectiveCallerClassUtility.isSupported()) {
            SECURITY_MANAGER = null;
        } else {
            ThrowableProxy.PrivateSecurityManager throwableproxy_privatesecuritymanager;

            try {
                throwableproxy_privatesecuritymanager = new ThrowableProxy.PrivateSecurityManager((ThrowableProxy.SyntheticClass_1) null);
                if (throwableproxy_privatesecuritymanager.getClasses() == null) {
                    throwableproxy_privatesecuritymanager = null;
                    ThrowableProxy.LOGGER.error("Unable to obtain call stack from security manager.");
                }
            } catch (Exception exception) {
                throwableproxy_privatesecuritymanager = null;
                ThrowableProxy.LOGGER.debug("Unable to install security manager.", (Throwable) exception);
            }

            SECURITY_MANAGER = throwableproxy_privatesecuritymanager;
        }

        Method method = null;
        Method method1 = null;
        Method[] amethod = Throwable.class.getMethods();
        Method[] amethod1 = amethod;
        int i = amethod.length;

        for (int j = 0; j < i; ++j) {
            Method method2 = amethod1[j];

            if (method2.getName().equals("getSuppressed")) {
                method = method2;
            } else if (method2.getName().equals("addSuppressed")) {
                method1 = method2;
            }
        }

        GET_SUPPRESSED = method;
        ADD_SUPPRESSED = method1;
    }

    static class SyntheticClass_1 {    }

    private static class PrivateSecurityManager extends SecurityManager {

        private PrivateSecurityManager() {}

        public Class[] getClasses() {
            return this.getClassContext();
        }

        PrivateSecurityManager(ThrowableProxy.SyntheticClass_1 throwableproxy_syntheticclass_1) {
            this();
        }
    }

    class CacheEntry {

        private final StackTracePackageElement element;
        private final ClassLoader loader;

        public CacheEntry(StackTracePackageElement stacktracepackageelement, ClassLoader classloader) {
            this.element = stacktracepackageelement;
            this.loader = classloader;
        }
    }
}
