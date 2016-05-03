package org.bukkit.craftbukkit.libs.joptsimple;

public interface ValueConverter {

    Object convert(String s);

    Class valueType();

    String valuePattern();
}
