package org.apache.logging.log4j.core.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public final class ReflectiveCallerClassUtility {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final boolean GET_CALLER_CLASS_SUPPORTED;
    private static final Method GET_CALLER_CLASS_METHOD;
    static final int JAVA_7U25_COMPENSATION_OFFSET;

    public static boolean isSupported() {
        return ReflectiveCallerClassUtility.GET_CALLER_CLASS_SUPPORTED;
    }

    public static Class getCaller(int i) {
        if (!ReflectiveCallerClassUtility.GET_CALLER_CLASS_SUPPORTED) {
            return null;
        } else {
            try {
                return (Class) ReflectiveCallerClassUtility.GET_CALLER_CLASS_METHOD.invoke((Object) null, new Object[] { Integer.valueOf(i + 1 + ReflectiveCallerClassUtility.JAVA_7U25_COMPENSATION_OFFSET)});
            } catch (IllegalAccessException illegalaccessexception) {
                ReflectiveCallerClassUtility.LOGGER.warn("Should not have failed to call getCallerClass.");
            } catch (InvocationTargetException invocationtargetexception) {
                ReflectiveCallerClassUtility.LOGGER.warn("Should not have failed to call getCallerClass.");
            }

            return null;
        }
    }

    static {
        Method method = null;
        byte b0 = 0;

        try {
            ClassLoader classloader = Loader.getClassLoader();
            Class oclass = classloader.loadClass("sun.reflect.Reflection");
            Method[] amethod = oclass.getMethods();
            Method[] amethod1 = amethod;
            int i = amethod.length;

            for (int j = 0; j < i; ++j) {
                Method method1 = amethod1[j];
                int k = method1.getModifiers();
                Class[] aclass = method1.getParameterTypes();

                if (method1.getName().equals("getCallerClass") && Modifier.isStatic(k) && aclass.length == 1 && aclass[0] == Integer.TYPE) {
                    method = method1;
                    break;
                }
            }

            if (method == null) {
                ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass does not exist.");
            } else {
                Object object = method.invoke((Object) null, new Object[] { Integer.valueOf(0)});

                if (object != null && object == oclass) {
                    object = method.invoke((Object) null, new Object[] { Integer.valueOf(1)});
                    if (object == oclass) {
                        b0 = 1;
                        ReflectiveCallerClassUtility.LOGGER.warn("sun.reflect.Reflection#getCallerClass is broken in Java 7u25. You should upgrade to 7u40. Using alternate stack offset to compensate.");
                    }
                } else {
                    method = null;
                    ReflectiveCallerClassUtility.LOGGER.warn("sun.reflect.Reflection#getCallerClass returned unexpected value of [{}] and is unusable. Will fall back to another option.", new Object[] { object});
                }
            }
        } catch (ClassNotFoundException classnotfoundexception) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection is not installed.");
        } catch (IllegalAccessException illegalaccessexception) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass is not accessible.");
        } catch (InvocationTargetException invocationtargetexception) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass is not supported.");
        }

        if (method == null) {
            GET_CALLER_CLASS_SUPPORTED = false;
            GET_CALLER_CLASS_METHOD = null;
            JAVA_7U25_COMPENSATION_OFFSET = -1;
        } else {
            GET_CALLER_CLASS_SUPPORTED = true;
            GET_CALLER_CLASS_METHOD = method;
            JAVA_7U25_COMPENSATION_OFFSET = b0;
        }

    }
}
