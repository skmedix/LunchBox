package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class LightningStrikeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final LightningStrike bolt;

    public LightningStrikeEvent(World world, LightningStrike bolt) {
        super(world);
        this.bolt = bolt;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public LightningStrike getLightning() {
        return this.bolt;
    }

    public HandlerList getHandlers() {
        return LightningStrikeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return LightningStrikeEvent.handlers;
    }
}
