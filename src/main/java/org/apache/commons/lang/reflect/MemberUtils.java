package org.apache.commons.lang.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.SystemUtils;

abstract class MemberUtils {

    private static final int ACCESS_TEST = 7;
    private static final Method IS_SYNTHETIC;
    private static final Class[] ORDERED_PRIMITIVE_TYPES;
    static Class class$java$lang$reflect$Member;

    static void setAccessibleWorkaround(AccessibleObject o) {
        if (o != null && !o.isAccessible()) {
            Member m = (Member) o;

            if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    o.setAccessible(true);
                } catch (SecurityException securityexception) {
                    ;
                }
            }

        }
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !isSynthetic(m);
    }

    static boolean isSynthetic(Member m) {
        if (MemberUtils.IS_SYNTHETIC != null) {
            try {
                return ((Boolean) MemberUtils.IS_SYNTHETIC.invoke(m, (Object[]) null)).booleanValue();
            } catch (Exception exception) {
                ;
            }
        }

        return false;
    }

    static int compareParameterTypes(Class[] left, Class[] right, Class[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);

        return leftCost < rightCost ? -1 : (rightCost < leftCost ? 1 : 0);
    }

    private static float getTotalTransformationCost(Class[] srcArgs, Class[] destArgs) {
        float totalCost = 0.0F;

        for (int i = 0; i < srcArgs.length; ++i) {
            Class srcClass = srcArgs[i];
            Class destClass = destArgs[i];

            totalCost += getObjectTransformationCost(srcClass, destClass);
        }

        return totalCost;
    }

    private static float getObjectTransformationCost(Class srcClass, Class destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        } else {
            float cost;

            for (cost = 0.0F; srcClass != null && !destClass.equals(srcClass); srcClass = srcClass.getSuperclass()) {
                if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                    cost += 0.25F;
                    break;
                }

                ++cost;
            }

            if (srcClass == null) {
                ++cost;
            }

            return cost;
        }
    }

    private static float getPrimitivePromotionCost(Class srcClass, Class destClass) {
        float cost = 0.0F;
        Class cls = srcClass;

        if (!srcClass.isPrimitive()) {
            cost += 0.1F;
            cls = ClassUtils.wrapperToPrimitive(srcClass);
        }

        for (int i = 0; cls != destClass && i < MemberUtils.ORDERED_PRIMITIVE_TYPES.length; ++i) {
            if (cls == MemberUtils.ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1F;
                if (i < MemberUtils.ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = MemberUtils.ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }

        return cost;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static {
        Method isSynthetic = null;

        if (SystemUtils.isJavaVersionAtLeast(1.5F)) {
            try {
                isSynthetic = (MemberUtils.class$java$lang$reflect$Member == null ? (MemberUtils.class$java$lang$reflect$Member = class$("java.lang.reflect.Member")) : MemberUtils.class$java$lang$reflect$Member).getMethod("isSynthetic", ArrayUtils.EMPTY_CLASS_ARRAY);
            } catch (Exception exception) {
                ;
            }
        }

        IS_SYNTHETIC = isSynthetic;
        ORDERED_PRIMITIVE_TYPES = new Class[] { Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
    }
}
