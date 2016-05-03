package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class PluginEnableEvent extends PluginEvent {

    private static final HandlerList handlers = new HandlerList();

    public PluginEnableEvent(Plugin plugin) {
        super(plugin);
    }

    public HandlerList getHandlers() {
        return PluginEnableEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PluginEnableEvent.handlers;
    }
}
