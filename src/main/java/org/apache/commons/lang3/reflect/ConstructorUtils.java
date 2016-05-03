package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class ConstructorUtils {

    public static Object invokeConstructor(Class oclass, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeConstructor(oclass, aobject, aclass);
    }

    public static Object invokeConstructor(Class oclass, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        aclass = ArrayUtils.nullToEmpty(aclass);
        Constructor constructor = getMatchingAccessibleConstructor(oclass, aclass);

        if (constructor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + oclass.getName());
        } else {
            return constructor.newInstance(aobject);
        }
    }

    public static Object invokeExactConstructor(Class oclass, Object... aobject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        Class[] aclass = ClassUtils.toClass(aobject);

        return invokeExactConstructor(oclass, aobject, aclass);
    }

    public static Object invokeExactConstructor(Class oclass, Object[] aobject, Class[] aclass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        aobject = ArrayUtils.nullToEmpty(aobject);
        aclass = ArrayUtils.nullToEmpty(aclass);
        Constructor constructor = getAccessibleConstructor(oclass, aclass);

        if (constructor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + oclass.getName());
        } else {
            return constructor.newInstance(aobject);
        }
    }

    public static Constructor getAccessibleConstructor(Class oclass, Class... aclass) {
        Validate.notNull(oclass, "class cannot be null", new Object[0]);

        try {
            return getAccessibleConstructor(oclass.getConstructor(aclass));
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        }
    }

    public static Constructor getAccessibleConstructor(Constructor constructor) {
        Validate.notNull(constructor, "constructor cannot be null", new Object[0]);
        return MemberUtils.isAccessible(constructor) && isAccessible(constructor.getDeclaringClass()) ? constructor : null;
    }

    public static Constructor getMatchingAccessibleConstructor(Class oclass, Class... aclass) {
        Validate.notNull(oclass, "class cannot be null", new Object[0]);

        Constructor constructor;

        try {
            constructor = oclass.getConstructor(aclass);
            MemberUtils.setAccessibleWorkaround(constructor);
            return constructor;
        } catch (NoSuchMethodException nosuchmethodexception) {
            constructor = null;
            Constructor[] aconstructor = oclass.getConstructors();
            Constructor[] aconstructor1 = aconstructor;
            int i = aconstructor.length;

            for (int j = 0; j < i; ++j) {
                Constructor constructor1 = aconstructor1[j];

                if (ClassUtils.isAssignable(aclass, constructor1.getParameterTypes(), true)) {
                    constructor1 = getAccessibleConstructor(constructor1);
                    if (constructor1 != null) {
                        MemberUtils.setAccessibleWorkaround(constructor1);
                        if (constructor == null || MemberUtils.compareParameterTypes(constructor1.getParameterTypes(), constructor.getParameterTypes(), aclass) < 0) {
                            constructor = constructor1;
                        }
                    }
                }
            }

            return constructor;
        }
    }

    private static boolean isAccessible(Class oclass) {
        for (Class oclass1 = oclass; oclass1 != null; oclass1 = oclass1.getEnclosingClass()) {
            if (!Modifier.isPublic(oclass1.getModifiers())) {
                return false;
            }
        }

        return true;
    }
}
