package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ServiceUnregisterEvent extends ServiceEvent {

    private static final HandlerList handlers = new HandlerList();

    public ServiceUnregisterEvent(RegisteredServiceProvider serviceProvider) {
        super(serviceProvider);
    }

    public HandlerList getHandlers() {
        return ServiceUnregisterEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ServiceUnregisterEvent.handlers;
    }
}
