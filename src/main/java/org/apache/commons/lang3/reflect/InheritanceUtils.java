package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils {

    public static int distance(Class oclass, Class oclass1) {
        if (oclass != null && oclass1 != null) {
            if (oclass.equals(oclass1)) {
                return 0;
            } else {
                Class oclass2 = oclass.getSuperclass();
                int i = BooleanUtils.toInteger(oclass1.equals(oclass2));

                if (i == 1) {
                    return i;
                } else {
                    i += distance(oclass2, oclass1);
                    return i > 0 ? i + 1 : -1;
                }
            }
        } else {
            return -1;
        }
    }
}
