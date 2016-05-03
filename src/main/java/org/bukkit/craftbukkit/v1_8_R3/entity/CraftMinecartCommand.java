package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.Set;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartCommandBlock;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class CraftMinecartCommand extends CraftMinecart implements CommandMinecart {

    private final PermissibleBase perm = new PermissibleBase(this);

    public CraftMinecartCommand(CraftServer server, EntityMinecartCommandBlock entity) {
        super(server, (EntityMinecartAbstract) entity);
    }

    public String getCommand() {
        return ((EntityMinecartCommandBlock) this.getHandle()).getCommandBlock().getCommand();
    }

    public void setCommand(String command) {
        ((EntityMinecartCommandBlock) this.getHandle()).getCommandBlock().setCommand(command != null ? command : "");
    }

    public void setName(String name) {
        ((EntityMinecartCommandBlock) this.getHandle()).getCommandBlock().setName(name != null ? name : "@");
    }

    public EntityType getType() {
        return EntityType.MINECART_COMMAND;
    }

    public void sendMessage(String message) {}

    public void sendMessage(String[] messages) {}

    public String getName() {
        return ((EntityMinecartCommandBlock) this.getHandle()).getCommandBlock().getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a minecart");
    }

    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    public Set getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    public Server getServer() {
        return Bukkit.getServer();
    }
}
