package org.bukkit.util;

import java.lang.reflect.Array;

public class Java15Compat {

    public static Object[] Arrays_copyOfRange(Object[] original, int start, int end) {
        if (original.length >= start && start >= 0) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                Object[] copy = (Object[]) Array.newInstance(original.getClass().getComponentType(), length);

                System.arraycopy(original, start, copy, 0, copyLength);
                return copy;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
