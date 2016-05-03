package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityExplodeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Location location;
    private final List blocks;
    private float yield;

    public EntityExplodeEvent(Entity what, Location location, List blocks, float yield) {
        super(what);
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public List blockList() {
        return this.blocks;
    }

    public Location getLocation() {
        return this.location;
    }

    public float getYield() {
        return this.yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public HandlerList getHandlers() {
        return EntityExplodeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityExplodeEvent.handlers;
    }
}
