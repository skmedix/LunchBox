package org.bukkit.craftbukkit.v1_8_R3.util;

import org.bukkit.util.CachedServerIcon;

public class CraftIconCache implements CachedServerIcon {

    public final String value;

    public CraftIconCache(String value) {
        this.value = value;
    }
}
