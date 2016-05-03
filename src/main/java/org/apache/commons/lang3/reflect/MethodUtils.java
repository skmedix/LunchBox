package org.apache.commons.lang3.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class MethodUtils {

    public static Object invokeMethod(Object object, String s, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeMethod(object, s, aobject, aclass);
    }

    public static Object invokeMethod(Object object, String s, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aclass = ArrayUtils.nullToEmpty(aclass);
        aobject = ArrayUtils.nullToEmpty(aobject);
        Method method = getMatchingAccessibleMethod(object.getClass(), s, aclass);

        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + s + "() on object: " + object.getClass().getName());
        } else {
            return method.invoke(object, aobject);
        }
    }

    public static Object invokeExactMethod(Object object, String s, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeExactMethod(object, s, aobject, aclass);
    }

    public static Object invokeExactMethod(Object object, String s, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        aclass = ArrayUtils.nullToEmpty(aclass);
        Method method = getAccessibleMethod(object.getClass(), s, aclass);

        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + s + "() on object: " + object.getClass().getName());
        } else {
            return method.invoke(object, aobject);
        }
    }

    public static Object invokeExactStaticMethod(Class oclass, String s, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        aclass = ArrayUtils.nullToEmpty(aclass);
        Method method = getAccessibleMethod(oclass, s, aclass);

        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + s + "() on class: " + oclass.getName());
        } else {
            return method.invoke((Object) null, aobject);
        }
    }

    public static Object invokeStaticMethod(Class oclass, String s, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeStaticMethod(oclass, s, aobject, aclass);
    }

    public static Object invokeStaticMethod(Class oclass, String s, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        aclass = ArrayUtils.nullToEmpty(aclass);
        Method method = getMatchingAccessibleMethod(oclass, s, aclass);

        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + s + "() on class: " + oclass.getName());
        } else {
            return method.invoke((Object) null, aobject);
        }
    }

    public static Object invokeExactStaticMethod(Class oclass, String s, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeExactStaticMethod(oclass, s, aobject, aclass);
    }

    public static Method getAccessibleMethod(Class oclass, String s, Class... aclass) {
        try {
            return getAccessibleMethod(oclass.getMethod(s, aclass));
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        }
    }

    public static Method getAccessibleMethod(Method method) {
        if (!MemberUtils.isAccessible(method)) {
            return null;
        } else {
            Class oclass = method.getDeclaringClass();

            if (Modifier.isPublic(oclass.getModifiers())) {
                return method;
            } else {
                String s = method.getName();
                Class[] aclass = method.getParameterTypes();

                method = getAccessibleMethodFromInterfaceNest(oclass, s, aclass);
                if (method == null) {
                    method = getAccessibleMethodFromSuperclass(oclass, s, aclass);
                }

                return method;
            }
        }
    }

    private static Method getAccessibleMethodFromSuperclass(Class oclass, String s, Class... aclass) {
        for (Class oclass1 = oclass.getSuperclass(); oclass1 != null; oclass1 = oclass1.getSuperclass()) {
            if (Modifier.isPublic(oclass1.getModifiers())) {
                try {
                    return oclass1.getMethod(s, aclass);
                } catch (NoSuchMethodException nosuchmethodexception) {
                    return null;
                }
            }
        }

        return null;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class oclass, String s, Class... aclass) {
        while (oclass != null) {
            Class[] aclass1 = oclass.getInterfaces();

            for (int i = 0; i < aclass1.length; ++i) {
                if (Modifier.isPublic(aclass1[i].getModifiers())) {
                    try {
                        return aclass1[i].getDeclaredMethod(s, aclass);
                    } catch (NoSuchMethodException nosuchmethodexception) {
                        Method method = getAccessibleMethodFromInterfaceNest(aclass1[i], s, aclass);

                        if (method != null) {
                            return method;
                        }
                    }
                }
            }

            oclass = oclass.getSuperclass();
        }

        return null;
    }

    public static Method getMatchingAccessibleMethod(Class oclass, String s, Class... aclass) {
        Method method;

        try {
            method = oclass.getMethod(s, aclass);
            MemberUtils.setAccessibleWorkaround(method);
            return method;
        } catch (NoSuchMethodException nosuchmethodexception) {
            method = null;
            Method[] amethod = oclass.getMethods();
            Method[] amethod1 = amethod;
            int i = amethod.length;

            for (int j = 0; j < i; ++j) {
                Method method1 = amethod1[j];

                if (method1.getName().equals(s) && ClassUtils.isAssignable(aclass, method1.getParameterTypes(), true)) {
                    Method method2 = getAccessibleMethod(method1);

                    if (method2 != null && (method == null || MemberUtils.compareParameterTypes(method2.getParameterTypes(), method.getParameterTypes(), aclass) < 0)) {
                        method = method2;
                    }
                }
            }

            if (method != null) {
                MemberUtils.setAccessibleWorkaround(method);
            }

            return method;
        }
    }

    public static Set getOverrideHierarchy(Method method, ClassUtils.Interfaces classutils_interfaces) {
        Validate.notNull(method);
        LinkedHashSet linkedhashset = new LinkedHashSet();

        linkedhashset.add(method);
        Class[] aclass = method.getParameterTypes();
        Class oclass = method.getDeclaringClass();
        Iterator iterator = ClassUtils.hierarchy(oclass, classutils_interfaces).iterator();

        iterator.next();

        label29:
        while (iterator.hasNext()) {
            Class oclass1 = (Class) iterator.next();
            Method method1 = getMatchingAccessibleMethod(oclass1, method.getName(), aclass);

            if (method1 != null) {
                if (Arrays.equals(method1.getParameterTypes(), aclass)) {
                    linkedhashset.add(method1);
                } else {
                    Map map = TypeUtils.getTypeArguments(oclass, method1.getDeclaringClass());

                    for (int i = 0; i < aclass.length; ++i) {
                        Type type = TypeUtils.unrollVariables(map, method.getGenericParameterTypes()[i]);
                        Type type1 = TypeUtils.unrollVariables(map, method1.getGenericParameterTypes()[i]);

                        if (!TypeUtils.equals(type, type1)) {
                            continue label29;
                        }
                    }

                    linkedhashset.add(method1);
                }
            }
        }

        return linkedhashset;
    }
}
