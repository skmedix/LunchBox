package org.bukkit.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PermissibleBase implements Permissible {

    private ServerOperator opable = null;
    private Permissible parent = this;
    private final List attachments = new LinkedList();
    private final Map permissions = new HashMap();

    public PermissibleBase(ServerOperator opable) {
        this.opable = opable;
        if (opable instanceof Permissible) {
            this.parent = (Permissible) opable;
        }

        this.recalculatePermissions();
    }

    public boolean isOp() {
        return this.opable == null ? false : this.opable.isOp();
    }

    public void setOp(boolean value) {
        if (this.opable == null) {
            throw new UnsupportedOperationException("Cannot change op value as no ServerOperator is set");
        } else {
            this.opable.setOp(value);
        }
    }

    public boolean isPermissionSet(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else {
            return this.permissions.containsKey(name.toLowerCase());
        }
    }

    public boolean isPermissionSet(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        } else {
            return this.isPermissionSet(perm.getName());
        }
    }

    public boolean hasPermission(String inName) {
        if (inName == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else {
            String name = inName.toLowerCase();

            if (this.isPermissionSet(name)) {
                return ((PermissionAttachmentInfo) this.permissions.get(name)).getValue();
            } else {
                Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);

                return perm != null ? perm.getDefault().getValue(this.isOp()) : Permission.DEFAULT_PERMISSION.getValue(this.isOp());
            }
        }
    }

    public boolean hasPermission(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        } else {
            String name = perm.getName().toLowerCase();

            return this.isPermissionSet(name) ? ((PermissionAttachmentInfo) this.permissions.get(name)).getValue() : perm.getDefault().getValue(this.isOp());
        }
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        } else {
            PermissionAttachment result = this.addAttachment(plugin);

            result.setPermission(name, value);
            this.recalculatePermissions();
            return result;
        }
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        } else {
            PermissionAttachment result = new PermissionAttachment(plugin, this.parent);

            this.attachments.add(result);
            this.recalculatePermissions();
            return result;
        }
    }

    public void removeAttachment(PermissionAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("Attachment cannot be null");
        } else if (this.attachments.contains(attachment)) {
            this.attachments.remove(attachment);
            PermissionRemovedExecutor ex = attachment.getRemovalCallback();

            if (ex != null) {
                ex.attachmentRemoved(attachment);
            }

            this.recalculatePermissions();
        } else {
            throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this.parent);
        }
    }

    public void recalculatePermissions() {
        this.clearPermissions();
        Set defaults = Bukkit.getServer().getPluginManager().getDefaultPermissions(this.isOp());

        Bukkit.getServer().getPluginManager().subscribeToDefaultPerms(this.isOp(), this.parent);
        Iterator iterator = defaults.iterator();

        while (iterator.hasNext()) {
            Permission attachment = (Permission) iterator.next();
            String name = attachment.getName().toLowerCase();

            this.permissions.put(name, new PermissionAttachmentInfo(this.parent, name, (PermissionAttachment) null, true));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.parent);
            this.calculateChildPermissions(attachment.getChildren(), false, (PermissionAttachment) null);
        }

        iterator = this.attachments.iterator();

        while (iterator.hasNext()) {
            PermissionAttachment attachment1 = (PermissionAttachment) iterator.next();

            this.calculateChildPermissions(attachment1.getPermissions(), false, attachment1);
        }

    }

    public synchronized void clearPermissions() {
        Set perms = this.permissions.keySet();
        Iterator iterator = perms.iterator();

        while (iterator.hasNext()) {
            String name = (String) iterator.next();

            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(name, this.parent);
        }

        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this.parent);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this.parent);
        this.permissions.clear();
    }

    private void calculateChildPermissions(Map children, boolean invert, PermissionAttachment attachment) {
        Set keys = children.keySet();
        Iterator iterator = keys.iterator();

        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
            boolean value = ((Boolean) children.get(name)).booleanValue() ^ invert;
            String lname = name.toLowerCase();

            this.permissions.put(lname, new PermissionAttachmentInfo(this.parent, lname, attachment, value));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.parent);
            if (perm != null) {
                this.calculateChildPermissions(perm.getChildren(), !value, attachment);
            }
        }

    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        } else {
            PermissionAttachment result = this.addAttachment(plugin, ticks);

            if (result != null) {
                result.setPermission(name, value);
            }

            return result;
        }
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        } else {
            PermissionAttachment result = this.addAttachment(plugin);

            if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, (Runnable) (new PermissibleBase.RemoveAttachmentRunnable(result)), (long) ticks) == -1) {
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add PermissionAttachment to " + this.parent + " for plugin " + plugin.getDescription().getFullName() + ": Scheduler returned -1");
                result.remove();
                return null;
            } else {
                return result;
            }
        }
    }

    public Set getEffectivePermissions() {
        return new HashSet(this.permissions.values());
    }

    private class RemoveAttachmentRunnable implements Runnable {

        private PermissionAttachment attachment;

        public RemoveAttachmentRunnable(PermissionAttachment attachment) {
            this.attachment = attachment;
        }

        public void run() {
            this.attachment.remove();
        }
    }
}
