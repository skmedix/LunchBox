package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

abstract class MemberUtils {

    private static final int ACCESS_TEST = 7;
    private static final Class[] ORDERED_PRIMITIVE_TYPES = new Class[] { Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    static boolean setAccessibleWorkaround(AccessibleObject accessibleobject) {
        if (accessibleobject != null && !accessibleobject.isAccessible()) {
            Member member = (Member) accessibleobject;

            if (!accessibleobject.isAccessible() && Modifier.isPublic(member.getModifiers()) && isPackageAccess(member.getDeclaringClass().getModifiers())) {
                try {
                    accessibleobject.setAccessible(true);
                    return true;
                } catch (SecurityException securityexception) {
                    ;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    static boolean isPackageAccess(int i) {
        return (i & 7) == 0;
    }

    static boolean isAccessible(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers()) && !member.isSynthetic();
    }

    static int compareParameterTypes(Class[] aclass, Class[] aclass1, Class[] aclass2) {
        float f = getTotalTransformationCost(aclass2, aclass);
        float f1 = getTotalTransformationCost(aclass2, aclass1);

        return f < f1 ? -1 : (f1 < f ? 1 : 0);
    }

    private static float getTotalTransformationCost(Class[] aclass, Class[] aclass1) {
        float f = 0.0F;

        for (int i = 0; i < aclass.length; ++i) {
            Class oclass = aclass[i];
            Class oclass1 = aclass1[i];

            f += getObjectTransformationCost(oclass, oclass1);
        }

        return f;
    }

    private static float getObjectTransformationCost(Class oclass, Class oclass1) {
        if (oclass1.isPrimitive()) {
            return getPrimitivePromotionCost(oclass, oclass1);
        } else {
            float f;

            for (f = 0.0F; oclass != null && !oclass1.equals(oclass); oclass = oclass.getSuperclass()) {
                if (oclass1.isInterface() && ClassUtils.isAssignable(oclass, oclass1)) {
                    f += 0.25F;
                    break;
                }

                ++f;
            }

            if (oclass == null) {
                ++f;
            }

            return f;
        }
    }

    private static float getPrimitivePromotionCost(Class oclass, Class oclass1) {
        float f = 0.0F;
        Class oclass2 = oclass;

        if (!oclass.isPrimitive()) {
            f += 0.1F;
            oclass2 = ClassUtils.wrapperToPrimitive(oclass);
        }

        for (int i = 0; oclass2 != oclass1 && i < MemberUtils.ORDERED_PRIMITIVE_TYPES.length; ++i) {
            if (oclass2 == MemberUtils.ORDERED_PRIMITIVE_TYPES[i]) {
                f += 0.1F;
                if (i < MemberUtils.ORDERED_PRIMITIVE_TYPES.length - 1) {
                    oclass2 = MemberUtils.ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }

        return f;
    }
}
