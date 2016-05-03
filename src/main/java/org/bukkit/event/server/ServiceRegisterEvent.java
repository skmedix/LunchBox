package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ServiceRegisterEvent extends ServiceEvent {

    private static final HandlerList handlers = new HandlerList();

    public ServiceRegisterEvent(RegisteredServiceProvider registeredProvider) {
        super(registeredProvider);
    }

    public HandlerList getHandlers() {
        return ServiceRegisterEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ServiceRegisterEvent.handlers;
    }
}
