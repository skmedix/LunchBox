package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SheepDyeWoolEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private DyeColor color;

    public SheepDyeWoolEvent(Sheep sheep, DyeColor color) {
        super(sheep);
        this.color = color;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Sheep getEntity() {
        return (Sheep) this.entity;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public HandlerList getHandlers() {
        return SheepDyeWoolEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return SheepDyeWoolEvent.handlers;
    }
}
