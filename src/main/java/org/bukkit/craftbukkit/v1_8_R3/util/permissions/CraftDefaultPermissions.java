package org.bukkit.craftbukkit.v1_8_R3.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CraftDefaultPermissions {

    private static final String ROOT = "minecraft";

    public static void registerCorePermissions() {
        Permission parent = DefaultPermissions.registerPermission("minecraft", "Gives the user the ability to use all vanilla utilities and commands");

        CommandPermissions.registerPermissions(parent);
        parent.recalculatePermissibles();
    }
}
