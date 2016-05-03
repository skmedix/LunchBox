package org.bukkit.craftbukkit.libs.joptsimple.internal;

public final class Objects {

    public static void ensureNotNull(Object target) {
        if (target == null) {
            throw new NullPointerException();
        }
    }

    static {
        new Objects();
    }
}
