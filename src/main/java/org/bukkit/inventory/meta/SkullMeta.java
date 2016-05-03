package org.bukkit.inventory.meta;

public interface SkullMeta extends ItemMeta {

    String getOwner();

    boolean hasOwner();

    boolean setOwner(String s);

    SkullMeta clone();
}
