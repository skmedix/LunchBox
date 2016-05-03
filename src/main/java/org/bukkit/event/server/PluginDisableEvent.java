package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class PluginDisableEvent extends PluginEvent {

    private static final HandlerList handlers = new HandlerList();

    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }

    public HandlerList getHandlers() {
        return PluginDisableEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PluginDisableEvent.handlers;
    }
}
