package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class WeatherChangeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final boolean to;

    public WeatherChangeEvent(World world, boolean to) {
        super(world);
        this.to = to;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public boolean toWeatherState() {
        return this.to;
    }

    public HandlerList getHandlers() {
        return WeatherChangeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return WeatherChangeEvent.handlers;
    }
}
