package org.bukkit.craftbukkit.libs.joptsimple.internal;

public final class Classes {

    public static String shortNameOf(String className) {
        return className.substring(className.lastIndexOf(46) + 1);
    }

    static {
        new Classes();
    }
}
