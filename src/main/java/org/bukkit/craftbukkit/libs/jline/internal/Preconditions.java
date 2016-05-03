package org.bukkit.craftbukkit.libs.jline.internal;

public class Preconditions {

    public static Object checkNotNull(Object reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
