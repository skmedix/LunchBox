package org.apache.logging.log4j.core.helpers;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Loader {

    private static boolean ignoreTCL = false;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

    public static ClassLoader getClassLoader() {
        return getClassLoader(Loader.class, (Class) null);
    }

    public static ClassLoader getClassLoader(Class oclass, Class oclass1) {
        ClassLoader classloader = null;

        try {
            classloader = getTCL();
        } catch (Exception exception) {
            Loader.LOGGER.warn("Caught exception locating thread ClassLoader {}", new Object[] { exception.getMessage()});
        }

        ClassLoader classloader1 = oclass == null ? null : oclass.getClassLoader();
        ClassLoader classloader2 = oclass1 == null ? null : oclass1.getClass().getClassLoader();

        return isChild(classloader, classloader1) ? (isChild(classloader, classloader2) ? classloader : classloader2) : (isChild(classloader1, classloader2) ? classloader1 : classloader2);
    }

    public static URL getResource(String s, ClassLoader classloader) {
        try {
            ClassLoader classloader1 = getTCL();
            URL url;

            if (classloader1 != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using context classloader " + classloader1 + '.');
                url = classloader1.getResource(s);
                if (url != null) {
                    return url;
                }
            }

            classloader1 = Loader.class.getClassLoader();
            if (classloader1 != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using " + classloader1 + " class loader.");
                url = classloader1.getResource(s);
                if (url != null) {
                    return url;
                }
            }

            if (classloader != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using " + classloader + " class loader.");
                url = classloader.getResource(s);
                if (url != null) {
                    return url;
                }
            }
        } catch (Throwable throwable) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", throwable);
        }

        Loader.LOGGER.trace("Trying to find [" + s + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(s);
    }

    public static InputStream getResourceAsStream(String s, ClassLoader classloader) {
        try {
            ClassLoader classloader1 = getTCL();
            InputStream inputstream;

            if (classloader1 != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using context classloader " + classloader1 + '.');
                inputstream = classloader1.getResourceAsStream(s);
                if (inputstream != null) {
                    return inputstream;
                }
            }

            classloader1 = Loader.class.getClassLoader();
            if (classloader1 != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using " + classloader1 + " class loader.");
                inputstream = classloader1.getResourceAsStream(s);
                if (inputstream != null) {
                    return inputstream;
                }
            }

            if (classloader != null) {
                Loader.LOGGER.trace("Trying to find [" + s + "] using " + classloader + " class loader.");
                inputstream = classloader.getResourceAsStream(s);
                if (inputstream != null) {
                    return inputstream;
                }
            }
        } catch (Throwable throwable) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", throwable);
        }

        Loader.LOGGER.trace("Trying to find [" + s + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResourceAsStream(s);
    }

    private static ClassLoader getTCL() {
        ClassLoader classloader;

        if (System.getSecurityManager() == null) {
            classloader = Thread.currentThread().getContextClassLoader();
        } else {
            classloader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        }

        return classloader;
    }

    private static boolean isChild(ClassLoader classloader, ClassLoader classloader1) {
        if (classloader != null && classloader1 != null) {
            ClassLoader classloader2;

            for (classloader2 = classloader.getParent(); classloader2 != null && classloader2 != classloader1; classloader2 = classloader2.getParent()) {
                ;
            }

            return classloader2 != null;
        } else {
            return classloader != null;
        }
    }

    public static Class loadClass(String s) throws ClassNotFoundException {
        if (Loader.ignoreTCL) {
            return Class.forName(s);
        } else {
            try {
                return getTCL().loadClass(s);
            } catch (Throwable throwable) {
                return Class.forName(s);
            }
        }
    }

    static {
        String s = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", (String) null);

        if (s != null) {
            Loader.ignoreTCL = OptionConverter.toBoolean(s, true);
        }

    }
}
