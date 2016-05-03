package org.bukkit.permissions;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class PermissionAttachment {

    private PermissionRemovedExecutor removed;
    private final Map permissions = new LinkedHashMap();
    private final Permissible permissible;
    private final Plugin plugin;

    public PermissionAttachment(Plugin plugin, Permissible Permissible) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        } else {
            this.permissible = Permissible;
            this.plugin = plugin;
        }
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public void setRemovalCallback(PermissionRemovedExecutor ex) {
        this.removed = ex;
    }

    public PermissionRemovedExecutor getRemovalCallback() {
        return this.removed;
    }

    public Permissible getPermissible() {
        return this.permissible;
    }

    public Map getPermissions() {
        return new LinkedHashMap(this.permissions);
    }

    public void setPermission(String name, boolean value) {
        this.permissions.put(name.toLowerCase(), Boolean.valueOf(value));
        this.permissible.recalculatePermissions();
    }

    public void setPermission(Permission perm, boolean value) {
        this.setPermission(perm.getName(), value);
    }

    public void unsetPermission(String name) {
        this.permissions.remove(name.toLowerCase());
        this.permissible.recalculatePermissions();
    }

    public void unsetPermission(Permission perm) {
        this.unsetPermission(perm.getName());
    }

    public boolean remove() {
        try {
            this.permissible.removeAttachment(this);
            return true;
        } catch (IllegalArgumentException illegalargumentexception) {
            return false;
        }
    }
}
