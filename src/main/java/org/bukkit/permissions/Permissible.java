package org.bukkit.permissions;

import java.util.Set;
import org.bukkit.plugin.Plugin;

public interface Permissible extends ServerOperator {

    boolean isPermissionSet(String s);

    boolean isPermissionSet(Permission permission);

    boolean hasPermission(String s);

    boolean hasPermission(Permission permission);

    PermissionAttachment addAttachment(Plugin plugin, String s, boolean flag);

    PermissionAttachment addAttachment(Plugin plugin);

    PermissionAttachment addAttachment(Plugin plugin, String s, boolean flag, int i);

    PermissionAttachment addAttachment(Plugin plugin, int i);

    void removeAttachment(PermissionAttachment permissionattachment);

    void recalculatePermissions();

    Set getEffectivePermissions();
}
